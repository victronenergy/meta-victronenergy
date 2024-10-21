LINUX_VERSION ?= "6.6.36"
LINUX_RPI_BRANCH ?= "rpi-6.6.y"
LINUX_RPI_KMETA_BRANCH ?= "yocto-6.6"

SRCREV_machine = "769634f344626ed73bcda14c91b567067974d7b2"
SRCREV_meta = "733366844f5e114221372929392bf237fc8d823c"

KMETA = "kernel-meta"

SRC_URI = " \
    git://github.com/raspberrypi/linux.git;name=machine;branch=${LINUX_RPI_BRANCH};protocol=https \
    git://git.yoctoproject.org/yocto-kernel-cache;type=kmeta;name=meta;branch=${LINUX_RPI_KMETA_BRANCH};destsuffix=${KMETA} \
    file://powersave.cfg \
    file://venus.cfg \
    file://old-kmod.cfg \
    "

require linux-raspberrypi.inc

KERNEL_DTC_FLAGS += "-@ -H epapr"

do_compile:append() {
    oe_runmake ${RPI_KERNEL_DEVICETREE}
}

do_deploy:append() {
    src=$(get_real_dtb_path_in_kernel)
    # Install the overlays in DEPLOYDIR/overlays/
    for DTB in ${RPI_KERNEL_DEVICETREE_OVERLAYS}; do
        install -d "${DEPLOYDIR}/$(dirname ${DTB})"
        install -m 0644 "$src/${DTB}" "${DEPLOYDIR}/${DTB}"
    done
    # Install the board dtb(s) in DEPLOYDIR/
    for DTB in ${RPI_KERNEL_DEVICETREE_DTB}; do
        install -m 0644 "$src/${DTB}" "${DEPLOYDIR}/${DTB#broadcom/}"
    done
}
