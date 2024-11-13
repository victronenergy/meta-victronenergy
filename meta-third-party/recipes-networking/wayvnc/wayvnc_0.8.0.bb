DESCRIPTION = "This is a VNC server for wlroots based Wayland compositors."
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYING;md5=94fc374e7174f41e3afe0f027ee59ff7"

DEPENDS += "aml libdrm libxkbcommon neatvnc virtual/libgbm wayland wayland-native"

inherit meson pkgconfig

SRC_URI = "git://github.com/any1/wayvnc;protocol=https;branch=v0.8"
SRCREV = "15d09b0f9f971792c1a09a5e53640951b8b74aac"
S = "${WORKDIR}/git"

do_install:append() {
    mkdir -p ${D}${sysconfdir}/sway/config.d
    echo "# NOTE: completely unsecure!" > ${D}${sysconfdir}/sway/config.d/wayvnc.conf
    echo "exec_always /usr/bin/wayvnc -C /dev/null 0.0.0.0" >> ${D}${sysconfdir}/sway/config.d/wayvnc.conf
}
