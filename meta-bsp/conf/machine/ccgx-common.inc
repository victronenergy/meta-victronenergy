# variable needed for compilation
MACHINE_FEATURES ?= "ext2 keyboard screen serial usbgadget usbhost"

# Inputs for machine-conf-runtime recipe
VE_MKX_PORT = "/dev/ttyO1"
VE_VEDIRECT_PORTS = "/dev/ttyO2"
VE_VEDIRECT_AND_CONSOLE_PORT = "/dev/ttyO0"
VE_RELAYS = "/sys/class/gpio/gpio182"
VE_BUZZER = "/sys/class/gpio/gpio35"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-ccgx"
PREFERRED_PROVIDER_virtual/bootloader = "u-boot-ccgx"
PREFERRED_VERSION_u-boot-fw-utils = "2013.01.01-ccgx-v3"

# config of u-boot to be used
UBOOT_MACHINE = "bpp3_config"
UBOOT_ENTRYPOINT = "0x80008000"
UBOOT_LOADADDRESS = "0x80008000"

# generate uImage variant of linux kernel
KERNEL_IMAGETYPE = "uImage"

# UBI image
UBI_VOLNAME="rootfs"
MKUBIFS_ARGS = "-m 2048 -e 129024 -c 2388"
UBINIZE_ARGS = "-m 2048 -p 128KiB -s 512 -O 512"