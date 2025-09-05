DESCRIPTION = "Localsettings python scripts"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit ve_package
inherit daemontools
inherit python-compile

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/victronenergy/localsettings.git;branch=master;protocol=https \
    file://com.victronenergy.settings.conf \
"
SRCREV = "7eb30e4dafa1cacb2550dbbc8eecd91508dd124f"
S = "${WORKDIR}/git"

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/${PN}.py --path=/data/conf"

RDEPENDS:${PN} += " \
    python3-core \
    python3-dbus \
    python3-pygobject \
    python3-lxml \
"

do_install () {
    install -d ${D}${bindir}
    install -m 755 -D ${S}/*.py ${D}${bindir}

    install -d ${D}/${sysconfdir}/dbus-1/system.d
    install -m 644 ${UNPACKDIR}/com.victronenergy.settings.conf ${D}/${sysconfdir}/dbus-1/system.d
}
