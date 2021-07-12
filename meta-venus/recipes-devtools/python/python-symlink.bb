LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SUMMARY = "make python3 the default python"
INHIBIT_DEFAULT_DEPS = "1"

do_install() {
    mkdir -p ${D}${bindir}
    ln -s ${bindir}/python3 ${D}${bindir}/python
}
