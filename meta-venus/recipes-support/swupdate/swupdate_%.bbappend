FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

RDEPENDS_${PN} += "u-boot-fw-utils"

# ubi support etc depends on the MACHINE
PACKAGE_ARCH = "${MACHINE_ARCH}"
