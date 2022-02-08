FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}-${PV}:"

KBUILD_DEFCONFIG_raspberrypi ?= "bcmrpi_defconfig"
KBUILD_DEFCONFIG:raspberrypi2 ?= "bcm2709_defconfig"
KBUILD_DEFCONFIG:raspberrypi4 ?= "bcm2711_defconfig"

SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

COMPATIBLE_MACHINE = "^rpi$"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ${KBUILD_DEFCONFIG}"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

#SRCREV = "6ff6f0c970199071c79176ec6147fda82fb28530"
#SRC_URI = "git://github.com/victronenergy/linux.git;protocol=git;branch=rpi_4.19.81"

SRCREV = "1375d5eac7a0fd578b49c8dd272425053c322f9c"
SRC_URI = "git://github.com/nmbath/linux.git;protocol=git;branch=rpi-5.10.y"

# needed for building newer perf
#SRC_URI += "file://0001-perf-Make-perf-able-to-build-with-latest-libbfd.patch"

# fix make[3]: *** [scripts/extract-cert] Error 1
DEPENDS += "openssl-native"

# NOTE: the regular dtb handling flattens the overlays with the
# normal dtbs. So handle dtb seperately.
do_compile:append() {
    for DTB in ${RPI_KERNEL_DEVICETREE}; do
        DTB=`normalize_dtb "${DTB}"`
        oe_runmake ${DTB}
    done
}

do_install:append() {
    for DTB in ${RPI_KERNEL_DEVICETREE}; do
        DIR="$(dirname ${DTB})"
        DTB=`normalize_dtb "${DTB}"`
        DTB_EXT=${DTB##*.}
        DTB_PATH=`get_real_dtb_path_in_kernel "${DTB}"`
        DTB_BASE_NAME=`basename ${DTB} ."${DTB_EXT}"`
        install -d ${D}/${KERNEL_IMAGEDEST}/${DIR}
        install -m 0644 ${DTB_PATH} ${D}/${KERNEL_IMAGEDEST}/${DIR}/${DTB_BASE_NAME}.${DTB_EXT}
    done
}

do_deploy:append() {
    for DTB in ${RPI_KERNEL_DEVICETREE}; do
        DIR="$(dirname ${DTB})"
        install -d ${DEPLOYDIR}/${DIR}
        DTB_PATH=`get_real_dtb_path_in_kernel "${DTB}"`
        install -m 0644 ${DTB_PATH} ${DEPLOYDIR}/${DIR}/${DTB_NAME}.${DTB_EXT}
    done
}

FILES:${KERNEL_PACKAGE_NAME}-devicetree += "/boot/overlays"
