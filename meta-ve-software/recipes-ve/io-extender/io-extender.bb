DESCRIPTION = "Support scripts for digital IO extender"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

SRC_URI = "\
    file://io-extender \
    file://io-extender.rules \
"

do_install() {
    install -m 755 -d ${D}${base_libdir}/udev
    install -m 755 ${UNPACKDIR}/io-extender ${D}${base_libdir}/udev

    install -m 755 -d ${D}${sysconfdir}/udev/rules.d
    install -m 0644 ${UNPACKDIR}/io-extender.rules ${D}/${sysconfdir}/udev/rules.d
}
