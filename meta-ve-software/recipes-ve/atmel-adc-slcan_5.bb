SUMMARY = "Firmware for ADC/CAN microcontroller"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = "git://github.com/victronenergy/atmel-adc-and-slcan.git;branch=master;protocol=https;lfs=0"
SRCREV = "f4ad881e55eb7fe4f75da3b9787d0714d4db4d86"
S = "${WORKDIR}/git"

FIRMWARE_DIR = "/opt/victronenergy/firmware"
BLOB = "samc21_slcan_adc.bin"

do_install() {
    dest=${D}${FIRMWARE_DIR}

    install -m 0755 -d ${dest}
    install -m 0644 bin/${BLOB} ${dest}
}

FILES:${PN} = "${FIRMWARE_DIR}"
