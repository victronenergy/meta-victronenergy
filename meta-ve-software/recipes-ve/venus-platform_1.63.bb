DESCRIPTION = "Venus platform"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "dbus iptables udev"
QT6_DEPENDS = "qtbase"
RDEPENDS:${PN} += "can-utils connman flashmq velib-python socketcand"

PR = "1"
SRC_URI = " \
    gitsm://github.com/victronenergy/venus-platform.git;branch=b${PV};protocol=ssh;user=git;tag=v${PV}-rev${PR} \
    file://can.inc \
"
S = "${WORKDIR}/git"

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/venus-platform"

inherit daemontools qmakeve

do_install:append() {
    install -d ${D}/${DAEMONTOOLS_TEMPLATE_CONF_DIR}
    install -m 644 ${WORKDIR}/can.inc ${D}/${DAEMONTOOLS_TEMPLATE_CONF_DIR}

    install -d "${D}/${bindir}"
    install -m 755 ${S}/svectl "${D}/${bindir}"
}

