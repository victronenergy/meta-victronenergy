<?php
/**
 * BLE Gateway to dbus-ble-sensors UDP forwarder
 *
 * Receives HTTP POST from Ruuvi Gateway or Shelly, extracts BLE advertisement data,
 * and forwards it to the dbus-ble-sensors UDP socket.
 *
 * nginx configuration:
 *   location /ble-gw {
 *       fastcgi_pass unix:/var/run/php/php-fpm.sock;
 *       fastcgi_param SCRIPT_FILENAME /path/to/ble-gw.php;
 *       include fastcgi_params;
 *   }
 */
error_reporting(0);

define('UDP_HOST', '127.0.0.1');
define('UDP_PORT', 18542);

define('BLE_SOCKET_VERSION', 0x01);
define('FLAG_RSSI', 0x01);
define('FLAG_REPEATER', 0x02);

// Security limits
define('MAX_INPUT_SIZE', 65536);        // 64KB max
define('MAX_TAGS_PER_REQUEST', 50);     // Max sensors per request
define('MAX_ADV_DATA_SIZE', 512);       // Max advertisement data size
define('MAX_MFG_DATA_SIZE', 255);       // Max manufacturer data (fits in uint8)
define('MIN_RSSI', -127);
define('MAX_RSSI', 0);

// Allowed manufacturer IDs (whitelist)
define('ALLOWED_MFG_IDS', [
    0x0499,  // Ruuvi
    0x0059,  // Mopeka/Nordic
    0x0067,  // Safiery
    0x02E1,  // SolarSense
    0x0F53,  // Gobius
]);

/**
 * Validate MAC address format
 */
function validate_mac(string $mac): bool {
    return preg_match('/^[0-9A-Fa-f]{2}(:[0-9A-Fa-f]{2}){5}$/', $mac) === 1;
}

/**
 * Validate hex string
 */
function validate_hex(string $hex): bool {
    return ctype_xdigit($hex) && strlen($hex) % 2 === 0;
}

/**
 * Convert MAC address string to bytes in bdaddr_t order (LSB first)
 */
function mac_to_bytes(string $mac): ?string {
    if (!validate_mac($mac)) {
        return null;
    }

    $parts = array_map('hexdec', explode(':', $mac));
    if (count($parts) !== 6) {
        return null;
    }

    foreach ($parts as $byte) {
        if ($byte < 0 || $byte > 255) {
            return null;
        }
    }

    return pack('C6', ...array_reverse($parts));
}

/**
 * Parse BLE advertisement data to extract manufacturer specific data
 * Returns ['mfg_id' => int, 'mfg_data' => string] or null
 */
function parse_advertisement(string $hex_data): ?array {
    if (!validate_hex($hex_data)) {
        return null;
    }

    if (strlen($hex_data) > MAX_ADV_DATA_SIZE * 2) {
        return null;
    }

    $data = hex2bin($hex_data);
    if ($data === false) {
        return null;
    }

    $len = strlen($data);
    $offset = 0;

    while ($offset < $len - 1) {
        $ad_len = ord($data[$offset]);

        if ($ad_len === 0 || $ad_len > 255) {
            break;
        }

        if ($offset + $ad_len >= $len) {
            break;
        }

        $ad_type = ord($data[$offset + 1]);
        $ad_data = substr($data, $offset + 2, $ad_len - 1);

        if ($ad_type === 0xff && strlen($ad_data) >= 2) {
            $mfg_id = unpack('v', substr($ad_data, 0, 2))[1];

            if (!in_array($mfg_id, ALLOWED_MFG_IDS, true)) {
                return null;
            }

            $mfg_data = substr($ad_data, 2);

            if (strlen($mfg_data) > MAX_MFG_DATA_SIZE) {
                return null;
            }

            return [
                'mfg_id' => $mfg_id,
                'mfg_data' => $mfg_data
            ];
        }

        $offset += $ad_len + 1;
    }

    return null;
}

/**
 * Build UDP packet for dbus-ble-sensors
 */
function build_packet(string $sensor_mac, int $mfg_id, string $mfg_data,
                      ?int $rssi = null, ?string $repeater_mac = null): ?string {
    if (!in_array($mfg_id, ALLOWED_MFG_IDS, true)) {
        return null;
    }

    if (strlen($mfg_data) > MAX_MFG_DATA_SIZE) {
        return null;
    }

    if ($rssi !== null && ($rssi < MIN_RSSI || $rssi > MAX_RSSI)) {
        return null;
    }

    $flags = 0;
    if ($rssi !== null) {
        $flags |= FLAG_RSSI;
    }
    if ($repeater_mac !== null) {
        $flags |= FLAG_REPEATER;
    }

    $packet = pack('CC', BLE_SOCKET_VERSION, $flags);

    $sensor_mac_bytes = mac_to_bytes($sensor_mac);
    if ($sensor_mac_bytes === null) {
        return null;
    }
    $packet .= $sensor_mac_bytes;

    $packet .= pack('v', $mfg_id);
    $packet .= pack('C', strlen($mfg_data));
    $packet .= $mfg_data;

    if ($rssi !== null) {
        $packet .= pack('c', $rssi);
    }

    if ($repeater_mac !== null) {
        $repeater_mac_bytes = mac_to_bytes($repeater_mac);
        if ($repeater_mac_bytes === null) {
            return null;
        }
        $packet .= $repeater_mac_bytes;
    }

    return $packet;
}

/**
 * Send UDP packet using fsockopen
 */
function send_udp(string $packet): bool {
    $fp = @fsockopen('udp://' . UDP_HOST, UDP_PORT, $errno, $errstr, 1);
    if ($fp === false) {
        return false;
    }

    $result = fwrite($fp, $packet);
    fclose($fp);

    return $result !== false;
}

/**
 * Process incoming request
 */
function process_request(): void {
    if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
        http_response_code(405);
        echo json_encode(['error' => 'Method not allowed']);
        return;
    }

    $content_type = $_SERVER['CONTENT_TYPE'] ?? '';
    if (strpos($content_type, 'application/json') === false) {
        http_response_code(415);
        echo json_encode(['error' => 'Content-Type must be application/json']);
        return;
    }

    $content_length = intval($_SERVER['CONTENT_LENGTH'] ?? 0);
    if ($content_length > MAX_INPUT_SIZE || $content_length === 0) {
        http_response_code(413);
        echo json_encode(['error' => 'Request too large or empty']);
        return;
    }

    $input = file_get_contents('php://input', false, null, 0, MAX_INPUT_SIZE);
    if ($input === false || strlen($input) === 0) {
        http_response_code(400);
        echo json_encode(['error' => 'Empty request body']);
        return;
    }

    $json = json_decode($input, true);
    if ($json === null || json_last_error() !== JSON_ERROR_NONE) {
        http_response_code(400);
        echo json_encode(['error' => 'Invalid JSON: ' . json_last_error_msg()]);
        return;
    }

    $data = $json['data'] ?? $json;

    if (!isset($data['tags']) || !is_array($data['tags'])) {
        http_response_code(400);
        echo json_encode(['error' => 'Missing or invalid tags array']);
        return;
    }

    $tag_count = count($data['tags']);
    if ($tag_count > MAX_TAGS_PER_REQUEST) {
        http_response_code(400);
        echo json_encode(['error' => 'Too many tags']);
        return;
    }

    if ($tag_count === 0) {
        http_response_code(200);
        header('Content-Type: application/json');
        echo json_encode([
            'status' => 'ok',
            'forwarded' => 0,
            'errors' => 0,
            'rejected' => 0
        ]);
        return;
    }

    $gw_mac = $data['gw_mac'] ?? null;

    if ($gw_mac !== null && !validate_mac($gw_mac)) {
        http_response_code(400);
        echo json_encode(['error' => 'Invalid gateway MAC address']);
        return;
    }

    $forwarded = 0;
    $errors = 0;
    $rejected = 0;

    foreach ($data['tags'] as $sensor_mac => $tag_data) {
        if (!is_string($sensor_mac) || !validate_mac($sensor_mac)) {
            $errors++;
            continue;
        }

        if (!is_array($tag_data) || !isset($tag_data['data']) || !is_string($tag_data['data'])) {
            $errors++;
            continue;
        }

        $adv = parse_advertisement($tag_data['data']);
        if ($adv === null) {
            $rejected++;
            continue;
        }

        $rssi = null;
        if (isset($tag_data['rssi'])) {
            $rssi = intval($tag_data['rssi']);
            if ($rssi < MIN_RSSI || $rssi > MAX_RSSI) {
                $errors++;
                continue;
            }
        }

        $packet = build_packet(
            $sensor_mac,
            $adv['mfg_id'],
            $adv['mfg_data'],
            $rssi,
            $gw_mac
        );

        if ($packet === null) {
            $errors++;
            continue;
        }

        if (send_udp($packet)) {
            $forwarded++;
        } else {
            $errors++;
        }
    }

    http_response_code(200);
    header('Content-Type: application/json');
    echo json_encode([
        'status' => 'ok',
        'forwarded' => $forwarded,
        'errors' => $errors,
        'rejected' => $rejected
    ]);
}

process_request();

?>
