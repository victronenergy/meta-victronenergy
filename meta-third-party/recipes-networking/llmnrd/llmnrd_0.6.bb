SUMMARY = "daemon implementing the Link-Local Multicast Name Resolution (LLMNR) protocol"
LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://COPYING;md5=05a4c5604b9126dcb84dcc6f1a84a7bb"

UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = " \
    git://github.com/tklauser/llmnrd;branch=master;protocol=https \
    file://0001-llmnrd-set-stdout-to-line-buffering.patch \
"
SRCREV = "33c661b515890df780d55c0011d06d1cc698ff65"
S = "${WORKDIR}/git"

DAEMONTOOLS_RUN = "setuidgid nobody ${sbindir}/llmnrd -H venus -6"
DAEMONTOOLS_LOG_DIR = "${localstatedir}/volatile/log/${PN}"

inherit daemontools pkgconfig

do_install() {
    oe_runmake 'DESTDIR=${D}' install
}
