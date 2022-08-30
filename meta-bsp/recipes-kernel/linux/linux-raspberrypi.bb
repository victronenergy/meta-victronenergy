require linux-venus.bb

COMPATIBLE_MACHINE = "^rpi$"

LINUX_VERSION = "5.10.110"
LINUX_VERSION_VENUS = "3"
LINUX_VERSION_EXTENSION =. "-rpi"
GIT_BRANCH =. "rpi-"

# NOTE: the regular dtb handling flattens the overlays with the
# normal dtbs, versions them and creates symlinks. Since that is
# unwanted, handle dtb seperately and keep KERNEL_DEVICETREE unset.
# Don't install the dtbs in /boot, since those must be installed in
# the FAT partition for raspberrypi's, not the rootfs.

RDEPENDS:${KERNEL_PACKAGE_NAME}-base:remove = "kernel-devicetree"

do_compile:append() {
    oe_runmake ${RPI_KERNEL_DEVICETREE}
}

do_deploy:append() {
    src=$(get_real_dtb_path_in_kernel)
    for DTB in ${RPI_KERNEL_DEVICETREE}; do
        install -d "${DEPLOYDIR}/$(dirname ${DTB})"
        install -m 0644 "$src/${DTB}" "${DEPLOYDIR}/${DTB}"
    done
}
