require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc
require u-boot-sunxi.inc

DEPENDS += "bc-native coreutils-native dtc-native swig-native u-boot-mkimage-native"

SRC_URI += "file://install.cmds"

do_compile_append () {
    mkimage -A arm -T script -C none -n 'Install Script' \
        -d ${WORKDIR}/install.cmds ${WORKDIR}/install.scr
}

do_deploy_append () {
    install -d ${DEPLOYDIR}
    install -m 0644 ${WORKDIR}/install.scr ${DEPLOYDIR}/install-${MACHINE}.scr
}
