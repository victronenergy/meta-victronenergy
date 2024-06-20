SUMMARY = "Upload custom logo"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit www

SRC_URI = " \
    file://logo.php \
    file://create-logo-dir \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    mkdir -p "${D}${WWW_ROOT}"
    install -m 644 ${WORKDIR}/logo.php "${D}${WWW_ROOT}"

    install -d "${D}${WWW_RCD}"
    install -m 755 "${WORKDIR}/create-logo-dir" "${D}${WWW_RCD}"
}

