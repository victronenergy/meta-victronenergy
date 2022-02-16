LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
S = "${WORKDIR}"

# not really actually, but bitbake really doesn't like that is depends on time...
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit ve_package

do_configure () {
    printf "${DISTRO_VERSION}\n${DISTRO_NAME}\n${DATE}${TIME}\n" > version
}

do_configure[nostamp] = "1"
do_configure[vardepsexclude] = "DATE TIME"

do_install () {
    install -d ${D}${vedir}
    install -m 644 version ${D}${vedir}
}

FILES:${PN} += "${vedir}"
