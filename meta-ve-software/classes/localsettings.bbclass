SETTINGS_DIR = "${sysconfdir}/venus/settings.d"
SETTINGS_FILE ?= "${UNPACKDIR}/localsettings"

do_install:append () {
    install -d ${D}${SETTINGS_DIR}
    install -m 0644 ${SETTINGS_FILE} ${D}${SETTINGS_DIR}/${PN}
}
