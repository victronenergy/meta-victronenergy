SUMMARY = "Preserve validated OCI runtime configuration"
DESCRIPTION = "Captures the allowlisted VENUS_HTTP_PORT setting before \
svscanboot discards the inherited container environment. The validated port \
is stored as plain data for the OCI-specific nginx startup script."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://container-env.sh"

S = "${S_UNUSED}"

inherit update-rc.d

INITSCRIPT_NAME = "container-env.sh"
# Run after mountall and container-volatile have prepared /run, but before
# svscanboot starts supervised services at S95.
INITSCRIPT_PARAMS = "start 06 S ."

do_install() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${UNPACKDIR}/container-env.sh ${D}${sysconfdir}/init.d/
}
