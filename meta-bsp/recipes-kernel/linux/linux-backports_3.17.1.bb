DESCRIPTION = "Linux Driver backports"
SECTION = "kernel"
LICENSE = "GPLv2"

DEPENDS += "coreutils-native"

inherit module
KERNEL_MODULES_META_PACKAGE = "linux-backports"
KERNEL_MODULE_PACKAGE_PREFIX  = "backport-"
BACKPORTS_CONFIG = "ccgx.config"

SRC_URI = " \
	http://www.kernel.org/pub/linux/kernel/projects/backports/stable/v${PV}/backports-${PV}-1.tar.xz \
	file://${BACKPORTS_CONFIG} \
"

SRC_URI[md5sum] = "a7658887f3247c2303f4a212cb441895"
SRC_URI[sha256sum] = "1b7640f3962fdbf6edafbdebbd5b6a0541999b48138478fccc694f01f12e55a1"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

KBUILD_OUTPUT ?= "${STAGING_KERNEL_DIR}"
S = "${WORKDIR}/backports-${PV}-1"
EXTRA_OEMAKE = "KLIB_BUILD=${KBUILD_OUTPUT}"

do_configure() {
	cp ${WORKDIR}/${BACKPORTS_CONFIG} ${S}/.config
	oe_runmake CC="${BUILD_CC}" -C ${S} oldconfig
}

do_install() {
	oe_runmake install INSTALL_MOD_PATH=${D}
}

python split_kernel_module_packages_append () {
    # complains about missing backport-kernel-module-eeprom-93cx6-3.7.1 without this
    d.setVar('RDEPENDS_backport-kernel-module-rtl8187-3.7.1', "")
}
