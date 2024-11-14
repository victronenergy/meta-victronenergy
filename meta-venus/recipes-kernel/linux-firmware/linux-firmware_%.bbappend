PACKAGES =+ "${PN}-rt2800 ${PN}-rt73"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Note: only as long a the rpi has conflicting firmware files...
PACKAGE_ARCH = "${MACHINE_ARCH}"

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

# backwards compatible symlinks
FILES_${PN}-mt7601u += "${nonarch_base_libdir}/firmware/mt7601u.bin "

# For mediatek mt7662
PACKAGES =+ "${PN}-mt7662"
FILES:${PN}-mt7662 = " \
  ${nonarch_base_libdir}/firmware/mediatek/mt7662_rom_patch.bin \
  ${nonarch_base_libdir}/firmware/mediatek/mt7662.bin \
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

