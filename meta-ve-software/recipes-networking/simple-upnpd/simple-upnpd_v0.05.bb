LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

DESCRIPTION = "Daemon which only announces the device its presence over upnp"

DEPENDS += "gupnp libsoup-2.4"
RDEPENDS_${PN} = "glib-2.0 gupnp"
PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}/git"
PR = "r1"

INITSCRIPT_NAME = "simple-upnpd"
INITSCRIPT_PARAMS = "start 99 5 2 . stop 10 0 1 6 ."
inherit update-rc.d useradd

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM_${PN} = "simple-upnpd"
USERADD_PARAM_${PN} = "--no-create-home --shell /bin/false -g simple-upnpd simple-upnpd"

SRC_URI = " \
	git://github.com/victronenergy/simple-upnpd.git;protocol=https;tag=${PV} \
	file://simple-upnpd \
	file://simple-upnpd.skeleton.xml \
"

do_install() {
	install -d ${D}/${base_bindir} ${D}${sysconfdir}/init.d

	install -m 0755 ${S}/simple-upnpd ${D}/${base_bindir}
	install -m 0755 ${WORKDIR}/simple-upnpd.skeleton.xml ${D}/${sysconfdir}
	sed -i "s/:::MACHINE:::/${MACHINE}/g" ${D}${sysconfdir}/simple-upnpd.skeleton.xml
	install -m 0755 ${WORKDIR}/simple-upnpd ${D}${sysconfdir}/init.d
}

pkg_postinst_${PN}() {
	if [ "x$D" = "x" ]; then
		hwaddr=$(ifconfig -a | grep "eth0.*HWaddr" | awk '{print $(NF)}')
		hwaddr_small=$(echo ${hwaddr} | sed "s/://g" |  tr '[:upper:]' '[:lower:]' )
		cat ${sysconfdir}/simple-upnpd.skeleton.xml | sed -e "s/:::MAC:::/${hwaddr}/g" -e "s/:::mac_small:::/${hwaddr_small}/g"  > ${sysconfdir}/simple-upnpd.xml
	else
		exit 1
	fi
}
