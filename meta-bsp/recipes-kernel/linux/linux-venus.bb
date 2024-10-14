SECTION = "kernel"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

RDEPENDS:${KERNEL_PACKAGE_NAME}-base += "kernel-devicetree"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ${KERNEL_CONFIG}"

LINUX_VERSION = "5.10.109"
LINUX_VERSION_VENUS = "17"
LINUX_VERSION_EXTENSION = "-venus-${LINUX_VERSION_VENUS}"

PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}"

GIT_BRANCH = "mans/tigro"

SRC_URI = "git://github.com/victronenergy/linux.git;protocol=https;branch=${GIT_BRANCH}"
SRCREV = "e0cb19f3f1597821498239aa7a33d1d741f4836d"
S = "${WORKDIR}/git"

python () {
    dtb = d.getVar('KERNEL_DEVICETREE').split()
    d.setVar('KERNEL_DEVICETREE', ' '.join(map(os.path.basename, dtb)))
}

do_configure:append() {
    sed -i 's/^\(CONFIG_LOCALVERSION=\).*/\1"${LINUX_VERSION_EXTENSION}"/' \
        .config
}

DEPENDS += "openssl-native"
HOST_EXTRACFLAGS += "-I${STAGING_INCDIR_NATIVE}"
