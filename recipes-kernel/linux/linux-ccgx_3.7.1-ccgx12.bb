# fix behavior of base do_install_prepend - its overwrite ready to use uImage by uncompresses Image
require linux-ccgx.inc

# Mind it, this recipe is not installed itself but provides kernel-image etc.
# Hence RPEDEND on that one....
RDEPENDS_kernel-image += "linux-backports"
RDEPENDS_kernel-image += "kernel-modules"
RDEPENDS_kernel-image += "mtd-utils"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "08b5aa1413f9578defbb04dbcaa66815"
SRC_URI[sha256sum] = "9fdb5714b82848ce4018ced9eb9adc2b96a7aaaac209ed360e7b5d16aa60bcba"

SRC_URI += " \
	file://0001-enable-CONFIG_DEVTMPFS-for-newer-udev.patch \
	file://0002-ARM-7668-1-fix-memset-related-crashes-caused-by-rece.patch \
	file://0003-ARM-7670-1-fix-the-memset-fix.patch \
	file://0004-import-compiler-gcc5.h-from-v4.1-it-gets-merge-there.patch \
	file://0005-ARM-8158-1-LLVMLinux-use-static-inline-in-ARM-ftrace.patch \
"

# This was introduced to remove uImage from /boot and save 3MB
KERNEL_DROPIMAGE = ""

PR = "r2"

S = "${WORKDIR}/linux-${PV}"

pkg_postinst_kernel-base_append() {
	if [ "x$D" = "x" ]; then
		if [ -e /proc/mtd ]; then
			LINUX_DEV=`grep \"kernel\" /proc/mtd | awk -F: '{print $1}'`
		else
			echo "ERROR: No MTD in proc"
			exit 1;
		fi

		if [ "x$LINUX_DEV" = "x" ]; then
			echo "ERROR: MTD device not found"
			exit 1;
		fi
	
		if [ -f /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION} ] && [ -n $LINUX_DEV ]; then
			echo "INFO: Erasing $LINUX_DEV"
			flash_erase  /dev/$LINUX_DEV 0 0
			echo "INFO: Write Linux kernel > $LINUX_DEV"
			nandwrite -p /dev/$LINUX_DEV /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
			rm /${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}
			echo "Update finished!"
		else
			echo "ERROR: No kernel (/${KERNEL_IMAGEDEST}/${KERNEL_IMAGETYPE}-${KERNEL_VERSION}) image found!"
			exit 1
		fi

		# erase the stored bootparams and use the default kernel ones
		MTD_DEV=`grep bootparms /proc/mtd`
		PARAMS_DEV=${MTD_DEV:0:4}
		echo "INFO: Erasing bootparms $PARAMS_DEV"
		flash_erase /dev/${PARAMS_DEV} 0 0
	else
		# Exit 1 is used to set the status of the package on unpacked in rootfs image
		# The result is that the package will be installed on first boot
		exit 1
	fi
}
