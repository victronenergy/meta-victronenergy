DESCRIPTION = "U-Boot - the Universal Boot Loader"
HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"

PROVIDES = "virtual/bootloader"
DEPENDS = "mtd-utils"

PACKAGE_ARCH = "${MACHINE_ARCH}"
EXTRA_OEMAKE = "CROSS_COMPILE=${TARGET_PREFIX}"
UBOOT_MACHINE ?= "${MACHINE}_config"
UBOOT_IMAGE ?= "u-boot.img"
UBOOT_MAKE_TARGET ?= "all"

require u-boot.src.inc

do_configure () {
	oe_runmake ${UBOOT_MACHINE}
}

do_compile () {
	unset LDFLAGS
	unset CFLAGS
	unset CPPFLAGS
	oe_runmake ${UBOOT_MAKE_TARGET}
}

do_install () {
	install -d ${D}/boot
	install ${S}/${UBOOT_IMAGE} ${D}/boot/${MACHINE}-${UBOOT_IMAGE}
}

inherit deploy

addtask deploy before do_package after do_compile

do_deploy () {
	install -d ${DEPLOY_DIR_IMAGE}
	install ${S}/${UBOOT_IMAGE} ${DEPLOY_DIR_IMAGE}/${UBOOT_IMAGE}
	if [ -e ${S}/MLO ] ; then
		install ${S}/MLO ${DEPLOY_DIR_IMAGE}/MLO
	fi
}

FILES_${PN} = "/boot ${sysconfdir}"
# no gnu_hash in uboot.bin, by design, so skip QA
INSANE_SKIP_${PN} = "1"

