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

MMC_TOOLS = "e2fsprogs-mke2fs util-linux-sfdisk"
UBI_TOOLS = "mtd-utils-ubifs"

python () {
    fs = d.getVar('FSTAB') or 'mmc'

    if fs.startswith('mmc'):
        d.appendVar('IMAGE_INSTALL', ' ${MMC_TOOLS}')
    elif fs == 'ubi':
        d.appendVar('IMAGE_INSTALL', ' ${UBI_TOOLS}')
    else:
        raise Exception('Unknown rootfs device type')

    import re

    path = bb.utils.which(d.getVar('FILESPATH'), 'swupdate')
    with open(path) as f:
        for s in f:
            m = re.search(r'^\s*MODULES="(.*?)"', s)
            if m is not None:
                for mod in m.group(1).split():
                    if re.fullmatch(r'[a-z0-9-]+', mod):
                        d.appendVar('IMAGE_INSTALL', ' kernel-module-' + mod)
}

IMAGE_INSTALL:append:canvu500 = "\
    imx-kobs \
"

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
    install ${UNPACKDIR}/install.sh ${IMAGE_ROOTFS}/${base_libdir}/venus

    if [ "${SWUPDATE_INIT}" = "tiny" ]; then
        install ${UNPACKDIR}/swupdate ${IMAGE_ROOTFS}/${base_sbindir}/init
    else
        cp ${UNPACKDIR}/swupdate ${IMAGE_ROOTFS}${sysconfdir}/init.d
    fi
}

addtask add_files after do_rootfs before do_image
