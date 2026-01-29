DESCRIPTION = "TI blobs used by U-Boot"
LICENSE = "TI-TFL"
LIC_FILES_CHKSUM = "file://LICENSE.ti;md5=b5aebf0668bdf95621259288c4a46d76"

PV = "11.02.11"
SRC_URI = "git://git.ti.com/processor-firmware/ti-linux-firmware.git;protocol=https;branch=ti-linux-firmware"
SRCREV = "8ec0c42b8ccf2c9e8bebfd7c4e03d93fed555442"
S = "${WORKDIR}/git"

inherit firmware

COMPATIBLE_MACHINE = "^am62xx"

TI_DM = "am62xx"
TI_SYSFW = "am62x"

do_install() {
    install -d ${D}${FIRMWARE_DIR}/ti-dm/${TI_DM}
    install -m 0644 ${S}/ti-dm/${TI_DM}/* ${D}${FIRMWARE_DIR}/ti-dm/${TI_DM}

    install -d ${D}${FIRMWARE_DIR}/ti-sysfw
    install -m 0644 ${S}/ti-sysfw/*-${TI_SYSFW}-* ${D}${FIRMWARE_DIR}/ti-sysfw
}

FILES:${PN} = "${FIRMWARE_DIR}"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

INHIBIT_DEFAULT_DEPS = "1"
INSANE_SKIP += "arch"
