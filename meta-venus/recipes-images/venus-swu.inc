python () {
    dn = os.path.dirname(d.getVar('FILE'))
    pn = d.getVar('PN')
    pv = pn.split('-')[-1]

    fp = [os.path.join(dn, 'venus-swu', pv), os.path.join(dn, 'venus-swu')]
    d.appendVar('FILESEXTRAPATHS', ':'.join(fp))

    if pv == d.getVar('SWU_VERSION'):
       d.appendVar('PROVIDES', 'venus-swu')
}

SRC_URI = "file://sw-description"

SRC_URI_append_canvu500 += "file://imx-kobs-update.sh"

inherit swupdate

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

VENUS_IMAGE = "venus-image"
VENUS_IMAGE_FILE = "${VENUS_IMAGE}-${MACHINE}.${ROOT_FSTYPE}"
VENUS_VERSION = "${BUILDNAME} ${DISTRO_VERSION}"

BOOT_IMAGE = ""
BOOT_IMAGE_beaglebone = "venus-boot-image"

BOOT_IMAGE_FILE = \
    "${@'${BOOT_IMAGE}-${MACHINE}.${BOOT_FSTYPE}' if '${BOOT_IMAGE}' else ''}"

# IMAGE_DEPENDS: list of images that contains a root filesystem
# it will be ensured they are built before creating swupdate image
IMAGE_DEPENDS = "${VENUS_IMAGE} ${BOOT_IMAGE}"

do_swuimage[depends] += "virtual/bootloader:do_deploy"

ROOT_FSTYPE = "ubifs"
ROOT_FSTYPE_beaglebone = "ext4.gz"
ROOT_FSTYPE_rpi = "ext4.gz"
ROOT_FSTYPE_sunxi = "ext4.gz"

BOOT_FSTYPE = "vfat.gz"

IMAGE_NAME = "${IMAGE_BASENAME}-${MACHINE}-${BUILDNAME}-${DISTRO_VERSION}"

# SWUPDATE_IMAGES: list of images that will be part of the compound image
# the list can have any binaries - images must be in the DEPLOY directory
SWUPDATE_IMAGES = "${VENUS_IMAGE_FILE} ${BOOT_IMAGE_FILE}"
SWUPDATE_IMAGES_append_canvu500 = " u-boot.imx splash.bmp.gz"
SWUPDATE_IMAGES_append_ccgx = " uImage u-boot.img MLO splash.bgra"
SWUPDATE_IMAGES_append_sunxi = " u-boot-sunxi-with-spl.bin"

python () {
    for n in d.getVar('SWUPDATE_IMAGES').split():
        d.setVarFlag('SWUPDATE_IMAGES_NOAPPEND_MACHINE', n, '1')
}

python do_subst() {
    import fileinput
    import re
    import sys

    swd = os.path.join(d.getVar('WORKDIR'), 'sw-description')
    f = fileinput.input(files=(swd), inplace=True)

    for line in f:
        line, n = re.subn(r'(\s*\w+)\s*=~\s*', r'\1 = ', line)
        if n:
            line = re.sub(r'\$\{(\w+)\}', lambda m: d.getVar(m.group(1)), line)
        sys.stdout.write(line)

    f.close()
}

addtask do_subst after do_unpack before do_swuimage

do_unpack[nostamp] = "1"
do_subst[nostamp] = "1"

do_swuimage[vardepsexclude] += "IMAGE_NAME"
