LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

DESCRIPTION = "Daemon which only announces the device its presence over upnp"

inherit pkgconfig

DEPENDS += "glib-2.0 gupnp libsoup"
PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git"

inherit daemontools useradd

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "simple-upnpd"
USERADD_PARAM:${PN} = "--no-create-home --shell /bin/false -g simple-upnpd simple-upnpd"

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${base_bindir}/start-simple-upnpd"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    git://github.com/victronenergy/simple-upnpd.git;branch=master;protocol=https \
    file://start-simple-upnpd \
    file://simple-upnpd.skeleton.xml \
"
SRCREV = "704dc945e59bfcb281dbe2e9607b75ca716a21b3"

do_install() {
    install -d ${D}/${base_bindir} ${D}${sysconfdir}

    install -m 0755 ${S}/simple-upnpd ${D}/${base_bindir}
    install -m 0755 ${UNPACKDIR}/simple-upnpd.skeleton.xml ${D}/${sysconfdir}
    install -m 0755 ${UNPACKDIR}/start-simple-upnpd ${D}${base_bindir}
}
