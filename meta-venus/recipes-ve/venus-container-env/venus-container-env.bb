SUMMARY = "Capture VENUS_*-prefixed environment variables early in container boot"
DESCRIPTION = "Docker/Podman's -e variables reach PID 1, but svscanboot strips the \
environment before starting svscan, so nothing under /service (including \
start-nginx.sh) ever sees them. This runs as a plain rcS.d script - before \
svscanboot, with no dependency that could block boot - and writes anything \
the container was started with into a file that later scripts can source \
instead of relying on inherited environment."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://container-env.sh"

S = "${S_UNUSED}"

inherit update-rc.d

INITSCRIPT_NAME = "container-env.sh"
# Run after mountall (S03) so /etc is writable, well before svscanboot.
INITSCRIPT_PARAMS = "start 03 S ."

do_install() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${UNPACKDIR}/container-env.sh ${D}${sysconfdir}/init.d/
}
