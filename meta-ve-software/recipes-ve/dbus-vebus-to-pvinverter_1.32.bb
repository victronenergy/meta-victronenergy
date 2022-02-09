DESCRIPTION = "PV inverter which gets its information from de VE.Bus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit ve_package
inherit daemontools
inherit python-compile

PR = "r0"
SRC_URI = "gitsm://github.com/victronenergy/dbus_conversions.git;branch=master;protocol=https;tag=${PV}"
DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 1000000 -a 100000000 ${bindir}/dbus_vebus_to_pvinverter.py"
S = "${WORKDIR}/git"
RDEPENDS:${PN} = "python3-core python3-dbus python3-pygobject"

do_install () {
    install -d ${D}${bindir}
    install -m 755 -D ${S}/*.py ${D}${bindir}

    install -d ${D}${bindir}/ext/velib_python
    install ${S}/ext/velib_python/vedbus.py ${D}${bindir}/ext/velib_python
    install ${S}/ext/velib_python/ve_utils.py ${D}${bindir}/ext/velib_python
}

