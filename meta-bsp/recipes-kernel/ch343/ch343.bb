SUMMARY = "Driver for CH343 USB serial port"
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6"

PV = "2.2"
SRC_URI = " \
    git://github.com/WCHSoftGroup/ch343ser_linux.git;protocol=https;branch=main \
"
SRCREV = "e7204309559d3844d09d0dee61ef104ee2402629"

S = "${WORKDIR}/git/driver"

inherit module

EXTRA_OEMAKE += "KERNELDIR=${STAGING_KERNEL_DIR}"

do_install() {
    dest=${D}/lib/modules/${KERNEL_VERSION}/${PN}
    install -d ${dest}
    install -m 0644 ch343.ko ${dest}
}
