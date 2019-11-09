require u-boot-rpi.inc

DEPENDS += "dtc-native"
do_deploy[depends] += "bcm2835-bootfiles:do_deploy"

SRC_URI = "git://github.com/victronenergy/u-boot.git;branch=master"
SRC_URI += " \
    file://uEnv.txt \
    file://config.txt \
"

S = "${WORKDIR}/git"

COMPATIBLE_MACHINE = "raspberrypi"

# This revision corresponds to the tag "v2017.09"
# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "c98ac3487e413c71e5d36322ef3324b21c6f60f9"

# Install required file for Raspberry Pi bootloader, to indicate that it should
# load u-boot.
do_deploy_append() {
    install ${WORKDIR}/config.txt ${DEPLOYDIR}/config.txt
    ${B}/tools/mkenvimage -s 16384 -o ${DEPLOYDIR}/uboot.env ${WORKDIR}/uEnv.txt

    # Keep a version file, to enable future updates
    echo "${PV}" > ${DEPLOYDIR}/u-boot-version.txt

    # Also deploy a symlink to make it easier to build a boot image later.
    install -d ${DEPLOYDIR}/boot
    ln -sf ../config.txt ${DEPLOYDIR}/boot
    ln -sf ../u-boot.bin ${DEPLOYDIR}/boot
    ln -sf ../uEnv.txt ${DEPLOYDIR}/boot
    ln -sf ../uboot.env ${DEPLOYDIR}/boot
    ln -sf ../u-boot-version.txt ${DEPLOYDIR}/boot
}
