# NOTE: this can be done a bit smarter since it is known which firmware is needed
# for fw in `find /lib/modules/ -name '*.ko' -exec modinfo -F firmware {} \;`; do echo -n "$fw "; if [ ! -f /lib/firmware/$fw ]; then echo "not shipped"; else echo ok; fi; done

PACKAGES =+ "${PN}-rt2800 ${PN}-rt73 ${PN}-rtl8723b"
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

#LICENSE_${PN}-rt2800 = "LICENCE.ralink-firmware.txt"
FILES_${PN}-rt2800 = " \
  /lib/firmware/rt2870.bin \
"

#LICENSE_${PN}-rt73 = "LICENCE.ralink-firmware.txt"
FILES_${PN}-rt73 = " \
  /lib/firmware/rt73.bin \
"

FILES_${PN}-rtl8723b = " \
  /lib/firmware/rtl_bt/rtl8723b_fw.bin \
"

# Include the NVRAM file for wifi firmware on rpi3.
SRC_URI_append_raspberrypi2 += "file://brcmfmac43430-sdio.txt"
FILES_${PN}-bcm43430_append_raspberrypi2 += " \
  /lib/firmware/brcm/brcmfmac43430-sdio.txt \
"

do_install_append_raspberrypi2() {
  install -D -m 0644 ${WORKDIR}/brcmfmac43430-sdio.txt ${D}/lib/firmware/brcm/brcmfmac43430-sdio.txt
}
