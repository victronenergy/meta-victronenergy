DESCRIPTION = "Mali support for Allwinner devices"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b234ee4d69f5fce4486a80fdaf4a4263"

COMPATIBLE_MACHINE = "sunxi"

inherit module

SRC_URI = "git://github.com/mripard/sunxi-mali.git;protocol=https"
SRCREV = "69e23ab841cfe90a78fd163fb355fe5a6a7da260"
S = "${WORKDIR}/git"

MALI_REV ?= "r8p1"
MALI_DIR = "${S}/${MALI_REV}/src/devicedrv/mali"
MALI_OPTS = "\
    USING_UMP=0 \
    BUILD=release \
    USING_PROFILING=0 \
    MALI_PLATFORM=sunxi \
    USING_DVFS=1 \
    USING_DEVFREQ=1 \
"

EXTRA_OEMAKE += "${MALI_OPTS} KDIR=${STAGING_KERNEL_BUILDDIR} -C ${MALI_DIR}"

do_configure() {
    cd ${MALI_REV}
    quilt push -a || test $?=2
}

do_install() {
    install -d ${D}/lib/modules/${KERNEL_VERSION}/video
    install -m 0644 ${MALI_DIR}/mali.ko ${D}/lib/modules/${KERNEL_VERSION}/video
}
