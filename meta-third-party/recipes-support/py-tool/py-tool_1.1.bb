SUMMARY = "A tool for upgrading Pylontech batteries using victron inverters"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://License.txt;md5=8dedea07eb63579306bbbff4c5254cd3"
SRC_URI = "git://github.com/pylontech-sw/py_tool.git;protocol=https;branch=master"
SRC_URI += "file://0001-adjust-the-makefile-for-OE.patch"
SRCREV = "656eee7f8e00bfc91e94dffd94b88b3c036abcf1"
UPSTREAM_CHECK_GITTAGREGEX = "V(?P<pver>\S+)"

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

