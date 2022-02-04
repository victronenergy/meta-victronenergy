SUMMARY = "Socketcand, socketcan over tcp/ip"
LICENSE = "GPLv2 | BSD"
LIC_FILES_CHKSUM = "file://${S}/socketcand.h;md5=abaa92ce5821533fe88fd4c645e9b494;endline=1"

SRC_URI = "git://github.com/linux-can/socketcand;branch=master;protocol=https"
SRCREV = "ae0af080058a576d62c72ffc011d644d0b4dcb98"
S = "${WORKDIR}/git"

inherit autotools-brokensep

