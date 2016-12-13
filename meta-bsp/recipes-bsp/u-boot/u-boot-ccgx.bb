require u-boot.inc
require u-boot-ccgx.src.inc

PR = "r4"

SRC_URI += " \
	file://fatload-initramfs.cmds \
	file://live.cmds \
	file://production.cmds \
	file://recover.cmds \
	file://upgrade.cmds \
	file://splash.bgra \
"

# NOTE: the swu version set the u-boot env, see u-boot-env-tools!
RDEPENDS_${PN} += "u-boot-env-tools"

do_compile_append () {
	mkimage -A arm -T script -C none -n 'Fatload with initramfs' -d ${WORKDIR}/fatload-initramfs.cmds ${WORKDIR}/fatload-initramfs.scr
	mkimage -A arm -T script -C none -n 'Live Script' -d ${WORKDIR}/live.cmds ${WORKDIR}/live.scr
	mkimage -A arm -T script -C none -n 'Production Script' -d ${WORKDIR}/production.cmds ${WORKDIR}/production.scr
	mkimage -A arm -T script -C none -n 'Recover Script' -d ${WORKDIR}/recover.cmds ${WORKDIR}/recover.scr
	mkimage -A arm -T script -C none -n 'Upgrade Script' -d ${WORKDIR}/upgrade.cmds ${WORKDIR}/upgrade.scr
}

do_deploy_append () {
	install -d ${DEPLOY_DIR_IMAGE}
	install ${WORKDIR}/fatload-initramfs.scr ${DEPLOY_DIR_IMAGE}
	install ${WORKDIR}/live.scr ${DEPLOY_DIR_IMAGE}
	install ${WORKDIR}/production.scr ${DEPLOY_DIR_IMAGE}
	install ${WORKDIR}/recover.scr ${DEPLOY_DIR_IMAGE}
	install ${WORKDIR}/upgrade.scr ${DEPLOY_DIR_IMAGE}
	install ${WORKDIR}/splash.bgra ${DEPLOY_DIR_IMAGE}
}
