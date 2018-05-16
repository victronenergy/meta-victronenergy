require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "nanopi"

DEPENDS += "bc-native coreutils-native dtc-native swig-native u-boot-mkimage-native"

SRC_URI = " \
	https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz \
	file://install.cmds \
"
SRC_URI[md5sum] = "7698560176f9c6b214fa914a87830ed5"
SRC_URI[sha256sum] = "53c9fb151757b12144b00bb2221f6ad39c095a507044fdfe027677414f84e3a2"

S = "${WORKDIR}/u-boot-${PV}"

do_compile_append () {
	mkimage -A arm -T script -C none -n 'Install Script' \
		-d ${WORKDIR}/install.cmds ${WORKDIR}/install.scr
}

do_deploy_append () {
	install -d ${DEPLOYDIR}
	install -m 0644 ${WORKDIR}/install.scr ${DEPLOYDIR}/install-${MACHINE}.scr
}
