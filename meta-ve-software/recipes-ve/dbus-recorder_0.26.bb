DESCRIPTION = "DBusrecorder"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit ve_package
inherit python-compile

RDEPENDS:${PN} = "bash python3-core python3-dbus"

SRC_URI = "git://github.com/victronenergy/dbus-recorder.git;branch=master;protocol=https;tag=${PV}"
S = "${WORKDIR}/git"

do_install () {
    install -d ${D}/${bindir}

    install -m 0755 ${S}/play.sh ${D}/${bindir}
    install -m 0755 ${S}/startdemo.sh ${D}/${bindir}
    install -m 0755 ${S}/stopdemo.sh ${D}/${bindir}

    # copy python scripts
    install -m 755 -D ${S}/*.py ${D}/${bindir}
    # copy data files
    install -m 444 -D ${S}/*.csv ${D}/${bindir}
}
