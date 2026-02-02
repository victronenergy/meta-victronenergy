DESCRIPTION = "Trusted Firmware-A"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://docs/license.rst;md5=6ed7bace7b0bc63021c6eba7b524039e"

PV = "2.14.0"
SRC_URI = "git://git.trustedfirmware.org/TF-A/trusted-firmware-a.git;protocol=https;branch=master"
SRCREV = "1d5aa939bc8d3d892e2ed9945fa50e36a1a924cc"
S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

inherit firmware

COMPATIBLE_MACHINE = "^am62xx$"

TFA_ARCH = "aarch64"
TFA_PLATFORM = "k3"
TFA_SPD = "opteed"
TFA_BOARD = "lite"
TFA_BUILD = "bl31"

EXTRA_OEMAKE += "\
    CROSS_COMPILE64=${TARGET_PREFIX} \
    PLAT=${TFA_PLATFORM} \
    SPD=${TFA_SPD} \
    TARGET_BOARD=${TFA_BOARD} \
    BUILD_BASE=${B} \
"

CPPFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

do_configure[noexec] = "1"

do_compile() {
    oe_runmake -C ${S} ${TFA_BUILD}
}
do_compile[cleandirs] += "${B}"

do_install() {
    install -d ${D}${FIRMWARE_DIR}
    install -m 0644 ${B}/${TFA_PLATFORM}/${TFA_BOARD}/release/bl31.bin ${D}${FIRMWARE_DIR}
}

FILES:${PN} = "${FIRMWARE_DIR}"
