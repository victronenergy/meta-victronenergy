require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=a2c678cfd4a4d97135585cad908541c6"

COMPATIBLE_MACHINE = "nanopi"

DEPENDS += "bc-native dtc-native swig-native u-boot-mkimage-native"

SRC_URI = " \
	git://github.com/victronenergy/u-boot.git;protocol=https;branch=nanopi \
	file://install.cmds \
"
SRCREV = "7de8ad4372d15404069f5cf77f4a5418ab7f9c07"

S = "${WORKDIR}/git"

do_compile_append () {
	mkimage -A arm -T script -C none -n 'Install Script' \
		-d ${WORKDIR}/install.cmds ${WORKDIR}/install.scr
}

do_deploy_append () {
	install -d ${DEPLOYDIR}
	install -m 0644 ${WORKDIR}/install.scr ${DEPLOYDIR}/install-${MACHINE}.scr
}
