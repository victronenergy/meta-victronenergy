SUMMARY = "Enable sysfs hardware setup in capable OCI containers"
DESCRIPTION = "Remounts sysfs read-write before udev when CAP_SYS_ADMIN is available"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI = "file://container-sysfs.sh"

S = "${S_UNUSED}"

inherit update-rc.d

INITSCRIPT_NAME = "container-sysfs.sh"
# Run after mountall (S03), but before udev (S04) evaluates hardware rules.
INITSCRIPT_PARAMS = "start 04 S ."

do_install() {
    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${UNPACKDIR}/container-sysfs.sh ${D}${sysconfdir}/init.d/
}
