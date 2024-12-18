DESCRIPTION = "Venus access (start essential services to access the device)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

QT6_DEPENDS = "qtbase"
RDEPENDS:${PN} = " \
    dbus \
    iptables \
    openssh \
    ssh-tunnel \
"
UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = "gitsm://github.com/victronenergy/venus-access.git;branch=master;protocol=ssh;user=git"
SRCREV = "2bb867efa449b692ab09e7a19e271ed47da9ede0"
S = "${WORKDIR}/git"

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/venus-access"

inherit daemontools qmakeve
