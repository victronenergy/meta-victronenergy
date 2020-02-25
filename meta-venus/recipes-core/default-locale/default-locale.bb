SUMMARY = "set default locale with UTF8 support"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

do_install() {
    install -d ${D}${sysconfdir}/profile.d
    echo "export LC_CTYPE=en_US.utf8" > ${D}${sysconfdir}/profile.d/profile.sh
    chmod 755 ${D}${sysconfdir}/profile.d/profile.sh
}
