DESCRIPTION = "Generic Victron image"

IMAGE_INSTALL = "packagegroup-core-boot packagegroup-base packagegroup-venus-base packagegroup-venus-machine ${ROOTFS_PKGMANAGE_BOOTSTRAP} ${CORE_IMAGE_EXTRA_INSTALL}"
IMAGE_INSTALL += "packagegroup-ve-console-apps"
IMAGE_FEATURES += "package-management debug-tweaks ssh-server-openssh"
IMAGE_FEATURES_append_beaglebone += " read-only-rootfs"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

IMAGE_ROOTFS_SIZE = "100000"

