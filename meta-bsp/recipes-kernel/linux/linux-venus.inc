SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

RDEPENDS:${KERNEL_PACKAGE_NAME}-base += "kernel-devicetree"
RDEPENDS:${KERNEL_PACKAGE_NAME}-base:ccgx = ""

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ${KERNEL_CONFIG}"

S = "${WORKDIR}/git"

DEPENDS += "coreutils-native openssl-native"
HOST_EXTRACFLAGS += "-I${STAGING_INCDIR_NATIVE}"
