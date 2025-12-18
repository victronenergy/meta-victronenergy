SUMMARY = "A tool for upgrading Pylontech batteries using victron inverters"

# doesn't have a copyright / license file at the moment.
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/Proprietary;md5=0557f9d92cf58f2ccdd50f62f8ac0b28"
SRC_URI = "git://github.com/pylontech-sw/py_tool.git;protocol=https;branch=master"
SRC_URI += "file://0001-adjust-the-makefile-for-OE.patch"
SRCREV = "1d565fa6228dcea930cd5e11d2285e993a29643a"

S = "${WORKDIR}/git"

do_compile() {
    rm -rf ${S}/py-tool
    oe_runmake -C ${S}
}

do_install() {
    install -d ${D}/opt/victronenergy/mqtt-rpc/thirdparty/pylon
    install -m 755 ${B}/py-tool ${D}/opt/victronenergy/mqtt-rpc/thirdparty/pylon
}

FILES:${PN} = "/opt/victronenergy/mqtt-rpc/thirdparty"

