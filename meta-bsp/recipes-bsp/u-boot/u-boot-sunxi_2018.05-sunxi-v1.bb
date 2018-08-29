require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "sunxi"

DEPENDS += "bc-native coreutils-native dtc-native swig-native u-boot-mkimage-native"

SRC_URI = " \
	https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz \
	file://install.cmds \
"
SRC_URI[md5sum] = "dda33501ea5cef319700dca67c650ff6"
SRC_URI[sha256sum] = "89585c5d34d1858abd0b39993242a37fcd3eafb03037dbc7359e4366bf01ee4d"

S = "${WORKDIR}/u-boot-${PV}"

do_compile_append () {
	mkimage -A arm -T script -C none -n 'Install Script' \
		-d ${WORKDIR}/install.cmds ${WORKDIR}/install.scr
}

do_deploy_append () {
	install -d ${DEPLOYDIR}
	install -m 0644 ${WORKDIR}/install.scr ${DEPLOYDIR}/install-${MACHINE}.scr
}
