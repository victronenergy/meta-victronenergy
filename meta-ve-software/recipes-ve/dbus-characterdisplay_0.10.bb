DESCRIPTION = "VE character display server for easysolar"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9b0a9609befce3122afcc444da0fe825"

inherit gmakevelib
inherit daemontools
inherit python-compile

RDEPENDS_${PN} = "machine-runtime-conf"

SRC_URI = " \
    gitsm://github.com/victronenergy/dbus-characterdisplay.git;protocol=https;tag=v${PV} \
    file://7beba966fdbb9155731ce359dc93ff5523ddd796.patch \
    file://860493a392703516e7fad78aa8cfa84816111c57.patch \
"

S = "${WORKDIR}/git"
PR = "2"

RDEPENDS_${PN} = " \
    dbus-systemcalc-py \
    localsettings \
    python3-core \
    python3-dbus \
    python3-evdev \
"

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_characterdisplay.py"
