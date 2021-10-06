SETTINGS_DIR = "${sysconfdir}/venus/settings.d"
SETTINGS_FILE ?= "${WORKDIR}/localsettings"

do_install_append () {
    install -d ${D}${SETTINGS_DIR}
    install -m 0644 ${SETTINGS_FILE} ${D}${SETTINGS_DIR}/${PN}
}
