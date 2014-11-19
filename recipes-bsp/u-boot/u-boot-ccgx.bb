require u-boot.inc

PR = "r3"

SRC_URI = " \
		https://github.com/victronenergy/u-boot/archive/v2013.01.01-ccgx-v3.tar.gz \
		file://live.cmds \
		file://production.cmds \
		file://recover.cmds \
		file://splash.bgra \
	"

SRC_URI[sha256sum] = "ee3d73d6f05879426a0d60df56562b5500f8df475921af185a3301abb44f505b"
SRC_URI[md5sum] = "730e07ad5b5122fdf1d8f99588c2e9cc"
S = "${WORKDIR}/u-boot-2013.01.01-ccgx-v3"

do_compile_append () {
	mkimage -A arm -T script -C none -n 'Live Script' -d ${WORKDIR}/live.cmds ${WORKDIR}/live.scr
	mkimage -A arm -T script -C none -n 'Production Script' -d ${WORKDIR}/production.cmds ${WORKDIR}/production.scr
	mkimage -A arm -T script -C none -n 'Recover Script' -d ${WORKDIR}/recover.cmds ${WORKDIR}/recover.scr
}

pkg_postinst_${PN} () {
    if [ "x$D" == "x" ]; then
        if [ -e /proc/mtd ]; then
            MTD_DEV=`grep u-boot /proc/mtd`
            UBOOT_DEV=${MTD_DEV:0:4}
        else
            echo "ERROR: No MTD device"
            exit 1;
        fi

        if [ -f /boot/${UBOOT_IMAGE} ] && [ -n $UBOOT_DEV ]; then
            echo "INFO: Erasing $UBOOT_DEV"
            flash_erase  /dev/$UBOOT_DEV 0 0
            echo "INFO: Write U-boot > $UBOOT_DEV"
            nandwrite -p /dev/$UBOOT_DEV /boot/${UBOOT_IMAGE}
            rm /boot/${UBOOT_IMAGE}
            echo "Update finished!"
        else
            echo "ERROR: No u-boot /boot/${UBOOT_IMAGE} image found!"
        fi

        # u-boot up to and including v2013.01.01-ccgx-v2 did not disable the
        # autoboot interrupt, so it added after the update.
        fw_setenv preboot "if gpio input 26; then setenv bootdelay -1; else setenv bootdelay 0; fi"
    else
        # Exit 1 is used to set the status of the package on unpacked in rootfs image
        # The result is that the package will be installed on first boot
        exit 1
    fi
}

do_deploy_append () {
	install -d ${DEPLOY_DIR_IMAGE}
	install ${WORKDIR}/live.scr ${DEPLOY_DIR_IMAGE}
	install ${WORKDIR}/production.scr ${DEPLOY_DIR_IMAGE}
	install ${WORKDIR}/recover.scr ${DEPLOY_DIR_IMAGE}
	install ${WORKDIR}/splash.bgra ${DEPLOY_DIR_IMAGE}
}
