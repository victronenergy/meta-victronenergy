SOC_FAMILY = "rpi"
include conf/machine/include/soc-family.inc

WKS_FILE ?= "sdimage-raspberrypi.wks"

# Inputs for machine-conf-runtime recipe
MACHINE_FEATURES += "headless"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-raspberrypi"

RPI_KERNEL_DEVICETREE_OVERLAYS = " \
    overlays/overlay_map.dtb \
    overlays/at86rf233.dtbo \
    overlays/disable-bt.dtbo \
    overlays/disable-wifi.dtbo \
    overlays/dwc2.dtbo \
    overlays/gpio-ir.dtbo \
    overlays/gpio-ir-tx.dtbo \
    overlays/gpio-key.dtbo \
    overlays/gpio-poweroff.dtbo \
    overlays/gpio-shutdown.dtbo \
    overlays/hifiberry-amp.dtbo \
    overlays/hifiberry-amp100.dtbo \
    overlays/hifiberry-amp3.dtbo \
    overlays/hifiberry-amp4pro.dtbo \
    overlays/hifiberry-dac.dtbo \
    overlays/hifiberry-dacplus.dtbo \
    overlays/hifiberry-dacplusadc.dtbo \
    overlays/hifiberry-dacplusadcpro.dtbo \
    overlays/hifiberry-dacplusdsp.dtbo \
    overlays/hifiberry-dacplushd.dtbo \
    overlays/hifiberry-digi-pro.dtbo \
    overlays/hifiberry-digi.dtbo \
    overlays/justboom-both.dtbo \
    overlays/justboom-dac.dtbo \
    overlays/justboom-digi.dtbo \
    overlays/i2c-gpio.dtbo \
    overlays/i2c-rtc.dtbo \
    overlays/imx219.dtbo \
    overlays/imx477.dtbo \
    overlays/imx708.dtbo \
    overlays/iqaudio-dac.dtbo \
    overlays/iqaudio-dacplus.dtbo \
    overlays/mcp2515-can0.dtbo \
    overlays/mcp2515-can1.dtbo \
    overlays/mcp3008.dtbo \
    overlays/miniuart-bt.dtbo \
    overlays/pitft22.dtbo \
    overlays/pitft28-capacitive.dtbo \
    overlays/pitft28-resistive.dtbo \
    overlays/pitft35-resistive.dtbo \
    overlays/pps-gpio.dtbo \
    overlays/rpi-ft5406.dtbo \
    overlays/rpi-poe.dtbo \
    overlays/uart0.dtbo \
    overlays/uart1.dtbo \
    overlays/uart2.dtbo \
    overlays/uart3.dtbo \
    overlays/uart4.dtbo \
    overlays/uart5.dtbo \
    overlays/vc4-fkms-v3d.dtbo \
    overlays/vc4-fkms-v3d-pi4.dtbo \
    overlays/vc4-kms-dsi-waveshare-panel.dtbo \
    overlays/vc4-kms-v3d.dtbo \
    overlays/vc4-kms-v3d-pi4.dtbo \
    overlays/vc4-kms-dsi-7inch.dtbo \
    overlays/w1-gpio.dtbo \
    overlays/w1-gpio-pullup.dtbo \
    overlays/wm8960-soundcard.dtbo \
"

RPI_KERNEL_DEVICETREE_OVERLAYS:append:raspberrypi5 = " \
    overlays/disable-bt-pi5.dtbo \
    overlays/disable-wifi-pi5.dtbo \
    overlays/uart0-pi5.dtbo \
    overlays/uart1-pi5.dtbo \
    overlays/uart2-pi5.dtbo \
    overlays/uart3-pi5.dtbo \
    overlays/uart4-pi5.dtbo \
    overlays/vc4-kms-v3d-pi5.dtbo \
"

KERNEL_PACKAGES ?= "kernel-image kernel-modules"
KERNEL_IMAGETYPE ?= "zImage"
RPI_KERNEL_DEVICETREE ?= " \
    ${RPI_KERNEL_DEVICETREE_DTB} \
    ${RPI_KERNEL_DEVICETREE_OVERLAYS} \
"

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
    ${RPI_KERNEL_DEVICETREE_OVERLAYS} \
    ${@' '.join([os.path.basename(dtb) for dtb in d.getVar("RPI_KERNEL_DEVICETREE_DTB").split()])} \
    ${UBOOT_BINARY} \
"

do_image_wic[depends] += " \
    bcm2835-bootfiles:do_deploy \
    virtual/bootloader:do_deploy \
"

do_image_wic[recrdeps] = "do_deploy"

# The kernel image is installed into the FAT32 boot partition and does not need
# to also be installed into the rootfs.
RDEPENDS:${KERNEL_PACKAGE_NAME}-base = ""
