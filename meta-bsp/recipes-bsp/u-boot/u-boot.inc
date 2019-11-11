DESCRIPTION = "U-Boot - the Universal Boot Loader"
HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"

PROVIDES = "virtual/bootloader"
DEPENDS = "mtd-utils"

PACKAGE_ARCH = "${MACHINE_ARCH}"
# gcc 4.9.2 has a BUG cause false array out bound warnings.. (which is in fido)
EXTRA_OEMAKE = 'CROSS_COMPILE=${TARGET_PREFIX} CC="${TARGET_PREFIX}gcc ${TOOLCHAIN_OPTIONS} -Wno-error=array-bounds"'
UBOOT_MACHINE ?= "${MACHINE}_config"
UBOOT_IMAGE ?= "u-boot.img"
UBOOT_MAKE_TARGET ?= "all"

do_configure () {
    oe_runmake -C ${S} ${UBOOT_MACHINE}
}

do_compile () {
    unset LDFLAGS
    unset CFLAGS
    unset CPPFLAGS
    oe_runmake -C ${S} ${UBOOT_MAKE_TARGET}
}

do_install () {
    install -d ${D}/boot
    install ${S}/${UBOOT_IMAGE} ${D}/boot/${MACHINE}-${UBOOT_IMAGE}
}

inherit deploy

addtask deploy before do_package after do_compile

do_deploy () {
    install -d ${DEPLOYDIR}
    install ${S}/${UBOOT_IMAGE} ${DEPLOYDIR}/${UBOOT_IMAGE}
    if [ -e ${S}/MLO ] ; then
        install ${S}/MLO ${DEPLOYDIR}/MLO
    fi
}

FILES_${PN} = "/boot ${sysconfdir}"
# no gnu_hash in uboot.bin, by design, so skip QA
INSANE_SKIP_${PN} = "1"

