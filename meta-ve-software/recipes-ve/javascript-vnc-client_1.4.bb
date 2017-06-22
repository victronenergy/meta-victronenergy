DESCRIPTION = "Javascript VNC Client: website that hosts a javascript vnc client which auto-connects to host running it"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

# Conflicts with vrmportal, since they both want to be the default website. For sure there is a solution
# for that, but that is out of scope of this initial fast release. And vrmportal is not yet released.

inherit allarch

# base-passwd is needed for www user
DEPENDS = "base-passwd"
RDEPENDS_${PN} = "hiawatha"

SRC_URI = " \
	file://javascript-vnc-client.conf \
	gitsm://github.com/victronenergy/javascript-vnc-client.git;protocol=https;tag=${PV} \
"

S = "${WORKDIR}/git"
BASE_DIR = "/var/www/javascript-vnc-client/"
DEST_DIR = "${D}${BASE_DIR}"

# Empty do compile, because otherwise bitbake will just do a make
do_compile() {
}

do_install () {
	install -d ${DEST_DIR}/
	oe_runmake DESTDIR=${DEST_DIR} install
	install -d ${D}${sysconfdir}/hiawatha/sites-enabled
	install -m 0644 ${WORKDIR}/javascript-vnc-client.conf ${D}${sysconfdir}/hiawatha/sites-enabled/javascript-vnc-client.conf
}

pkg_postinst_${PN}() {
	if [ "x$D" = "x" ]; then
		/etc/init.d/hiawatha restart
	fi
}
