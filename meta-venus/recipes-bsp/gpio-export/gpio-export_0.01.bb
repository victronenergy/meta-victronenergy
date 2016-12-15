SUMMARY = "gpio pin exports"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

INHIBIT_DEFAULT_DEPS = "1"

SRC_URI = " \
	file://gpio_pins.sh \
	file://gpio_list \
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
	install -m 0755 ${WORKDIR}/gpio_pins.sh	${D}${sysconfdir}/init.d/gpio_pins.sh
	install -m 0600 ${WORKDIR}/gpio_list	${D}${sysconfdir}/venus/gpio_list
}

