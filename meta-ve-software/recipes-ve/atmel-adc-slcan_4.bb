SUMMARY = "Firmware for ADC/CAN microcontroller"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

SRC_URI = "git://github.com/victronenergy/atmel-adc-and-slcan.git;protocol=https;tag=v${PV}"
S = "${WORKDIR}/git"

FIRMWARE_DIR = "/opt/victronenergy/firmware"
BLOB = "samc21_slcan_adc.bin"

do_install() {
    dest=${D}${FIRMWARE_DIR}

    install -m 0755 -d ${dest}
    install -m 0644 bin/${BLOB} ${dest}
}

FILES_${PN} = "${FIRMWARE_DIR}"
