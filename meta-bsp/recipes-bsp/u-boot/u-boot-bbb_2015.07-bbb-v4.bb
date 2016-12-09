require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=0507cd7da8e7ad6d6701926ec9b84c95"

UBOOT_LOCALVERSION = "-venus"
UBOOT_ENV = "uEnv"

S = "${WORKDIR}/u-boot-${PV}"

SRC_URI = " \
	https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz \
	file://uEnv.txt \
	file://install.cmds \
"
SRC_URI[md5sum] = "e5ee3a9c5c1338a8d5f685f74234fc51"
SRC_URI[sha256sum] = "ff43e6c4b6fdc6bf1054dff627fad7b8cb39de9fe7489a0df08fcc250c97835a"

do_compile_append () {
	mkimage -A arm -T script -C none -n 'Install Script' \
		-d ${WORKDIR}/install.cmds ${WORKDIR}/install.scr
}

do_deploy_append () {
	install -d ${DEPLOY_DIR_IMAGE}
	install -m 0644 ${WORKDIR}/install.scr ${DEPLOY_DIR_IMAGE}/install-${MACHINE}.scr
}
