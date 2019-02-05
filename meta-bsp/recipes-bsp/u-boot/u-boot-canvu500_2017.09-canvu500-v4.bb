require u-boot-canvu500.inc

DEPENDS += "bc-native u-boot-mkimage-native"

SRC_URI += "\
	file://install.cmds \
	file://splash.bmp.gz;unpack=0 \
"

do_compile_append () {
	mkimage -A arm -T script -C none -n 'Install Script' \
		-d ${WORKDIR}/install.cmds ${WORKDIR}/install.scr
}

do_deploy_append () {
	install -d ${DEPLOYDIR}
	install -m 0644 ${WORKDIR}/install.scr ${DEPLOYDIR}/install-${MACHINE}.scr
	install ${WORKDIR}/splash.bmp.gz ${DEPLOYDIR}
}
