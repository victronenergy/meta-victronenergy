# based on the standard linux.inc
# changed behavior of uImage creation - by default its replaced with non u-boot version
# fixed bug with device flattened tree creation
# added devicetree.dtb default link

DESCRIPTION = "Linux Kernel"
SECTION = "kernel"
LICENSE = "GPLv2"

LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

inherit kernel

DEPENDS_append = " u-boot-mkimage-native "

RPSRC = "http://www.rpsys.net/openzaurus/patches/archive"

# Support for device tree generation
FILES_kernel-devicetree = "/boot/devicetree*"
KERNEL_DEVICETREE_FLAGS = ""

python __anonymous () {
    devicetree = d.getVar("KERNEL_DEVICETREE", 1) or ''
    if devicetree:
        depends = d.getVar("DEPENDS", 1)
        d.setVar("DEPENDS", "%s dtc-native" % depends)
        packages = d.getVar("PACKAGES", 1)
        d.setVar("PACKAGES", "%s kernel-devicetree" % packages)
}

do_configure_prepend() {
        yes '' | oe_runmake -C ${S} O=${B} am3517_ccgx_defconfig
}

do_install_prepend() {
        if test ! -e arch/${ARCH}/boot/uImage ; then
            if test -e arch/${ARCH}/boot/Image ; then
               ln -f arch/${ARCH}/boot/Image arch/${ARCH}/boot/uImage
	    fi
	fi

        if test ! -e arch/${ARCH}/boot/uImage ; then
            if test -e arch/${ARCH}/boot/images/uImage ; then
                 ln -f arch/${ARCH}/boot/images/uImage arch/${ARCH}/boot/uImage
            fi
        fi

        if test -e arch/${ARCH}/kernel/vmlinux.lds ; then
             ln -f arch/${ARCH}/kernel/vmlinux.lds arch/${ARCH}/boot/vmlinux
        fi
}

do_install_append() {
    if test -n "${KERNEL_DEVICETREE}"; then
        dtc -I dts -O dtb ${KERNEL_DEVICETREE_FLAGS} -o devicetree ${KERNEL_DEVICETREE}
        install -m 0644 devicetree ${D}/boot/devicetree-${KERNEL_VERSION}
        ln -sf devicetree-${KERNEL_VERSION} ${D}/boot/devicetree
        install -d ${DEPLOY_DIR_IMAGE}
        install -m 0644 devicetree ${DEPLOY_DIR_IMAGE}/${KERNEL_IMAGE_BASE_NAME}.dtb
        cd ${DEPLOY_DIR_IMAGE}
        rm -f ${KERNEL_IMAGE_SYMLINK_NAME}.dtb
        ln -sf ${KERNEL_IMAGE_BASE_NAME}.dtb ${KERNEL_IMAGE_SYMLINK_NAME}.dtb
    fi

    if test -n "${KERNEL_DROPIMAGE}"; then
        rm -f ${D}/boot/uImage*
    fi
}
