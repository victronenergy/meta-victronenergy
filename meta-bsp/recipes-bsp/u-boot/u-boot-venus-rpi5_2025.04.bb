HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
DESCRIPTION = "U-Boot, a boot loader for Embedded boards based on PowerPC, \
ARM, MIPS and several other processors, which can be installed in a boot \
ROM and used to initialize and test the hardware or to download and run \
application code."
SECTION = "bootloaders"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"
PE = "1"

DEPENDS += "\
    flex-native bison-native python3-setuptools-native \
    gnutls-native bc-native dtc-native python3-pyelftools-native \
"

# Use U-Boot 2025.04 that supports Raspberry Pi 5
# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "502120fe1cbadcb49dbafe15860ae14ce87287f0"
SRC_URI = "git://github.com/victronenergy/u-boot;protocol=https;branch=rpi_v2025.04"

S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

inherit pkgconfig

do_configure[cleandirs] = "${B}"

do_configure:append:raspberrypi5() {
    # Remove any existing CONFIG_BOOTDELAY= lines
    sed -i '/^CONFIG_BOOTDELAY=/d' "${B}/.config"
    # Disable U-Boot interrupt timeout to avoid
    # boot issues without a connected debug UART
    # This is a known U-Boot issue discussed in:
    # https://bugzilla.opensuse.org/show_bug.cgi?id=1251192
    # https://lists.denx.de/pipermail/u-boot/2025-January/576305.html
    echo "CONFIG_BOOTDELAY=-2" >> ${B}/.config
}

require ${COREBASE}/meta/recipes-bsp/u-boot/u-boot.inc
