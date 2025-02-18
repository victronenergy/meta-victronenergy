FILESEXTRAPATHS:prepend := "${THISDIR}/files:"
SRC_URI += "file://0001-wpa_supplicant-enable-unix-socket-as-well.patch"

do_configure:append() {
    echo CONFIG_CTRL_IFACE_DBUS_INTRO=y >> wpa_supplicant/.config
    echo CONFIG_DEBUG_SYSLOG=y >> wpa_supplicant/.config
}

# wpa-supplicant is started by dbus, not by the scripts below see:
# /usr/share/dbus-1/system-services/fi.w1.wpa_supplicant1.service
do_install:append() {
	rm -rf ${D}${sysconfdir}/network/if-pre-up.d
	rm -rf ${D}${sysconfdir}/network/if-post-down.d
}
