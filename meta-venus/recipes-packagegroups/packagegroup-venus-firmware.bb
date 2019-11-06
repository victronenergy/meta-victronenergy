SUMMARY = "Firmware packages for built-in peripherals"
LICENSE = "MIT"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS_${PN} = "\
    venus-firmware-update \
"

RDEPENDS_${PN}_append_einstein = "\
    atmel-adc-slcan \
"
