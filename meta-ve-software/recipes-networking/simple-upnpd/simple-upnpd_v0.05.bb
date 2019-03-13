LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

DESCRIPTION = "Daemon which only announces the device its presence over upnp"

inherit pkgconfig

DEPENDS += "glib-2.0 gupnp libsoup-2.4"
PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git"

inherit daemontools useradd

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "simple-upnpd"
USERADD_PARAM_${PN} = "--no-create-home --shell /bin/false -g simple-upnpd simple-upnpd"

DAEMONTOOLS_SERVICE_DIR = "${sysconfdir}/${PN}/service"
DAEMONTOOLS_RUN = "${base_bindir}/start-simple-upnpd"

SRC_URI = " \
	git://github.com/victronenergy/simple-upnpd.git;protocol=https;tag=${PV} \
	file://start-simple-upnpd \
	file://simple-upnpd.skeleton.xml \
"

do_install() {
	install -d ${D}/${base_bindir} ${D}${sysconfdir}

	install -m 0755 ${S}/simple-upnpd ${D}/${base_bindir}
	install -m 0755 ${WORKDIR}/simple-upnpd.skeleton.xml ${D}/${sysconfdir}
	install -m 0755 ${WORKDIR}/start-simple-upnpd ${D}${base_bindir}
}
