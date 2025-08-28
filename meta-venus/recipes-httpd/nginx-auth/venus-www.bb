SUMMARY = "scripts to authenticate a user"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    git://github.com/victronenergy/venus-www.git;branch=master;protocol=https \
    file://prevent-sessions-from-being-removed.patch \
    file://create-venus-session-dir \
"

PR = "1"

SRCREV = "f3918e820c70a66280b9b96acf5942240b5f778c"
S = "${WORKDIR}/git"

inherit www

AUTHDIR = "${WWW_ROOT}/auth"

do_install() {
    mkdir -p "${D}${AUTHDIR}"
    install -m 644 "${S}/auth/browser_detection.js" ${D}${AUTHDIR}
    install -m 644 "${S}/auth/cert-explanation.html" ${D}${AUTHDIR}
    install -m 644 "${S}/auth/favicon.ico" "${D}${AUTHDIR}"
    install -m 644 "${S}/auth/info_icon.png" "${D}${AUTHDIR}"
    install -m 644 "${S}/auth/login.php" "${D}${AUTHDIR}"
    install -m 644 "${S}/auth/logout.php" "${D}${AUTHDIR}"
    install -m 644 "${S}/auth/session.php" "${D}${AUTHDIR}"
    install -m 644 "${S}/auth/styles.css" "${D}${AUTHDIR}"
    install -m 644 "${S}/auth/test.php" "${D}${AUTHDIR}"
    install -m 644 "${S}/auth/victron_logo.png" ${D}${AUTHDIR}

    install -d "${D}${WWW_RCD}"
    install -m 755 "${UNPACKDIR}/create-venus-session-dir" "${D}${WWW_RCD}"
}
