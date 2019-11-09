FILESEXTRAPATHS_prepend := "${THISDIR}/files:"
SRC_URI += "file://0001-wpa_supplicant-enable-unix-socket-as-well.patch"

do_configure_append() {
    echo CONFIG_CTRL_IFACE_DBUS_INTRO=y >> wpa_supplicant/.config
    echo CONFIG_DEBUG_SYSLOG=y >> wpa_supplicant/.config
}
