SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

FILESEXTRAPATHS:prepend := "${THISDIR}/linux-venus:"

inherit kernel

RDEPENDS:${KERNEL_PACKAGE_NAME}-base += "kernel-devicetree"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ${KERNEL_CONFIG}"

LINUX_VERSION = "5.10.109"
LINUX_VERSION_VENUS = "16"
LINUX_VERSION_EXTENSION = "-venus-${LINUX_VERSION_VENUS}"

PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}"

GIT_BRANCH = "venus-${LINUX_VERSION}"

SRC_URI = "git://github.com/victronenergy/linux.git;protocol=https;branch=${GIT_BRANCH};tag=v${PV}"
SRC_URI += "\
    file://0001-kconfig-venus-enable-netfilter-modules-for-tailscale.patch \
"
S = "${WORKDIR}/git"

do_configure:append() {
    sed -i 's/^\(CONFIG_LOCALVERSION=\).*/\1"${LINUX_VERSION_EXTENSION}"/' \
        .config
}

DEPENDS += "openssl-native"
HOST_EXTRACFLAGS += "-I${STAGING_INCDIR_NATIVE}"
