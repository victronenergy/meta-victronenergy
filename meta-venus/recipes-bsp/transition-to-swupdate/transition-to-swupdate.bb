DESCRIPTION = "conversion from opkg update to swupdate. \
				WARNING: this does completely erase the data and rootfs!"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"
PACKAGE_ARCH = "${MACHINE_ARCH}"

S = "${WORKDIR}"

SRC_URI += "https://updates.victronenergy.com/feeds/ccgx2/venus-netinstall-initramfs-ccgx.ext2.gz.u-boot"
SRC_URI[md5sum] = "01d2370ff0ad7f06d7af083ffb4f390c"
SRC_URI[sha256sum] = "bf191d5fa3d11c3d8e440a29aa439ae438c6d2725439a99316365fa2228eaa0f"

# just place the files in the root, the whole rootfs will be erased anyway..
do_install () {
	install -m 644 ${WORKDIR}/venus-netinstall-initramfs-ccgx.ext2.gz.u-boot ${D}
}

pkg_postinst_${PN} () {
    if [ "x$D" = "x" ]; then
		# first load the netinstall from the old mtd layout
		fw_setenv load-netinstall 'ubi part rootfs && ubifsmount rootfs && ubifsload 83000000 venus-netinstall-initramfs-ccgx.ext2.gz.u-boot'

		# thereafter update the mtd partitions
		fw_setenv update-mtdparts "setenv mtdparts 'mtdparts=omap2-nand.0:128k(spl1),128k(spl2),128k(spl3),128k(spl4),1m(u-boot),256k(env1),256k(env2),1m(u-boot2),256k(bootparms),768k(splash),6m(kernel1),6m(kernel2),-(ubisystem)'"

		# burn the installer to nand in the new kernel2 mtd, so we can resume on (power) failure etc
		fw_setenv burn-installer 'nand erase.part kernel2 && nand write 83000000 kernel2 $filesize'

		# as very last the env is stored, when interrupt this simply restart
		fw_setenv upd-misc 'setenv miscargs root=/dev/ram rw console=ttyO0,115200 $mtdparts omapdss.def_disp=lcd omapfb.vram=0:2M vram=2M ramdisk_size=32768'
		fw_setenv upd-boot "setenv nandboot 'setenv bootargs \$miscargs \$mtdparts && nand read 82000000 kernel1 && nand read 83000000 kernel2 && bootm 82000000 83000000' && setenv upd_mode 1 && saveenv && reset"

		# a bit paranoid, set mtdparts as well, so updating works with the default env as well
		fw_setenv mtdparts 'mtdparts=omap2-nand.0:512k(MLO),1m(u-boot),256k(env1),256k(env2),1m(u-boot2),256k(bootparms),768k(splash),6m(kernel),200m(data),-(rootfs)'

		# load u-boot instead of falcon mode
		fw_setenv upd_mode 1

		# instruct it to do above
		fw_setenv nandboot run load-netinstall update-mtdparts burn-installer upd-misc upd-boot

		echo goodbye!
		reboot
	fi
}

FILES_${PN} = "/"
