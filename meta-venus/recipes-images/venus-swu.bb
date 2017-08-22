SRC_URI = "file://sw-description"

SRC_URI_append_canvu500 += "file://imx-kobs-update.sh"

inherit swupdate

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

# IMAGE_DEPENDS: list of images that contains a root filesystem
# it will be ensured they are built before creating swupdate image
IMAGE_DEPENDS = "venus-image"
IMAGE_DEPENDS_append_beaglebone = " venus-boot-image"
IMAGE_DEPENDS_append_raspberrypi2 = " venus-boot-image"

do_swuimage[depends] += "virtual/bootloader:do_deploy"

ROOT_FSTYPE = "ubifs"
ROOT_FSTYPE_beaglebone = "ext4.gz"
ROOT_FSTYPE_raspberrypi2 = "ext4.gz"

BOOT_FSTYPE = "vfat.gz"

IMAGE_NAME = "${IMAGE_BASENAME}-${MACHINE}-${BUILDNAME}-${DISTRO_VERSION}"

# SWUPDATE_IMAGES: list of images that will be part of the compound image
# the list can have any binaries - images must be in the DEPLOY directory
SWUPDATE_IMAGES = "${IMAGE_DEPENDS}"
SWUPDATE_IMAGES_append_canvu500 = " u-boot.imx"
SWUPDATE_IMAGES_append_ccgx = " uImage u-boot.img MLO splash.bgra"

SWUPDATE_IMAGES_FSTYPES[venus-image] = ".${ROOT_FSTYPE}"
SWUPDATE_IMAGES_FSTYPES[venus-boot-image] = ".${BOOT_FSTYPE}"

SWUPDATE_IMAGES_NOAPPEND_MACHINE[uImage] = "1"
SWUPDATE_IMAGES_NOAPPEND_MACHINE[u-boot.img] = "1"
SWUPDATE_IMAGES_NOAPPEND_MACHINE[u-boot.imx] = "1"
SWUPDATE_IMAGES_NOAPPEND_MACHINE[MLO] = "1"
SWUPDATE_IMAGES_NOAPPEND_MACHINE[splash.bgra] = "1"

do_version() {
    sed -e "s/venus-version = .*;/venus-version = \"${BUILDNAME} ${DISTRO_VERSION}\";/" \
        -i ${WORKDIR}/sw-description
}

addtask do_version after do_unpack before do_swuimage

do_version[depends] += "venus-image:do_rootfs"
do_version[vardeps] += "DATETIME"
do_version[nostamp] = "1"
