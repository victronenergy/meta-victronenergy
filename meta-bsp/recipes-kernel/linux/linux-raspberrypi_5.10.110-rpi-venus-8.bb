require linux-venus.inc

COMPATIBLE_MACHINE = "^rpi$"

SRC_URI = " \
    git://github.com/victronenergy/linux.git;protocol=https;nobranch=1 \
    file://0001-gcc-plugins-remove-code-for-GCC-versions-older-than-.patch \
    file://0002-gcc-plugins-remove-support-for-GCC-4.9-and-older.patch \
    file://0003-gcc-plugins-remove-duplicate-include-in-gcc-common.h.patch \
    file://0004-gcc-plugins-Reorganize-gimple-includes-for-GCC-13.patch \
    file://0001-work-around-for-too-few-arguments-to-function-init_d.patch \
    file://0001-ata-ahci-fix-enum-constants-for-gcc-13.patch \
    file://wifi_cfg80211_certificate.patch \
"
UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRCREV = "3859262148951d08d011c96d637383883c451739"

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

