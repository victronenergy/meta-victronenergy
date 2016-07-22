FILESEXTRAPATHS_prepend := "${THISDIR}:"

# note: PRINC is needed in danny/ccgx
PRINC ?= "666000"
PRINC := "${@int(PRINC) + 22}"

inherit daemontools

DAEMON_PN = "${PN}-sshd"
DAEMONTOOLS_SERVICE_DIR = "/etc/ssh/service"
DAEMONTOOLS_RUN = "${bindir}/start-sshd.sh"
DAEMONTOOLS_DOWN = "1"
DAEMONTOOLS_LOG_DIR = "${DAEMONTOOLS_LOG_DIR_PREFIX}/sshd"

SRC_URI += "file://start-sshd.sh"

do_install_append() {
	install -m 755 ${WORKDIR}/start-sshd.sh ${D}${bindir}

	# fixme
	ln -sfn ${DAEMONTOOLS_SERVICE_DIR} ${D}${DAEMONTOOLS_SERVICES_DIR}/sshd
}

# disable the update-rc.d
INITSCRIPT_PACKAGES = ""

pkg_postinst_${PN}-sshd_append() {
	if [ "x$D" == "x" ]; then
		update-rc.d -f sshd remove
	else
		exit 1
	fi
}