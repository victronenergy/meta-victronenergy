FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}-${PV}:"

KBUILD_DEFCONFIG_raspberrypi ?= "bcmrpi_defconfig"
KBUILD_DEFCONFIG_raspberrypi2 ?= "bcm2709_defconfig"

SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel

COMPATIBLE_MACHINE = "^rpi$"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ${KBUILD_DEFCONFIG}"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

SRCREV = "7f9c648dad6473469b4133898fa6bb8d818ecff9"
SRC_URI = "git://github.com/raspberrypi/linux.git;protocol=git;branch=rpi-4.9.y \
    file://logo.patch \
    file://hjelmslund.patch \
    file://logo.cfg \
    file://slcan.cfg \
    file://can-peak.cfg \
"

# NOTE: the OE-core dtb handling flattens the overlays with the
# normal dtbs, renames them, installs symlinks etc and that needs
# to be undone again when making an image. So handle dtb ourselves.

do_compile_append() {
    for DTB in ${RPI_KERNEL_DEVICETREE}; do
        DTB="$(normalize_dtb ${DTB})"
        oe_runmake ${DTB}
    done
}

do_install_append() {
    for DTB in ${RPI_KERNEL_DEVICETREE}; do
        DTB=`normalize_dtb "${DTB}"`
        DTB_EXT=${DTB##*.}
        DTB_PATH=`get_real_dtb_path_in_kernel "${DTB}"`
        DTB_BASE_NAME=`basename ${DTB} ."${DTB_EXT}"`
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
