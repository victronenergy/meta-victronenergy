SUMMARY = "daemon implementing the Link-Local Multicast Name Resolution (LLMNR) protocol"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=05a4c5604b9126dcb84dcc6f1a84a7bb"

SRC_URI = " \
    git://github.com/tklauser/llmnrd;tag=${PV} \
    file://0001-llmnrd-set-stdout-to-line-buffering.patch \
"
S = "${WORKDIR}/git"

DAEMONTOOLS_SERVICE_DIR = "${sysconfdir}/${PN}/service"
DAEMONTOOLS_RUN = "setuidgid nobody ${sbindir}/llmnrd -H venus -6"
DAEMONTOOLS_LOG_DIR = "${localstatedir}/volatile/log/${PN}"

inherit daemontools pkgconfig

do_install() {
    oe_runmake 'DESTDIR=${D}' install
}
