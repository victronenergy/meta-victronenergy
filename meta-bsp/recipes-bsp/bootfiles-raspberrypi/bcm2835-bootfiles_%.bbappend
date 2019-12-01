FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://config.txt"

do_deploy_append() {
    install -m 640 ${WORKDIR}/config.txt ${DEPLOYDIR}/config.txt
    echo "dwc_otg.lpm_enable=0 console=serial0,115200" > ${DEPLOYDIR}/cmdline.txt
}
