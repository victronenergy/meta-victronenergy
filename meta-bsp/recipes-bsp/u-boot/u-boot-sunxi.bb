require u-boot.inc
require u-boot-sunxi.inc

DEPENDS += "bc-native coreutils-native dtc-native swig-native u-boot-mkimage-native"

SRC_URI += "file://install.cmds"
SRC_URI += "file://0001-revert-sunxi-Use-binman-for-sunxi-boards.patch"
SRC_URI += "file://0001-don-t-build-libpyfdt.patch"

do_compile:append () {
    mkimage -A arm -T script -C none -n 'Install Script' \
        -d ${WORKDIR}/install.cmds ${WORKDIR}/install.scr
}

do_deploy:append () {
    install -d ${DEPLOYDIR}
    install -m 0644 ${WORKDIR}/install.scr ${DEPLOYDIR}/install.scr
}
