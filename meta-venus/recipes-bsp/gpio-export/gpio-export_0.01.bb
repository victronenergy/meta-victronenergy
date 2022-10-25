SUMMARY = "gpio pin exports"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

PACKAGE_ARCH = "${MACHINE_ARCH}"

DEPENDS += "update-rc.d-native"
RDEPENDS:${PN} += "\
    bash \
    libgpiod-tools \
"

INHIBIT_DEFAULT_DEPS = "1"

SRC_URI = " \
    file://gpio_pins.sh \
    file://gpio_list \
"

SRC_URI:append:ccgx = "\
    file://mkx_pins.sh \
"

S = "${WORKDIR}"

INITSCRIPT_NAME = "gpio_pins.sh"
INITSCRIPT_PARAMS = "start 90 S ."
inherit update-rc.d

do_install () {
    # Create directories and install device independent scripts
    install -d ${D}${sysconfdir}/rcS.d
    install -d ${D}${sysconfdir}/venus
    install -d ${D}${sysconfdir}/init.d

    # Install device dependent scripts
    install -m 0755 ${WORKDIR}/gpio_pins.sh    ${D}${sysconfdir}/init.d/gpio_pins.sh
    install -m 0600 ${WORKDIR}/gpio_list    ${D}${sysconfdir}/venus/gpio_list
}

do_install:append:ccgx () {
    install -m 0755 ${WORKDIR}/mkx_pins.sh    ${D}${sysconfdir}/init.d/mkx_pins.sh
    update-rc.d -r ${D} mkx_pins.sh start 91 S .
}
