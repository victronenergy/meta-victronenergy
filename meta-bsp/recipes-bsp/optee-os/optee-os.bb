DESCRIPTION = "Open Portable Trusted Execution Environment"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c1f21c4f72f372ef38a5a4aee55ec173"

PV = "4.8.0"
SRC_URI = "git://github.com/OP-TEE/optee_os.git;protocol=https;branch=master"
SRCREV = "86660925433a8d4d1b19cfa5fe940081d77b34b4"
S = "${WORKDIR}/git"
B = "${WORKDIR}/build"

inherit firmware python3native

DEPENDS += "python3-cryptography-native python3-pyelftools-native"

COMPATIBLE_MACHINE = "^am62xx$"

OPTEE_PLATFORM = "k3"
OPTEE_FLAVOR = "am62x"
OPTEE_ARCH = "arm64"
OPTEE_CORE = "${@d.getVar('OPTEE_ARCH').upper()}"

EXTRA_OEMAKE += "\
    CROSS_COMPILE64=${TARGET_PREFIX} \
    PLATFORM=${OPTEE_PLATFORM} \
    PLATFORM_FLAVOR=${OPTEE_FLAVOR} \
    CFG_${OPTEE_CORE}_core=y \
    ta-targets=ta_${OPTEE_ARCH} \
    O=${B} \
"

CFLAGS += "--sysroot=${STAGING_DIR_HOST}"

CPPFLAGS[unexport] = "1"
LDFLAGS[unexport] = "1"
AS[unexport] = "1"
LD[unexport] = "1"

do_configure[noexec] = "1"

do_compile() {
    oe_runmake -C ${S} all
}
do_compile[cleandirs] += "${B}"

do_install() {
    install -d ${D}${FIRMWARE_DIR}
    install -m 0644 ${B}/core/tee-raw.bin ${D}${FIRMWARE_DIR}
}

FILES:${PN} = "${FIRMWARE_DIR}"
