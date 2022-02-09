LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SUMMARY = "make python3 the default python"
INHIBIT_DEFAULT_DEPS = "1"

do_install() {
    mkdir -p ${D}${bindir}
    ln -s ${bindir}/python3 ${D}${bindir}/python
}
