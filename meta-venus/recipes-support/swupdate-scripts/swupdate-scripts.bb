DESCRIPTION = "Helper scripts for swupdate"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

# swupdate is MACHINE specific, this is as well.
PACKAGE_ARCH = "${MACHINE_ARCH}"
RDEPENDS:${PN} = "bash e2fsprogs-resize2fs swupdate"

SRC_URI = " \
    file://check-updates.init \
    file://check-updates.sh \
    file://functions.sh \
    file://machine.sh \
    file://machines.conf \
    file://remount-rw.sh \
    file://resize2fs.sh \
    file://scan-versions.init \
    file://scan-versions.sh \
    file://set-feed.sh \
    file://set-swu-feed.sh \
    file://set-version.sh \
"

S = "${WORKDIR}"

SCRIPTDIR = "/opt/victronenergy/swupdate-scripts"

do_install () {
    DEST=${D}${SCRIPTDIR}
    install -d ${DEST}
    install -m 0755 ${WORKDIR}/check-updates.sh ${DEST}/check-updates.sh
    install -m 0644 ${WORKDIR}/functions.sh     ${DEST}/functions.sh
    install -m 0644 ${WORKDIR}/machine.sh       ${DEST}/machine.sh
    install -m 0644 ${WORKDIR}/machines.conf    ${DEST}/machines.conf
    install -m 0755 ${WORKDIR}/remount-rw.sh    ${DEST}/remount-rw.sh
    install -m 0755 ${WORKDIR}/resize2fs.sh     ${DEST}/resize2fs.sh
    install -m 0755 ${WORKDIR}/scan-versions.sh ${DEST}/scan-versions.sh
    install -m 0755 ${WORKDIR}/set-feed.sh      ${DEST}/set-feed.sh
    install -m 0755 ${WORKDIR}/set-swu-feed.sh  ${DEST}/set-swu-feed.sh
    install -m 0755 ${WORKDIR}/set-version.sh   ${DEST}/set-version.sh

    DEST=${D}${sysconfdir}/init.d
    install -d ${DEST}
    install -m 0755 ${WORKDIR}/check-updates.init ${DEST}/check-updates.sh
    install -m 0755 ${WORKDIR}/scan-versions.init ${DEST}/scan-versions.sh

    DEST=${D}${sysconfdir}/rc5.d
    install -d ${DEST}
    ln -sf ../init.d/scan-versions.sh ${DEST}/S98scan-versions.sh
    ln -sf ../init.d/check-updates.sh ${DEST}/S99check-updates.sh
}

FILES:${PN} += "${SCRIPTDIR}"

