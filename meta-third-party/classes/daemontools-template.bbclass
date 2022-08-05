inherit daemontools

# NOTE: bitbake is a bit picky about comments in multiline variables.
# If you need to include a comment, it can be done with:
#
# DAEMONTOOLS_TEMPLATE_CONF = "# First comment\n"
# DAEMONTOOLS_TEMPLATE_CONF .= "# More comment"
#
# If it becomes too messy, a file can be used instead of a variable.

DAEMONTOOLS_TEMPLATE_CONF ?= ""
DAEMONTOOLS_SERVICE_SYMLINK = "0"
DAEMONTOOLS_SERVICE_DIR = "${DAEMONTOOLS_TEMPLATE_DIR}"

do_install:append() {
	mkdir -p "${D}/${DAEMONTOOLS_TEMPLATE_CONF_DIR}"
	printf "${DAEMONTOOLS_TEMPLATE_CONF}\n" > "${D}/${DAEMONTOOLS_TEMPLATE_CONF_DIR}/${PN}.conf"
}
