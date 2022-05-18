require linux-venus.bb

COMPATIBLE_MACHINE = "^rpi$"

LINUX_VERSION = "5.10.110"
LINUX_VERSION_VENUS = "1"
LINUX_VERSION_EXTENSION =. "-rpi"
GIT_BRANCH =. "rpi-"

RDEPENDS:${KERNEL_PACKAGE_NAME}-base:remove = "kernel-devicetree"

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
