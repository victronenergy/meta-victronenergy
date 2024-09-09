DESCRIPTION = "Javascript VNC Client: website that hosts a javascript vnc client which auto-connects to host running it"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit www

# base-passwd is needed for www user
DEPENDS = "base-passwd"
RDEPENDS:${PN} = "nginx"

SRC_URI = " \
    gitsm://github.com/victronenergy/javascript-vnc-client.git;branch=master;protocol=https;tag=${PV} \
"

S = "${WORKDIR}/git"
BASE_DIR = "${WWW_ROOT}/gui-v1"
DEST_DIR = "${D}${BASE_DIR}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install () {
    install -d ${DEST_DIR}/
    oe_runmake DESTDIR=${DEST_DIR} install
}

