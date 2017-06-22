SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=d7810fab7487fb0aad327b76f1be7cd7"

SUMMARY = "WIP linux kernel for the ccgx"
DISCRIPTION = "\
	This provides a dts enabled linux kernel for the ccgx based on 4.9 LTS. \
	It does use an appended dtb though, to be compatible with u-boot expecting \
	a kernel with a build-in board file. \
	\
	For onboard hw there is no known advantage using this newer kernel. For USB \
	devices like Wifi / Bluetooth or BLE adapters switching might be worthwhile \
	but that needs to be evaluated. \
	\
	Some issue with needs to be addressed to deploy this kernel: \
	- splashscreen \
	- Backlight levels \
	- CAN-bus phy driver power enable \
	- Test support USB dongles \
	-   fw_setenv miscargs "omapdss.def_disp=lcd omapfb.vram=0:2M vram=2M fbskip usbcore.autosuspend=-1" \
	-   on-board USB hub seems unstable \
	- Check which modules are not needed and which ones are.. \
	- OE: gpio export now needs to be in userspace, see meta-venus/recipes-bsp/gpio-export \
	- OE, gui: backlight name changed to /sys/class/backlight/backlight \
	- for the upgrade image to work, either the ramdisk_size should be increased, or not \
	-  all kernel-modules should be shipped \
"

# Unfortunately at the moment openembedded cannot compile two kernels at
# the same time afaik, it would need https://patchwork.openembedded.org/patch/132830/.
# So disable this kernel for now...
DEFAULT_PREFERENCE = "-1"

inherit kernel

KERNEL_PRIORITY = "0"
LINUX_KERNEL_TYPE = "standard"
KERNEL_CONFIG_COMMAND = "make -C ${S} O=${B} ARCH=arm omap2plus_defconfig"

RDEPENDS_kernel-image += "kernel-modules"

# The ccgx u-boot does not have bootz enabled. To be compatible with the deployed
# u-boot, an uImage containing a zImage with dtb appended is created. This builds
# a regular zImage and dtb first, kernel_do_compile_append concatenates them and
# creates an uImage for it.
LINUX_VERSION_EXTENSION = "-venus"
KERNEL_EXTRA_ARGS = "LOADADDR=80008000 am3517-ccgx.dtb"
KERNEL_IMAGETYPE = "uImage"

# make an uImage containing the zImage with dtb appended, so it can be booted
# with the same bootm command /scripts as the deployed u-boot + script
kernel_do_compile_append() {
	rm ${KERNEL_OUTPUT}
	cat arch/${ARCH}/boot/zImage arch/${ARCH}/boot/dts/am3517-ccgx.dtb > zImage-with-dtb
	mkimage -A arm -O linux -C none  -T kernel -a 80008000 -e 80008000 -n 'Linux-with-dtb' -d zImage-with-dtb ${KERNEL_OUTPUT}
}

S = "${WORKDIR}/linux-${PV}"
B = "${WORKDIR}/linux-${PACKAGE_ARCH}-${LINUX_KERNEL_TYPE}-build"

SRC_URI = " \
	https://github.com/victronenergy/linux/archive/v${PV}.tar.gz \
	file://0001-ccgx-make-the-fbcon-a-module.patch \
	file://0001-enable-ralink-usb-wifi-drivers-as-module.patch \
"
SRC_URI[md5sum] = "ebb762530df802c571b543c1692bb412"
SRC_URI[sha256sum] = "1e190653763eb40216a03c0d27b3af4c54739b43ac10629e0a4acbe60b8ccd4a"

