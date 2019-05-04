SECTION = "kernel"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

COMPATIBLE_MACHINE = "ccgx"

inherit kernel

LINUX_VERSION_EXTENSION = "-venus"

KERNEL_CONFIG_COMMAND = "oe_runmake -C ${S} O=${B} ccgx_defconfig"
KERNEL_IMAGETYPES = "uImage"
KERNEL_EXTRA_ARGS = "zImage"
KERNEL_DEVICETREE = "am3517-ccgx.dtb"
KEEPUIMAGE = "no"
UBOOT_ENTRYPOINT = "80008000"

# To be compatible with the deployed u-boot, a uImage containing a
# zImage with dtb appended is created.
uboot_prep_kimage() {
	cat arch/${ARCH}/boot/zImage arch/${ARCH}/boot/dts/am3517-ccgx.dtb \
		> linux.bin
        linux_comp=none
}

RDEPENDS_kernel-base = ""

S = "${WORKDIR}/linux-${PV}"
B = "${WORKDIR}/build"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz \
	file://0001-lets-find-out-who-is-scheduling-NULL-pointers.patch"
SRC_URI[md5sum] = "dfa2e4bb30923cae0fe4072364549a06"
SRC_URI[sha256sum] = "c9e9cdcd4e9672d269be8690b7aeef5a75121a122072eada01411ed59ca520bb"

DEPENDS += "openssl-native"
HOST_EXTRACFLAGS += "-I${STAGING_INCDIR_NATIVE}"
