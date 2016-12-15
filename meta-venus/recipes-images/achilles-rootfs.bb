SUMMARY = "Root file system image"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58"
IMAGE_INSTALL = "packagegroup-custom-image-achilles ${ROOTFS_PKGMANAGE_BOOTSTRAP} ${CORE_IMAGE_EXTRA_INSTALL}"
IMAGE_FEATURES += "package-management"

inherit core-image
