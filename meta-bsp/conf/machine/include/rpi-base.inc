SOC_FAMILY = "rpi"
include conf/machine/include/soc-family.inc

WKS_FILE ?= "sdimage-raspberrypi.wks"

# Inputs for machine-conf-runtime recipe
MACHINE_FEATURES += "headless"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-raspberrypi"

RPI_KERNEL_DEVICETREE_OVERLAYS = " \
    overlays/ads7846.dtbo \
    overlays/disable-bt.dtbo \
    overlays/disable-wifi.dtbo \
    overlays/gpio-fan.dtbo \
    overlays/pitft28-capacitive.dtbo \
    overlays/pitft35-resistive.dtbo \
    overlays/rpi-display.dtbo \
    overlays/mcp2515-can0.dtbo \
    overlays/mcp2515-can1.dtbo \
    overlays/mcp251xfd.dtbo \
    overlays/rpi-backlight.dtbo \
    overlays/rpi-ft5406.dtbo \
    overlays/sc16is752-spi1.dtbo \
    overlays/spi1-3cs.dtbo \
    overlays/waveshare-can-fd-hat-mode-a.dtbo \
    overlays/waveshare-can-fd-hat-mode-b.dtbo \
"

KERNEL_PACKAGES ?= "kernel-image kernel-modules"
KERNEL_IMAGETYPE ?= "zImage"
RPI_KERNEL_DEVICETREE ?= " \
    ${RPI_KERNEL_DEVICETREE_DTB} \
    ${RPI_KERNEL_DEVICETREE_OVERLAYS} \
"

# swupdate uses this to figure out which boards are compatible
KERNEL_DEVICETREE = "${RPI_KERNEL_DEVICETREE_DTB}"

PREFERRED_PROVIDER_virtual/bootloader ?= "u-boot-venus"
PREFERRED_VERSION_u-boot-fw-utils ?= "venus"
UBOOT_SRC_URI ?= "git://github.com/victronenergy/u-boot.git;branch=rpi_v2022.01;tag=37cacbb7c8ecf8f620d75d957ad3640862f5bfd3;protocol=https"
UBOOT_SUFFIX ?= "bin"
UBOOT_BINARY ?= "u-boot.${UBOOT_SUFFIX}"

IMAGE_FSTYPES ?= "wic ext4.gz"

MACHINE_FEATURES += "apm usbhost keyboard vfat ext2 screen touchscreen alsa bluetooth wifi sdio ${@bb.utils.contains('DISABLE_VC4GRAPHICS', '1', '', 'vc4graphics', d)}"

# Raspberry Pi has no hardware clock
MACHINE_FEATURES_BACKFILL_CONSIDERED = "rtc"

SWU_VERSION = "3"

IMAGE_BOOT_FILES ?= " \
    bcm2835-bootfiles/* \
    config.txt \
    cmdline.txt \
    ${RPI_KERNEL_DEVICETREE} \
    ${UBOOT_BINARY} \
"

do_image_wic[depends] += " \
    bcm2835-bootfiles:do_deploy \
    virtual/bootloader:do_deploy \
"

do_image_wic[recrdeps] = "do_deploy"
