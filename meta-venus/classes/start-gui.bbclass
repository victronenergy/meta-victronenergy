# Common class to make it possible to switch the default gui.
#
# This is intended to only replace the daemontools service starting the gui,
# while leaving the gui's themselves installed. This can be done with:
#
# inherit start-gui
#
# PACKAGES += "start-fingerpaint"
# DAEMON_PN = "start-fingerpaint"
# RDEPENDS_${DAEMON_PN} = "${PN}"

inherit daemontools

# and adding it to the list of all known gui's below
GUIS = "start-gui-v1 start-fingerpaint start-sway"

# Not setting DAEMON_PN to something different will remove the whole
# gui instead of only the daemontools service.


# Make sure there can be only 1 and they replace eachother.
RPROVIDES:${DAEMON_PN} += "virtual/start-gui"
GUIS:remove = "${DAEMON_PN}"
RREPLACES:${DAEMON_PN} = "${GUIS}"
RCONFLICTS:${DAEMON_PN} = "${GUIS}"

# NOTE: PREFERRED_RPROVIDER_virtual/start-gui determines which gui
# to start by default.

# just to make OE happy, its rather pointless to license a start command...
LICENSE ?= "MIT"
LIC_FILES_CHKSUM ?= "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
