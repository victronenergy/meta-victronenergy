SUMMARY = "Restore volatile-directory semantics in restricted containers"
DESCRIPTION = "Clears /run and /var/volatile during boot when their tmpfs mounts are unavailable"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://container-volatile.sh"

S = "${S_UNUSED}"

inherit update-rc.d

INITSCRIPT_NAME = "container-volatile.sh"
# Run after mountall (S03) has attempted the fstab tmpfs mounts and before
# udev (S04) starts creating fresh runtime state.
INITSCRIPT_PARAMS = "start 04 S ."

do_install() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${UNPACKDIR}/container-volatile.sh ${D}${sysconfdir}/init.d/
}
