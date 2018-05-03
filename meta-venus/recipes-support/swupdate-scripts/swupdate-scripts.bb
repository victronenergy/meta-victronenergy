DESCRIPTION = "Helper scripts for swupdate"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

inherit allarch

RDEPENDS_${PN} = "bash e2fsprogs-resize2fs swupdate"

SRC_URI = " \
	file://check-updates.init \
	file://check-updates.sh \
	file://functions.sh \
	file://machine.sh \
	file://resize2fs.sh \
	file://scan-versions.init \
	file://scan-versions.sh \
	file://set-feed.sh \
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
    install -m 0755 ${WORKDIR}/resize2fs.sh     ${DEST}/resize2fs.sh
    install -m 0755 ${WORKDIR}/scan-versions.sh ${DEST}/scan-versions.sh
    install -m 0755 ${WORKDIR}/set-feed.sh      ${DEST}/set-feed.sh
    install -m 0755 ${WORKDIR}/set-version.sh   ${DEST}/set-version.sh

    DEST=${D}${sysconfdir}/init.d
    install -d ${DEST}
    install -m 0755 ${WORKDIR}/check-updates.init ${DEST}/check-updates.sh
    install -m 0755 ${WORKDIR}/scan-versions.init ${DEST}/scan-versions.sh

    DEST=${D}${sysconfdir}/rc5.d
    install -d ${DEST}
    ln -sf ../init.d/scan-versions.sh ${DEST}/S90scan-versions.sh
    ln -sf ../init.d/check-updates.sh ${DEST}/S99check-updates.sh
}

FILES_${PN} += "${SCRIPTDIR}"

