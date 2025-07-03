SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

COMPATIBLE_MACHINE = "^rpi$"

SRC_URI = " \
    git://github.com/victronenergy/linux.git;name=machine;branch=rpi-venus-6.12.25;protocol=https \
"

SRCREV = "082125db8bdc2bc571004a1946f06cf4c8e38df8"
S = "${WORKDIR}/git"
UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ${KERNEL_CONFIG}"

DEPENDS += "coreutils-native openssl-native"
HOST_EXTRACFLAGS += "-I${STAGING_INCDIR_NATIVE}"

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

