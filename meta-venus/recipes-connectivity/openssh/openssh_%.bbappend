FILESEXTRAPATHS_prepend := "${THISDIR}:"

inherit daemontools

DAEMON_PN = "${PN}-sshd"
DAEMONTOOLS_SERVICE_DIR = "/etc/ssh/service"
DAEMONTOOLS_RUN = "${bindir}/start-sshd.sh"
DAEMONTOOLS_DOWN = "1"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/sshd"

SRC_URI += "file://start-sshd.sh"

do_install_append() {
    install -m 755 ${WORKDIR}/start-sshd.sh ${D}${bindir}
}

# disable the update-rc.d
INITSCRIPT_PACKAGES = ""
