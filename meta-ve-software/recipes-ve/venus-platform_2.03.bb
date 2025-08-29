DESCRIPTION = "Venus platform"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "dbus iptables qtbase udev"
RDEPENDS:${PN} += "can-utils connman flashmq qtbase-plugin-qopensslbackend socketcand"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/victronenergy/venus-platform.git;branch=master;protocol=ssh;user=git \
    file://tailscale.patch \
    file://can.inc \
    file://venus_dbus_bridge_template.conf \
    file://venus_rpc_bridge_template.conf \
"
SRCREV = "fa577c5e7df447ac90fd4c8a262ded54fe10c7ce"
S = "${WORKDIR}/git"

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/venus-platform"

inherit daemontools qmakeve

do_install:append() {
    install -d ${D}/${DAEMONTOOLS_TEMPLATE_CONF_DIR}
    install -m 644 ${UNPACKDIR}/can.inc ${D}/${DAEMONTOOLS_TEMPLATE_CONF_DIR}

    install -d "${D}/${bindir}"
    install -m 755 ${S}/svectl "${D}/${bindir}"

    install -d ${D}${datadir}/flashmq
    install ${UNPACKDIR}/venus_dbus_bridge_template.conf ${D}${datadir}/flashmq
    install ${UNPACKDIR}/venus_rpc_bridge_template.conf ${D}${datadir}/flashmq
}

FILES:${PN} += "${datadir}"
