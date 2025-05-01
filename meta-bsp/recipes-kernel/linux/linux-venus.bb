SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

RDEPENDS:${KERNEL_PACKAGE_NAME}-base += "kernel-devicetree"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ${KERNEL_CONFIG}"

LINUX_VERSION = "6.12.23"
LINUX_VERSION_VENUS = "2"
LINUX_VERSION_EXTENSION = "-venus-${LINUX_VERSION_VENUS}"

PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}"

GIT_BRANCH = "venus-${LINUX_VERSION}"
SRC_URI = "git://github.com/victronenergy/linux.git;protocol=https;branch=${GIT_BRANCH};tag=v${PV}"
VENUS_PATCHES = "file://0001-ARM-dts-bbb-venus-disable-dma-on-uart2.patch"
VENUS_PATCHES:rpi = ""
SRC_URI += "${VENUS_PATCHES}"

S = "${WORKDIR}/git"

do_configure:append() {
    sed -i 's/^\(CONFIG_LOCALVERSION=\).*/\1"${LINUX_VERSION_EXTENSION}"/' \
        .config
}

DEPENDS += "coreutils-native openssl-native"
HOST_EXTRACFLAGS += "-I${STAGING_INCDIR_NATIVE}"
