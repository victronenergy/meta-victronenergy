FILESEXTRAPATHS:prepend := "${THISDIR}:"

inherit daemontools-template

DAEMON_PN = "${PN}-sshd"
DAEMONTOOLS_RUN = "${bindir}/start-sshd.sh"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/sshd"

SRC_URI += "file://start-sshd.sh"

do_install:append() {
    install -m 755 ${WORKDIR}/start-sshd.sh ${D}${bindir}
}

# disable the update-rc.d
INITSCRIPT_PACKAGES = ""
