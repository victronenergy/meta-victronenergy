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
    file://0006-parser-add-parsing-of-mtdname-property.patch \
    file://0007-corelib-add-function-to-get-the-mtd-from-the-name.patch \
    file://0008-flash-handler-add-support-for-specifying-mtd-name-in.patch \
    file://0009-handlers-add-pipe-handler.patch \
"
