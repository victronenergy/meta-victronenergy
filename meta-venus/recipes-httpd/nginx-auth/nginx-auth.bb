SUMMARY = "scripts to authenticate a user"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "git://github.com/jhofstee/nginx_auth_request.git"
SRCREV = "caf8c10125294ff6c4bab79702580edc958801c5"
S = "${WORKDIR}/git"

inherit www

AUTHDIR = "${WWW_ROOT}/auth"
CERTDIR = "${WWW_ROOT}/../cert-explanation"

do_install() {
    mkdir -p "${D}${AUTHDIR}"
    install -m 644 "${S}/auth/login.php" "${D}${AUTHDIR}"
    install -m 644 "${S}/auth/logout.php" "${D}${AUTHDIR}"
    install -m 644 "${S}/auth/MuseoSans_500.otf" ${D}${AUTHDIR}
    install -m 644 "${S}/auth/session.php" "${D}${AUTHDIR}"
    install -m 644 "${S}/auth/styles.css" "${D}${AUTHDIR}"
    install -m 644 "${S}/auth/test.php" "${D}${AUTHDIR}"
    install -m 644 "${S}/auth/victron_logo.png" ${D}${AUTHDIR}

    # this repository should be renamed or split or ... well this works for now.
    mkdir -p ${D}${CERTDIR}
    install -m 644 "${S}/cert-explanation/browser_detection.js" ${D}${CERTDIR}
    install -m 644 "${S}/cert-explanation/info_icon.png" ${D}${CERTDIR}
    install -m 644 "${S}/cert-explanation/styles.css" ${D}${CERTDIR}
    install -m 644 "${S}/cert-explanation/index.html" ${D}${CERTDIR}
    install -m 644 "${S}/cert-explanation/MuseoSans_500.otf" ${D}${CERTDIR}
    install -m 644 "${S}/cert-explanation/victron_logo.png" ${D}${CERTDIR}
}
