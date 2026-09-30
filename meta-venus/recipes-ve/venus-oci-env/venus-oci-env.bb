SUMMARY = "Preserve validated OCI runtime configuration"
DESCRIPTION = "Captures the allowlisted VENUS_HTTP_PORT and VENUS_HTTPS_PORT \
settings before svscanboot discards the inherited container environment. The \
validated ports are stored as plain data in /run/venus."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = " \
    file://container-env.sh \
    file://container-http-ports.sh \
"

S = "${S_UNUSED}"

inherit update-rc.d

INITSCRIPT_NAME = "container-env.sh"
# Run after mountall and container-volatile have prepared /run, but before
# svscanboot starts supervised services at S95.
INITSCRIPT_PARAMS = "start 06 S ."

do_install() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${UNPACKDIR}/container-env.sh ${D}${sysconfdir}/init.d/

    # run by start-nginx.sh before it starts nginx
    install -d ${D}${sysconfdir}/venus/www.d
    install -m 0755 ${UNPACKDIR}/container-http-ports.sh ${D}${sysconfdir}/venus/www.d/
}
