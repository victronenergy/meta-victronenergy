DESCRIPTION = "Venus platform"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit qmakeve
inherit daemontools

DEPENDS = "dbus iptables udev"
RDEPENDS:${PN} += "flashmq velib-python"

SRC_URI = " \
    gitsm://github.com/victronenergy/venus-platform.git;branch=setup-remote-tunnel2;protocol=ssh;user=git \
    file://can.inc \
"
SRCREV = "75b7ee91be246d25366161499f27bbd7f6a51902"
S = "${WORKDIR}/git"

#SRC_URI += "file://0001-don-t-trigger-a-gui-restart-from-venus-platform.patch"

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/venus-platform"

do_install:append() {
    install -d ${D}/${DAEMONTOOLS_TEMPLATE_CONF_DIR}
    install -m 644 ${WORKDIR}/can.inc ${D}/${DAEMONTOOLS_TEMPLATE_CONF_DIR}

    install -d "${D}/${bindir}"
    install -m 755 ${S}/svectl "${D}/${bindir}"
}

