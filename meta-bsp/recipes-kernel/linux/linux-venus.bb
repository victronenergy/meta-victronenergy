SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

inherit kernel

RDEPENDS_${KERNEL_PACKAGE_NAME}-base += "kernel-devicetree"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ${KERNEL_CONFIG}"

LINUX_VERSION = "5.10.33"
LINUX_VERSION_VENUS = "1"
LINUX_VERSION_EXTENSION = "-venus-${LINUX_VERSION_VENUS}"

PV = "${LINUX_VERSION}${LINUX_VERSION_EXTENSION}"
PR = "4"

SRC_URI = " \
    git://github.com/victronenergy/linux.git;protocol=https;branch=venus-${LINUX_VERSION};tag=v${PV} \
    file://0001-Kconfig-venus-select-the-mediatek-wifi-modules.patch \
    file://0002-Bluetooth-btbcm-add-default-address-for-BCM43430A1.patch \
    file://0003-ARM-dts-ccgx-fix-mmc-cd-gpio.patch \
    file://0004-Bluetooth-Fix-default-values-for-advertising-interva.patch \
    file://0005-i2c-mv64xxx-Add-bus-error-recovery.patch \
    file://0006-i2c-mv64xxx-set-retries-to-5.patch \
    file://0007-backlight-victron-gxdisp-use-regulator-from-devicetr.patch \
    file://0008-ARM-dts-ccgx2-add-recovery-info-for-i2c2.patch \
    file://0009-ARM-dts-ccgx2-add-regulator-for-devices-on-hdmi-i2c-.patch \
"
S = "${WORKDIR}/git"

do_configure_append() {
    sed -i 's/^\(CONFIG_LOCALVERSION=\).*/\1"${LINUX_VERSION_EXTENSION}"/' \
        .config
}

DEPENDS += "openssl-native"
HOST_EXTRACFLAGS += "-I${STAGING_INCDIR_NATIVE}"
