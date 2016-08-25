SRC_URI = "file://sw-description"

SWUPDATE = "base"
SWUPDATE_bpp3 += "swupdate"
SWUPDATE_ccgx += "swupdate"

inherit ${SWUPDATE}

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

# IMAGE_DEPENDS: list of images that contains a root filesystem
# it will be ensured they are built before creating swupdate image
IMAGE_DEPENDS = "venus-image"

# SWUPDATE_IMAGES: list of images that will be part of the compound image
# the list can have any binaries - images must be in the DEPLOY directory
SWUPDATE_IMAGES = "venus-image-${MACHINE}.ubifs uImage u-boot.img MLO splash.bgra"
