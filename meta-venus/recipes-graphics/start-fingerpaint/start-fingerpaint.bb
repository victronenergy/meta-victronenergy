inherit start-gui

RDEPENDS_${PN} = "fingerpaint"
DAEMONTOOLS_SCRIPT = ". /etc/profile.d/qt6.sh && exec /usr/share/examples/widgets/touch/fingerpaint/fingerpaint"
