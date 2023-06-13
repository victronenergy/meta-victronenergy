python () {
    dn = os.path.dirname(d.getVar('FILE'))
    pn = d.getVar('PN')
    pl = pn.split('-')
    pv = pl.pop()
    pb = '-'.join(pl)
    ps = [pl.pop()] if len(pl) > 2 else []

    fp = [os.path.join(dn, 'venus-swu', pv), os.path.join(dn, 'venus-swu')]
    d.appendVar('FILESEXTRAPATHS', ':'.join(fp))

    if pv == d.getVar('SWU_VERSION'):
        d.appendVar('PROVIDES', pb)

    if pv != '1':
        pl.append(pv)

    d.setVar('IMAGE_BASENAME', '-'.join(pl + ps))
}

SRC_URI = "file://sw-description"

SRC_URI:append:canvu500 = " file://imx-kobs-update.sh"

inherit swupdate

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PACKAGE_ARCH = "${MACHINE_ARCH}"

VENUS_IMAGE = "venus-image"
VENUS_IMAGE_FILE = "${VENUS_IMAGE}-${MACHINE}.${ROOT_FSTYPE}"
VENUS_VERSION = "${BUILDNAME} ${DISTRO_VERSION}"

BOOT_IMAGE = ""
BOOT_IMAGE:beaglebone = "venus-boot-image"

BOOT_IMAGE_FILE = \
    "${@'${BOOT_IMAGE}-${MACHINE}.${BOOT_FSTYPE}' if '${BOOT_IMAGE}' else ''}"

# IMAGE_DEPENDS: list of images that contains a root filesystem
# it will be ensured they are built before creating swupdate image
IMAGE_DEPENDS = "${VENUS_IMAGE} ${BOOT_IMAGE}"

do_swuimage[depends] += "virtual/bootloader:do_deploy"

ROOT_FSTYPE = "ubifs"
ROOT_FSTYPE:beaglebone = "ext4.gz"
ROOT_FSTYPE:rpi = "ext4.gz"
ROOT_FSTYPE:sunxi = "ext4.gz"

BOOT_FSTYPE = "vfat.gz"

IMAGE_NAME = "${IMAGE_BASENAME}-${MACHINE}-${BUILDNAME}-${DISTRO_VERSION}"

# SWUPDATE_IMAGES: list of images that will be part of the compound image
# the list can have any binaries - images must be in the DEPLOY directory
SWUPDATE_IMAGES = "${VENUS_IMAGE_FILE} ${BOOT_IMAGE_FILE}"
SWUPDATE_IMAGES:append:canvu500 = " u-boot.imx splash.bmp.gz"
SWUPDATE_IMAGES:append:ccgx = " uImage u-boot.img MLO splash.bgra"
SWUPDATE_IMAGES:append:sunxi = " u-boot-sunxi-with-spl.bin"

python () {
    for n in d.getVar('SWUPDATE_IMAGES').split():
        d.setVarFlag('SWUPDATE_IMAGES_NOAPPEND_MACHINE', n, '1')
}

DEPENDS += "dtc-native"

def set_compatible_machines(d):
    import subprocess

    deploy_dir = d.getVar("DEPLOY_DIR_IMAGE")
    boards = set()
    for dtb in d.getVar("KERNEL_DEVICETREE").split():
        compatible = subprocess.check_output(["fdtget", os.path.join(deploy_dir, dtb), "/", "compatible"]).decode("utf-8")
        board = compatible.split()[0]
        boards.add('"' + board + '"')
    d.setVar("HARDWARE_COMPATIBLE", ", ".join(sorted(boards)))

python do_subst() {
    import fileinput
    import re
    import sys

    set_compatible_machines(d)

    swd = os.path.join(d.getVar('WORKDIR'), 'sw-description')
    f = fileinput.input(files=(swd), inplace=True)

    for line in f:
        line, n = re.subn(r'(\s*\w+)(\s*[=:])~\s*', r'\1\2 ', line)
        if n:
            line = re.sub(r'\$\{(\w+)\}', lambda m: d.getVar(m.group(1)), line)
        sys.stdout.write(line)

    f.close()
}

addtask do_subst after do_unpack before do_swuimage
do_subst[depends] += "virtual/kernel:do_deploy"

do_unpack[nostamp] = "1"
do_subst[nostamp] = "1"

do_swuimage[vardepsexclude] += "IMAGE_NAME"
