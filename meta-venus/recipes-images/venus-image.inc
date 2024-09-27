IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-base \
    packagegroup-venus-base \
    packagegroup-venus-machine \
    venus-version \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    ${KERNEL_PACKAGES} \
"
IMAGE_INSTALL += "packagegroup-ve-console-apps"
IMAGE_FEATURES += "package-management ssh-server-openssh"
IMAGE_FEATURES += "read-only-rootfs"

BAD_RECOMMENDATIONS += "${@'' if d.getVar('VENUS_GPU') == '1' else 'mesa-megadriver'}"

IMAGE_LINGUAS = "en-us"

LICENSE = "MIT"

inherit core-image

IMAGE_NAME = "${IMAGE_BASENAME}-${MACHINE}-${DATETIME}-${DISTRO_VERSION}"
IMAGE_NAME[vardepsexclude] += "DATETIME"

python () {
    pn = d.getVar('PN').split('-', 2)
    img_type = pn[2] if len(pn) > 2 else 'normal'
    d.setVar('VENUS_IMAGE_TYPE', img_type)
}

fakeroot do_image_type() {
    echo "${VENUS_IMAGE_TYPE}" >${IMAGE_ROOTFS}/etc/venus/image-type
}
addtask do_image_type after do_rootfs before do_image
