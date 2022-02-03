SUMMARY = "Firmware packages for built-in peripherals"
LICENSE = "MIT"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} = "\
    venus-firmware-update \
"

RDEPENDS:${PN}:append:einstein = "\
    atmel-adc-slcan \
"
