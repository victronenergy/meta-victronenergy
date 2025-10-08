FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://config.txt"

do_deploy:append() {
    install -m 640 ${UNPACKDIR}/config.txt ${DEPLOYDIR}/config.txt
    echo "dwc_otg.lpm_enable=0 console=serial0,115200 usb-storage.delay_use=20" > ${DEPLOYDIR}/cmdline.txt
}
