DESCRIPTION = "VE character display server for easysolar"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9b0a9609befce3122afcc444da0fe825"

inherit gmakevelib
inherit daemontools
inherit python-compile

RDEPENDS_${PN} = "machine-runtime-conf"

SRC_URI = " \
    gitsm://github.com/victronenergy/dbus-characterdisplay.git;protocol=https;tag=v${PV} \
    file://0001-prevent-keypresses-from-reaching-the-GUI.patch \
    file://0002-Show-sensible-text-when-AC-in-not-configured.patch \
    file://0003-handle-new-backlight-controller.patch \
    file://0004-reduce-amount-of-light-needed-to-activate-the-backli.patch \
"

PR = "1"

S = "${WORKDIR}/git"

RDEPENDS_${PN} = " \
    dbus-systemcalc-py \
    localsettings \
    python-argparse \
    python-dbus \
    python-evdev \
"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_characterdisplay.py"
