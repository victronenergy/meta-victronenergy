IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-base \
    packagegroup-venus-base \
    packagegroup-venus-machine \
    ${CORE_IMAGE_EXTRA_INSTALL} \
    ${KERNEL_PACKAGES} \
"
IMAGE_INSTALL += "packagegroup-ve-console-apps"
IMAGE_FEATURES += "package-management ssh-server-openssh"

IMAGE_LINGUAS = "en-us"

LICENSE = "MIT"

inherit core-image

IMAGE_NAME = "${IMAGE_BASENAME}-${MACHINE}-${DATETIME}-${DISTRO_VERSION}"
IMAGE_NAME[vardepsexclude] += "DATETIME"
