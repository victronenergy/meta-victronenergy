SUMMARY = "Socketcand, socketcan over tcp/ip"
LICENSE = "GPLv2 | BSD"
LIC_FILES_CHKSUM = "file://${S}/socketcand.h;md5=abaa92ce5821533fe88fd4c645e9b494;endline=1"

SRC_URI = "git://github.com/linux-can/socketcand"
SRCREV = "5dab329ff36824a3e4feab046dd9878946d31043"
S = "${WORKDIR}/git"

inherit autotools-brokensep

