FILESEXTRAPATHS_prepend := "${THISDIR}/cronie:"

# note: PRINC is needed in danny/ccgx
PRINC ?= "666000"
PRINC := "${@int(PRINC) + 3}"

SRC_URI += "file://crontab"

do_install_append () {
	install -m 0755 ${WORKDIR}/crontab ${D}${sysconfdir}
	chmod 600 ${D}${sysconfdir}/crontab
}

