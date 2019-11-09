DESCRIPTION = "Javascript VNC Client: website that hosts a javascript vnc client which auto-connects to host running it"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit www

# base-passwd is needed for www user
DEPENDS = "base-passwd"
RDEPENDS_${PN} = "hiawatha"

SRC_URI = " \
    file://javascript-vnc-client.conf \
    gitsm://github.com/victronenergy/javascript-vnc-client.git;protocol=https;tag=${PV} \
"

S = "${WORKDIR}/git"
BASE_DIR = "${WWW_ROOT}"
DEST_DIR = "${D}${BASE_DIR}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    install -d ${DEST_DIR}/
    oe_runmake DESTDIR=${DEST_DIR} install
    install -d ${D}${sysconfdir}/hiawatha/sites-enabled
    install -m 0644 ${WORKDIR}/javascript-vnc-client.conf ${D}${sysconfdir}/hiawatha/sites-enabled/javascript-vnc-client.conf
}

