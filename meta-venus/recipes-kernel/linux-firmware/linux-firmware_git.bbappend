# NOTE: this can be done a bit smarter since it is known which firmware is needed
# for fw in `find /lib/modules/ -name '*.ko' -exec modinfo -F firmware {} \;`; do echo -n "$fw "; if [ ! -f /lib/firmware/$fw ]; then echo "not shipped"; else echo ok; fi; done

PACKAGES =+ "${PN}-rt2800 ${PN}-rt73"
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

#LICENSE_${PN}-rt2800 = "LICENCE.ralink-firmware.txt"
FILES_${PN}-rt2800 = " \
  /lib/firmware/rt2870.bin \
"

#LICENSE_${PN}-rt73 = "LICENCE.ralink-firmware.txt"
FILES_${PN}-rt73 = " \
  /lib/firmware/rt73.bin \
"

PACKAGES =+ "${PN}-rtl-bt"
FILES_${PN}-rtl-bt = " \
  /lib/firmware/rtl_bt \
"

# Include the NVRAM file for wifi firmware on rpi3.
SRC_URI_append_raspberrypi2 += "file://brcmfmac43430-sdio.txt file://brcmfmac43455-sdio.txt file://brcmfmac43455-sdio.clm_blob"
FILES_${PN}-bcm43430_append_raspberrypi2 += " \
  /lib/firmware/brcm/brcmfmac43430-sdio.txt \
  /lib/firmware/brcm/brcmfmac43455-sdio.txt \
  /lib/firmware/brcm/brcmfmac43455-sdio.clm_blob \
"

do_install_append_raspberrypi2() {
  install -D -m 0644 ${WORKDIR}/brcmfmac43430-sdio.txt ${D}/lib/firmware/brcm
  install -D -m 0644 ${WORKDIR}/brcmfmac43455-sdio.txt ${D}/lib/firmware/brcm
  install -D -m 0644 ${WORKDIR}/brcmfmac43455-sdio.clm_blob ${D}/lib/firmware/brcm
}

# Wifi NVRAM file for AP6210 module
SRC_URI += "file://brcm/brcmfmac43362-sdio.txt"
FILES_${PN}-bcm43362 += "\
  /lib/firmware/brcm/brcmfmac43362-sdio.txt \
"

# BT firmware for AP6210 module
SRC_URI += "file://brcm/BCM20702A1.hcd"
PACKAGES =+ "${PN}-bcm20702a1"
FILES_${PN}-bcm20702a1 = "\
  /lib/firmware/brcm/BCM20702A1.hcd \
"

do_install_append() {
  install -d ${D}/lib/firmware/brcm
  install -m 0644 ${WORKDIR}/brcm/brcmfmac43362-sdio.txt ${D}/lib/firmware/brcm
  install -m 0644 ${WORKDIR}/brcm/BCM20702A1.hcd ${D}/lib/firmware/brcm
}
