require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=0507cd7da8e7ad6d6701926ec9b84c95"

UBOOT_LOCALVERSION = "-venus"
UBOOT_ENV = "uEnv"

S = "${WORKDIR}/u-boot-2015.07"

SRC_URI = " \
	ftp://ftp.denx.de/pub/u-boot/u-boot-2015.07.tar.bz2 \
	file://0001-Remove-SPL_OS_BOOT-config-option.patch \
	file://uEnv.txt \
"
SRC_URI[md5sum] = "3dac9a0b46fed77fc768ad3bd2d68c05"
SRC_URI[sha256sum] = "0b48c9bd717f2c322ef791f8282e14c88be942dc7d1226df7e31a812a3af94d9"
