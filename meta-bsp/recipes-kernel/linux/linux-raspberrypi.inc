require linux-rpi.inc
inherit linux-kernel-base

DESCRIPTION = "Linux Kernel for Raspberry Pi"
SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

COMPATIBLE_MACHINE = "raspberrypi"

PE = "1"
PV = "${LINUX_VERSION}+git${SRCPV}"

KMACHINE ?= "${MACHINE}"
KCONFIG_MODE = "--alldefconfig"
KBUILD_DEFCONFIG_raspberrypi ?= "bcmrpi_defconfig"
KBUILD_DEFCONFIG_raspberrypi2 ?= "bcm2709_defconfig"

# CMDLINE for raspberrypi
CMDLINE = "dwc_otg.lpm_enable=0 console=serial0,115200 root=/dev/mmcblk0p2 rootfstype=ext4 rootwait"

# Add the kernel debugger over console kernel command line option if enabled
CMDLINE_append = ' ${@base_conditional("ENABLE_KGDB", "1", "kgdboc=serial0,115200", "", d)}'

UDEV_GE_141 ?= "1"

# Set programmatically some variables during recipe parsing
# See http://www.yoctoproject.org/docs/current/bitbake-user-manual/bitbake-user-manual.html#anonymous-python-functions
python __anonymous () {
    kerneltype = d.getVar("KERNEL_IMAGETYPE", True)
    kerneldt = d.getVar("KERNEL_DEVICETREE", True)
    d.setVar("KERNEL_DEVICETREE", kerneldt)
}

do_install_prepend() {
    install -d ${D}/lib/firmware
}

do_bundle_initramfs_append() {
    if [ ! -z "${INITRAMFS_IMAGE}" -a x"${INITRAMFS_IMAGE_BUNDLE}" = x1 ]; then
        if test "x${KERNEL_IMAGETYPE}" != "xuImage" ; then
            if test -n "${KERNEL_DEVICETREE}"; then
                # Add RPi bootloader trailer to kernel image to enable DeviceTree support
                for type in ${KERNEL_IMAGETYPES} ; do
                    ${STAGING_BINDIR_NATIVE}/mkknlimg --dtok ${KERNEL_OUTPUT_DIR}/$type.initramfs ${KERNEL_OUTPUT_DIR}/$type.initramfs
                done
            fi
        fi
    fi
}

do_deploy_append() {
    # Symlinks to make boot image construction easier
    install -d ${DEPLOYDIR}/boot
    install -d ${DEPLOYDIR}/boot/overlays
    for i in ${KERNEL_DEVICE_BLOBS}; do
        ln -sf ../${KERNEL_IMAGETYPE}-${i} ${DEPLOYDIR}/boot/$i
    done
    for i in ${KERNEL_DEVICE_OVERLAYS}; do
        ovl=`basename $i`
        ln -sf ../../${KERNEL_IMAGETYPE}-${ovl} ${DEPLOYDIR}/boot/overlays/${ovl}
    done
}
