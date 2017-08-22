require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=0507cd7da8e7ad6d6701926ec9b84c95"

COMPATIBLE_MACHINE = "beaglebone"

UBOOT_LOCALVERSION = "-venus"
UBOOT_ENV = "uEnv"

S = "${WORKDIR}/u-boot-${PV}"

SRC_URI = " \
	https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz \
	file://uEnv.txt \
	file://install.cmds \
"
SRC_URI[md5sum] = "2fbe0aa0778d7343d74c513d5e219e56"
SRC_URI[sha256sum] = "40ae2388b1d8910219f02aef83b50004737c6f4b3d8383fe62fbe7beee72efc9"

do_compile_append () {
	mkimage -A arm -T script -C none -n 'Install Script' \
		-d ${WORKDIR}/install.cmds ${WORKDIR}/install.scr
}

do_deploy_append () {
	install -d ${DEPLOYDIR}
	install -m 0644 ${WORKDIR}/install.scr ${DEPLOYDIR}/install-${MACHINE}.scr
}
