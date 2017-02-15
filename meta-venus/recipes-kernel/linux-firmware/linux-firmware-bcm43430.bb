# Newer versions of OpenEmbedded already include this module. Delete this file
# if you ever update openembedded-core.

LICENSE = "Firmware-broadcom_bcm43xx"
NO_GENERIC_LICENSE[Firmware-broadcom_bcm43xx] = "LICENCE.broadcom_bcm43xx"
LIC_FILES_CHKSUM = "file://LICENCE.broadcom_bcm43xx;md5=3160c14df7228891b868060e1951dfbc"

SRCREV = "12987cadb37de28719990dfc2397ec6d09e10566"
PE = "1"
PV = "0.0+git${SRCPV}"

SRC_URI = " \
	git://git.kernel.org/pub/scm/linux/kernel/git/firmware/linux-firmware.git \
	file://brcmfmac43430-sdio.txt \
"

S = "${WORKDIR}/git"

inherit allarch

CLEANBROKEN = "1"

do_compile() {
	:
}

do_install() {
	install -d ${D}/lib/firmware/brcm
	cp ${S}/brcm/brcmfmac43430-sdio.bin ${D}/lib/firmware/brcm/
	cp ${WORKDIR}/brcmfmac43430-sdio.txt ${D}/lib/firmware/brcm/
}

FILES_${PN} = " \
	/lib/firmware/brcm/brcmfmac43430-sdio.bin \
	/lib/firmware/brcm/brcmfmac43430-sdio.txt \
"
