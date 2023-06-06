DESCRIPTION = "This is a VNC server for wlroots based Wayland compositors."
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYING;md5=94fc374e7174f41e3afe0f027ee59ff7"

DEPENDS += "aml libdrm libxkbcommon neatvnc virtual/libgbm wayland wayland-native"

inherit meson

SRC_URI = "git://github.com/any1/wayvnc;protocol=https"
SRCREV = "8038e655971144af29962cdbf76a7b17be6dfb26"
S = "${WORKDIR}/git"

do_install:append() {
    mkdir -p ${D}${sysconfdir}/sway/config.d
    echo "# NOTE: completely unsecure!" > ${D}${sysconfdir}/sway/config.d/wayvnc.conf
    echo "exec_always /usr/bin/wayvnc -C /dev/null 0.0.0.0" >> ${D}${sysconfdir}/sway/config.d/wayvnc.conf
}
