LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b682f77530f2c81c6633e2ba58fae236"

RDEPENDS:${PN} = " python3-core"
SRC_URI = "git://github.com/victronenergy/velib_python.git;branch=master;protocol=https"
SRCREV = "c79220cc50c66088e548d5f59745e955f77ef857"
S = "${WORKDIR}/git"

do_install() {
    install -d ${D}/${sbindir}
    install -m 755 ${S}/mosquitto_bridge_registrator.py ${D}/${sbindir}
    install -m 644 ${S}/ve_utils.py ${D}/${sbindir}
}
