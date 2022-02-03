PACKAGES =+ "${PN}-rt2800 ${PN}-rt73"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

#LICENSE:${PN}-rt2800 = "LICENCE.ralink-firmware.txt"
FILES:${PN}-rt2800 = " \
  /lib/firmware/rt2870.bin \
"

#LICENSE:${PN}-rt73 = "LICENCE.ralink-firmware.txt"
FILES:${PN}-rt73 = " \
  /lib/firmware/rt73.bin \
"

PACKAGES =+ "${PN}-rtl-bt"
FILES:${PN}-rtl-bt = " \
  /lib/firmware/rtl_bt \
"

# Wifi NVRAM file for AP6210 module
SRC_URI += "file://brcm/brcmfmac43362-sdio.txt"
FILES:${PN}-bcm43362 += "\
  /lib/firmware/brcm/brcmfmac43362-sdio.txt \
"

# BT firmware for AP6210 module
SRC_URI += "file://brcm/BCM20702A1.hcd"
PACKAGES =+ "${PN}-bcm20702a1"
FILES:${PN}-bcm20702a1 = "\
  /lib/firmware/brcm/BCM20702A1.hcd \
"

# BT firmware for BCM43430A1
SRC_URI += "file://brcm/BCM43430A1.hcd"
PACKAGES =+ "${PN}-bcm43430a1"
FILES:${PN}-bcm43430a1 = "\
  ${nonarch_base_libdir}/firmware/brcm/BCM43430A1.hcd \
"

# For mediatek mt7662
PACKAGES =+ "${PN}-mt7662"
FILES:${PN}-mt7662 = " \
  ${nonarch_base_libdir}/firmware/mt7662_rom_patch.bin \
  ${nonarch_base_libdir}/firmware/mt7662.bin \
"

do_install:append() {
  install -d ${D}${nonarch_base_libdir}/firmware/brcm
  install -m 0644 ${WORKDIR}/brcm/brcmfmac43362-sdio.txt ${D}${nonarch_base_libdir}/firmware/brcm
  install -m 0644 ${WORKDIR}/brcm/BCM20702A1.hcd ${D}${nonarch_base_libdir}/firmware/brcm
  install -m 0644 ${WORKDIR}/brcm/BCM43430A1.hcd ${D}${nonarch_base_libdir}/firmware/brcm
  ln -s brcmfmac43430-sdio.AP6212.txt ${D}${nonarch_base_libdir}/firmware/brcm/brcmfmac43430-sdio.txt
}

do_install:append:rpi() {
  rm ${D}${nonarch_base_libdir}/firmware/brcm/brcmfmac43430-sdio.bin
  rm ${D}${nonarch_base_libdir}/firmware/brcm/brcmfmac43430-sdio.AP6212.txt
  rm ${D}${nonarch_base_libdir}/firmware/brcm/brcmfmac43430-sdio.Hampoo-D2D3_Vi8A1.txt
  rm ${D}${nonarch_base_libdir}/firmware/brcm/brcmfmac43430-sdio.MUR1DX.txt
  rm ${D}${nonarch_base_libdir}/firmware/brcm/brcmfmac43455-sdio.clm_blob
  rm ${D}${nonarch_base_libdir}/firmware/brcm/brcmfmac43455-sdio.bin
  rm ${D}${nonarch_base_libdir}/firmware/brcm/BCM43430A1.hcd
  rm ${D}${nonarch_base_libdir}/firmware/brcm/brcmfmac43430-sdio.txt
  rm ${D}${nonarch_base_libdir}/firmware/brcm/brcmfmac43455-sdio.raspberrypi,3-model-a-plus.txt
  rm ${D}${nonarch_base_libdir}/firmware/brcm/brcmfmac43430-sdio.raspberrypi,3-model-b.txt
  rm ${D}${nonarch_base_libdir}/firmware/brcm/brcmfmac43455-sdio.raspberrypi,3-model-b-plus.txt
  rm ${D}${nonarch_base_libdir}/firmware/brcm/brcmfmac43455-sdio.raspberrypi,4-model-b.txt
  rm ${D}${nonarch_base_libdir}/firmware/brcm/brcmfmac43430-sdio.raspberrypi,model-zero-w.txt
  rm ${D}${nonarch_base_libdir}/firmware/cypress/cyfmac43430-sdio.bin
  rm ${D}${nonarch_base_libdir}/firmware/cypress/cyfmac43455-sdio.bin
  rm ${D}${nonarch_base_libdir}/firmware/cypress/cyfmac43455-sdio.clm_blob
}
