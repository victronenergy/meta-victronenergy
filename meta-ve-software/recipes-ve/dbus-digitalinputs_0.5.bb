SUMMARY = "Provides dbus support for digital inputs on Venus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d72366a45605591565f5bd03e155bc87"

inherit ve_package
inherit daemontools
inherit python-compile

SRC_URI = "\
    gitsm://github.com/victronenergy/dbus-digitalinputs.git;protocol=https;tag=v${PV} \
    file://start-digitalinputs.sh \
"
S = "${WORKDIR}/git"

RDEPENDS_${PN} = " \
    localsettings \
    python \
    python-dbus \
    python-pygobject \
"

DAEMONTOOLS_SERVICE_DIR = "${bindir}/service"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/start-digitalinputs.sh"

do_install () {
    install -d ${D}${bindir}
    cp -r ${S}/* ${D}${bindir}
    install -m 0755 ${WORKDIR}/start-digitalinputs.sh ${D}${bindir}
}
