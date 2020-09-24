DESCRIPTION = "sway is an i3-compatible Wayland compositor."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=dfc67e5b1fa10ebb4b70eb0c0ca67bea"

DEPENDS += "cairo libpcre libevdev libinput libxkbcommon json-c pango virtual/libgles2 wayland wayland-native wayland-protocols wlroots"

inherit meson

SRC_URI = " \
    git://github.com/swaywm/sway;protocol=https;branch=v1.5 \
    file://0001-skip-root-check.patch \
    file://config \
"
SRCREV = "108b8e97b77acba6206cdc69e419531d17380c7a"
S = "${WORKDIR}/git"

# note: weston is needed since it provides the default terminal
RDEPENDS:${PN} += "xkeyboard-config weston"
RCONFLICTS:${PN} += "weston-init"
RRECOMMENDS:${PN} += "ttf-bitstream-vera"

PACKAGES += "${PN}-backgrounds"
FILES:${PN}-backgrounds += "${datadir}/backgrounds"

PACKAGES += "${PN}-completion"
FILES:${PN}-completion += " \
    /usr/share/fish \
    /usr/share/bash-completion \
    /usr/share/fish \
    /usr/share/zsh \
"

FILES:${PN} += "usr/share/wayland-sessions"

do_install:append() {
    install ${WORKDIR}/config ${D}${sysconfdir}/sway/config
}


