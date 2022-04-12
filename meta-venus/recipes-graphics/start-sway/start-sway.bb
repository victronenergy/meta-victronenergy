inherit start-gui

RDEPENDS:${PN} = "sway"
DAEMONTOOLS_SCRIPT = "mkdir -p /tmp/sway && export XDG_RUNTIME_DIR=/tmp/sway && /usr/bin/openvt -s -c 1 /usr/bin/sway -V -d"
