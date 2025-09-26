SUMMARY = "Bring Wi-Fi up on Raspberry Pi"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit daemontools

DAEMONTOOLS_RUN = "${bindir}/wlan0-up"

SRC_URI = "file://wlan0-up"
S = "${S_UNUSED}"

do_install() {
    install -d ${D}${bindir}
    install -m 755 ${UNPACKDIR}/wlan0-up ${D}${bindir}
}

COMPATIBLE_MACHINE = "^rpi$"
