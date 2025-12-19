SUMMARY = "A tool for upgrading Pylontech batteries using victron inverters"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://License.txt;md5=8dedea07eb63579306bbbff4c5254cd3"
SRC_URI = "git://github.com/pylontech-sw/py_tool.git;protocol=https;branch=master"
SRC_URI += "file://0001-adjust-the-makefile-for-OE.patch"
SRCREV = "fcb471f4097080fa95f17a88f41fc3596531b7d2"

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

