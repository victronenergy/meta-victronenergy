DESCRIPTION = "Venus platform"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DEPENDS = "dbus libevdev iptables qtbase udev"
RDEPENDS:${PN} += "can-utils connman flashmq mk2vsc qtbase-plugin-qopensslbackend socketcand"

# container has no macvlan/ipvlan-manageable interface for connman to work
# with anyway (see meta-venus/recipes-packagegroups/packagegroup-venus-core.bb)
RDEPENDS:${PN}:remove:venus-container = "connman"

# compiled-in Device/IsContainer flag other daemons/the GUI can key off
EXTRA_QMAKEVARS_PRE:append:venus-container = " DEFINES+=VENUS_CONTAINER"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    gitsm://github.com/victronenergy/venus-platform.git;branch=mbath/oci;protocol=ssh;user=git \
    file://can.inc \
    file://venus_dbus_bridge_template.conf \
    file://venus_rpc_bridge_template.conf \
"
SRCREV = "fbd8dbd1f922613ef8c64b0cb84d507eb87a029c"
S = "${WORKDIR}/git"

DAEMONTOOLS_RUN = "${@softlimit(d, data=100000000, stack=1000000, all=100000000)} ${bindir}/venus-platform"

inherit daemontools pkgconfig qmakeve

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
