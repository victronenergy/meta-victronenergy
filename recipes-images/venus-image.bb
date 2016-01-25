DESCRIPTION = "Generic Victron image"

IMAGE_INSTALL = "packagegroup-core-boot packagegroup-base packagegroup-venus-base ${ROOTFS_PKGMANAGE_BOOTSTRAP} ${CORE_IMAGE_EXTRA_INSTALL}"
IMAGE_FEATURES += "package-management debug-tweaks ssh-server-openssh"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_ROOTFS_SIZE = "100000"

