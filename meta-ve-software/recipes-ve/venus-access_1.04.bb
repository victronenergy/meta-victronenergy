DESCRIPTION = "Venus access (start essential services to access the device)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit qmakeve
inherit daemontools

RDEPENDS:${PN} = " \
    dbus \
    iptables \
    openssh \
    ssh-tunnel \
"
SRC_URI = "gitsm://github.com/victronenergy/venus-access.git;branch=setup-remote-tunnel;protocol=ssh;user=git"
SRCREV = "bd9e3363d131112863b8e7a9689d2a66c2f3fc23"
S = "${WORKDIR}/git"

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/venus-access"

