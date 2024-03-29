SUMMARY = "rootfs to flash an image"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SWUPDATE_INIT ?= "tiny"

IMAGE_INSTALL = " \
    base-files \
    base-passwd \
    bash \
    busybox \
    swupdate \
    u-boot-fw-utils \
    venus-version \
    ${@bb.utils.contains('SWUPDATE_INIT', 'tiny', '', 'initscripts sysvinit tinylogin', d)} \
"

IMAGE_INSTALL:append:beaglebone = "e2fsprogs-mke2fs util-linux-sfdisk"
IMAGE_INSTALL:append:canvu500 = "imx-kobs mtd-utils-ubifs kernel-module-usbserial kernel-module-ftdi-sio"
IMAGE_INSTALL:append:ccgx = "mtd-utils-ubifs"
IMAGE_INSTALL:append:einstein = "\
    kernel-module-sun4i-backend \
    kernel-module-sun4i-drm \
    kernel-module-sun4i-drm-hdmi \
    kernel-module-victron-gxdisp-bl \
"
IMAGE_INSTALL:append:nanopi = "\
    kernel-module-sun4i-drm \
    kernel-module-sun8i-drm-hdmi \
    kernel-module-sun8i-mixer \
    kernel-module-victron-gxdisp-bl \
"
IMAGE_INSTALL:append:sunxi = "e2fsprogs-mke2fs util-linux-sfdisk"

IMAGE_LINGUAS = " "

IMAGE_FSTYPES = "cpio.gz.u-boot"

inherit image

python __anonymous() {
    d.delVarFlag('do_unpack', 'noexec')
}

addtask unpack before do_add_files

SRC_URI = "\
    file://install.sh \
    file://swupdate \
"

do_add_files() {
    install -d ${IMAGE_ROOTFS}/${base_libdir}/venus
    install ${WORKDIR}/install.sh ${IMAGE_ROOTFS}/${base_libdir}/venus

    if [ "${SWUPDATE_INIT}" = "tiny" ]; then
        install ${WORKDIR}/swupdate ${IMAGE_ROOTFS}/${base_sbindir}/init
    else
        cp ${WORKDIR}/swupdate ${IMAGE_ROOTFS}${sysconfdir}/init.d
    fi
}

addtask add_files after do_rootfs before do_image
