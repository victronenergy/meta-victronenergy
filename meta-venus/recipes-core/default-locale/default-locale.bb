SUMMARY = "set default locale with UTF8 support"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

do_install() {
    install -d ${D}${sysconfdir}/profile.d
    echo "export LC_CTYPE=en_US.utf8" > ${D}${sysconfdir}/profile.d/profile.sh
    chmod 755 ${D}${sysconfdir}/profile.d/profile.sh
}
