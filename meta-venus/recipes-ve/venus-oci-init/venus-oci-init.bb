SUMMARY = "Let containers stop without CAP_SYS_BOOT"
DESCRIPTION = "Ends the container once sysvinit has shut down, since halt and reboot are not permitted"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = " \
    file://container-init \
    file://container-stop.sh \
"

S = "${S_UNUSED}"

inherit update-alternatives update-rc.d

ALTERNATIVE:${PN} = "init"
ALTERNATIVE_LINK_NAME[init] = "${base_sbindir}/init"
ALTERNATIVE_TARGET[init] = "${base_sbindir}/init.container"
# Above sysvinit, so its re-exec on shutdown runs container-init.
ALTERNATIVE_PRIORITY[init] = "100"

INITSCRIPT_NAME = "container-stop.sh"
# After umountfs (S40), before halt and reboot (S90).
INITSCRIPT_PARAMS = "start 89 0 6 ."

do_install() {
    install -d ${D}${base_sbindir} ${D}${sysconfdir}/init.d
    install -m 0755 ${UNPACKDIR}/container-init ${D}${base_sbindir}/init.container
    install -m 0755 ${UNPACKDIR}/container-stop.sh ${D}${sysconfdir}/init.d/
}

RDEPENDS:${PN} = "sysvinit"
