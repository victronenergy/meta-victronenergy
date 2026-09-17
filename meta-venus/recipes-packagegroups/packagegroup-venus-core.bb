SUMMARY = "Hardware-agnostic Venus application layer, shared by the OCI container and real hardware images"
DESCRIPTION = "Everything Venus needs except GUI, VNC, on-device software update, or hardware tied to one board."

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

require packagegroup-venus-core.inc

RDEPENDS:${PN} += "\
    can-bus-bms \
    can-bus-bms-hv \
    dbus-acsystem \
    dbus-adc \
    dbus-ble-sensors \
    dbus-bornay-windplus \
    dbus-canopen-motordrive \
    dbus-cgwacs \
    dbus-digitalinputs \
    dbus-eebus \
    dbus-fronius \
    dbus-fzsonick-48tl \
    dbus-generator \
    dbus-imt-si-rs485tc \
    dbus-modbus-client \
    dbus-modbustcp \
    dbus-modem \
    dbus-motordrive \
    dbus-mqtt-integrations \
    dbus-parallel-bms \
    dbus-pump \
    dbus-recorder \
    dbus-rv-c \
    dbus-shelly \
    dbus-spy \
    dbus-switch \
    dbus-systemcalc-py \
    dbus-tempsensor-relay \
    dbus-valence \
    dbus-vebus-to-pvinverter \
    dup \
    gps-dbus \
    gui-v2-webassembly \
    hub4control \
    localsettings \
    machine-runtime-conf \
    mk2-dbus \
    mk2vsc \
    mqtt-rpc \
    netmon \
    prodtest \
    python3-fcntl \
    serial-starter \
    service-advertiser \
    support-keys \
    vebus-system-config \
    vebus-updater \
    vecan-dbus \
    vedirect-interface \
    velib-tools \
    venus-access \
    venus-eeprom \
    venus-opportunity-loads \
    venus-platform \
    vesmart-server \
    vrmlogger \
    vup \
    xupc \
    xupd \
    xupt \
"

# needs a physical BLE/HCI adapter to do anything; not container-passthrough-capable yet
RDEPENDS:${PN}:remove:venus-container = " vesmart-server"

# ccgx is slow enough that an EEPROM probe that never finds anything costs real boot time
RDEPENDS:${PN}:remove:ccgx = "\
    dbus-mqtt-integrations \
    dbus-shelly \
    netmon \
    venus-eeprom \
    venus-opportunity-loads \
"
RDEPENDS:${PN}:remove:canvu500 = "netmon"
RDEPENDS:${PN}:append:nanopi = " dbus-paygo"

# real hardware gets this from swupdate-scripts' scan-versions.sh instead
RDEPENDS:${PN}:append:venus-container = " venus-container-versions"

# real hardware sets this when the native gui-v2 Qt process starts; the
# container never runs one (gui-v2 is served client-side, in the browser)
RDEPENDS:${PN}:append:venus-container = " venus-container-gui-version"
