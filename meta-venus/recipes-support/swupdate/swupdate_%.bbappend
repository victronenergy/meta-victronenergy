FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

RDEPENDS_${PN} += "u-boot-fw-utils"

# ubi support etc depends on the MACHINE
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += "\
	file://0001-parser-report-error-if-no-images-etc-found.patch \
	file://0001-copyfile-do-not-close-output-file-descriptor.patch \
	file://0002-handlers-add-pipe-handler.patch \
"
