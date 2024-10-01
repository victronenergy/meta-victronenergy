DESCRIPTION = "EDID for Ekrano GX display"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "\
    file://EDID_Ekrano_GX_rev_A6_48M.bin \
    file://ekrano-edid.conf \
"

S = "${WORKDIR}/sources"
UNPACKDIR = "${S}"

do_install() {
    fwdir=${D}${nonarch_base_libdir}/firmware
    install -d ${fwdir}
    install -m 644 EDID_Ekrano_GX_rev_A6_48M.bin ${fwdir}

    etcdir=${D}${sysconfdir}/modprobe.d
    install -d ${etcdir}
    install -m 644 ekrano-edid.conf ${etcdir}
}

FILES:${PN} = "${nonarch_base_libdir} ${sysconfdir}"
