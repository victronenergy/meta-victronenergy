DESCRIPTION = "Generic Victron image"

IMAGE_INSTALL = "packagegroup-core-boot packagegroup-venus-base ${ROOTFS_PKGMANAGE_BOOTSTRAP} ${CORE_IMAGE_EXTRA_INSTALL}"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_ROOTFS_SIZE = "100000"

