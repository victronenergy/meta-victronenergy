FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

RDEPENDS_${PN} += "u-boot-fw-utils"

# ubi support etc depends on the MACHINE
PACKAGE_ARCH = "${MACHINE_ARCH}"

SRC_URI += "\
	file://0001-parser-report-error-if-no-images-etc-found.patch \
	file://0002-copyfile-do-not-close-output-file-descriptor.patch \
	file://0003-Callers-of-copyfile-must-close-the-output-descriptor.patch \
	file://0004-core-util-add-ustrtoull.patch \
	file://0005-copyfile-add-new-property-to-handle-offset-for-raw-h.patch \
"
