require u-boot.inc
require u-boot-sunxi.inc

DEPENDS += "bc-native coreutils-native dtc-native swig-native u-boot-mkimage-native"

SRC_URI += "file://install.cmds"

do_compile:append () {
    mkimage -A arm -T script -C none -n 'Install Script' \
        -d ${UNPACKDIR}/install.cmds ${UNPACKDIR}/install.scr
}

do_deploy:append () {
    install -d ${DEPLOYDIR}
    install -m 0644 ${UNPACKDIR}/install.scr ${DEPLOYDIR}/install.scr
}
