FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

KBUILD_DEFCONFIG_raspberrypi ?= "bcmrpi_defconfig"
KBUILD_DEFCONFIG_raspberrypi2 ?= "bcm2709_defconfig"
KBUILD_DEFCONFIG_raspberrypi4 ?= "bcm2711_defconfig"

SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel

COMPATIBLE_MACHINE = "^rpi$"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ${KBUILD_DEFCONFIG}"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

SRCREV = "10552421195fbfe7a48ff8f692f0684c32229e46"
SRC_URI = "git://github.com/victronenergy/linux.git;protocol=git;branch=rpi_4.19.81"

# NOTE: the regular dtb handling flattens the overlays with the
# normal dtbs. So handle dtb seperately.
do_compile_append() {
    for DTB in ${RPI_KERNEL_DEVICETREE}; do
        DTB=`normalize_dtb "${DTB}"`
        oe_runmake ${DTB}
    done
}

do_install_append() {
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

do_deploy_append() {
    for DTB in ${RPI_KERNEL_DEVICETREE}; do
        DIR="$(dirname ${DTB})"
        install -d ${DEPLOYDIR}/${DIR}
        DTB_PATH=`get_real_dtb_path_in_kernel "${DTB}"`
        install -m 0644 ${DTB_PATH} ${DEPLOYDIR}/${DIR}/${DTB_NAME}.${DTB_EXT}
    done
}

FILES_kernel-devicetree += "/boot/overlays"
