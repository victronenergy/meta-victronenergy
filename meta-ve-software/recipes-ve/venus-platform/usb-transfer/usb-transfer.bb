DESCRIPTION = "USB transfer scripts (export/import Venus OS config to USB)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

RDEPENDS:${PN} += "python3-core python3-lxml"

# TODO: Is this needed?
SRC_URI = "\
    file://functions.sh \
    file://lcddriver.py \
    file://notify.py \
    file://post-hook.sh \
    file://usb-transfer.py \
"

do_install:append() {
    install -d ${D}/opt/victronenergy/venus-platform/usb-transfer/rc
    install -m 0755 ${UNPACKDIR}/functions.sh     ${D}/opt/victronenergy/venus-platform/usb-transfer/rc/functions.sh
    install -m 0755 ${UNPACKDIR}/lcddriver.py     ${D}/opt/victronenergy/venus-platform/usb-transfer/rc/lcddriver.py
    install -m 0755 ${UNPACKDIR}/notify.py        ${D}/opt/victronenergy/venus-platform/usb-transfer/rc/notify.py
    install -m 0755 ${UNPACKDIR}/post-hook.sh     ${D}/opt/victronenergy/venus-platform/usb-transfer/rc/post-hook.sh
    install -m 0755 ${UNPACKDIR}/usb-transfer.py  ${D}/opt/victronenergy/venus-platform/usb-transfer/rc/usb-transfer.py
}

# TODO: Is this needed?
FILES:${PN} += "/opt/victronenergy/venus-platform/usb-transfer/rc"
