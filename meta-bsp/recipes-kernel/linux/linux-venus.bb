SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

RDEPENDS:${KERNEL_PACKAGE_NAME}-base += "kernel-devicetree"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ${KERNEL_CONFIG}"

LINUX_VERSION = "5.10.109"
LINUX_VERSION_VENUS = "20"
LINUX_VERSION_EXTENSION = "-venus-${LINUX_VERSION_VENUS}"

PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}"

GIT_BRANCH = "venus-${LINUX_VERSION}"

VENUS_PATCHES ?= " \
	file://0001-configs-sunxi_victron-make-spi-a-module.patch \
	file://0002-can-dev-better-handle-do_set_mode-dev-CAN_MODE_START.patch \
	file://0003-can-mcp251xfd-retry-later-if-spi-communication-faile.patch \
	file://0008-mcp251xfd-only-print-CRC-warnings-when-enabled.patch \
"
SRC_URI = "git://github.com/victronenergy/linux.git;protocol=https;branch=${GIT_BRANCH};tag=v${PV}"
SRC_URI += "${VENUS_PATCHES}"
S = "${WORKDIR}/git"

do_configure:append() {
    sed -i 's/^\(CONFIG_LOCALVERSION=\).*/\1"${LINUX_VERSION_EXTENSION}"/' \
        .config
}

DEPENDS += "openssl-native"
HOST_EXTRACFLAGS += "-I${STAGING_INCDIR_NATIVE}"
