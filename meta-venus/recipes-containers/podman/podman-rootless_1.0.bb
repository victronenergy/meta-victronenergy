SUMMARY = "System environment for running podman rootless as a dedicated user"
DESCRIPTION = "Creates the 'container' system user and the runtime \
directories podman needs to run containers without root. The \
subuid/subgid entry (shadow_%.bbappend) that this depends on lives with \
the recipe that already owns those files."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit useradd update-rc.d

# newuidmap/newgidmap (setuid, used by podman/crun to set up the user
# namespace UID/GID mapping) come from shadow - Venus normally only ships
# busybox's login/passwd applets, so this has to be pulled in explicitly
# for rootless containers to work.
RDEPENDS:${PN} = "shadow"

USERADD_PACKAGES = "${PN}"
# This static image account is deliberately below 2000. UIDs/GIDs 2000..2999
# are reserved by venus.conf for persistent post-image extension identities.
# -s /bin/sh (rather than the usual /bin/false for Venus service users):
# there is deliberately no boot-time service starting containers yet, so
# testing means an interactive shell as this user (su/sudo -u container).
# Revisit once container startup is automated.
# -G dialout: the standard udev default rule (50-udev-default.rules)
# already puts every ttyUSBn/ttyACMn/etc device in the dialout group, so
# this makes any USB-serial/RS485 peripheral usable from a rootless
# container out of the box - podman run still needs --group-add
# keep-groups per invocation for the container process to actually pick
# this membership up (there is no global default for that), but no
# manual chgrp/usermod is needed on the device or user any more.
USERADD_PARAM:${PN} = "-d /data/home/container -r -p '*' -s /bin/sh -G dialout container"

INITSCRIPT_NAME = "container-cgroup-delegation"
INITSCRIPT_PARAMS = "start 20 5 ."

SRC_URI = " \
    file://volatiles \
    file://storage.conf \
    file://container-cgroup-delegation.sh \
    file://run-as-container \
"
S = "${S_UNUSED}"

do_install() {
    install -d ${D}${sysconfdir}/default/volatiles
    install -m 0644 ${UNPACKDIR}/volatiles ${D}${sysconfdir}/default/volatiles/40_${PN}

    # Template only - the volatiles fragment above copies this into the
    # container user's own $HOME/.config/containers/storage.conf on first
    # boot, since that's the file rootless podman actually reads.
    install -d ${D}${sysconfdir}/podman-rootless
    install -m 0644 ${UNPACKDIR}/storage.conf ${D}${sysconfdir}/podman-rootless/storage.conf

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${UNPACKDIR}/container-cgroup-delegation.sh ${D}${sysconfdir}/init.d/container-cgroup-delegation

    install -d ${D}${bindir}
    install -m 0755 ${UNPACKDIR}/run-as-container ${D}${bindir}/run-as-container
}

FILES:${PN} = " \
    ${sysconfdir}/default/volatiles/40_${PN} \
    ${sysconfdir}/podman-rootless/storage.conf \
    ${sysconfdir}/init.d/container-cgroup-delegation \
    ${bindir}/run-as-container \
"
