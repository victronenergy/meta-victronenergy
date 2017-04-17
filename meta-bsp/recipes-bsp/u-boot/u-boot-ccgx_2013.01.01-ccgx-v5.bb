require u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb"
SRC_URI += "https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "e47178a8ba8fcd12502df3cb8b3f8dee"
SRC_URI[sha256sum] = "b133ce45ec10679199b46c3b82f6e3b57b57dd613dba5683010cfb3352b7cd43"
S = "${WORKDIR}/u-boot-${PV}"

SRC_URI += " \
	file://fatload-initramfs.cmds \
	file://live.cmds \
	file://upgrade.cmds \
	file://splash.bgra \
"

do_compile_append () {
	mkimage -A arm -T script -C none -n 'Fatload with initramfs' -d ${WORKDIR}/fatload-initramfs.cmds ${WORKDIR}/fatload-initramfs.scr
	mkimage -A arm -T script -C none -n 'Live Script' -d ${WORKDIR}/live.cmds ${WORKDIR}/live.scr
	mkimage -A arm -T script -C none -n 'Upgrade Script' -d ${WORKDIR}/upgrade.cmds ${WORKDIR}/upgrade.scr
}

do_deploy_append () {
	install -d ${DEPLOYDIR}
	install ${WORKDIR}/fatload-initramfs.scr ${DEPLOYDIR}
	install ${WORKDIR}/live.scr ${DEPLOYDIR}
	install ${WORKDIR}/upgrade.scr ${DEPLOYDIR}
	install ${WORKDIR}/splash.bgra ${DEPLOYDIR}
}
