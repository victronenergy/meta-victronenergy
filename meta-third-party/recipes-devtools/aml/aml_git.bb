DESCRIPTION = "Andri's Main Loop."
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYING;md5=af623d135dc7dd7e8963c0051f96aa37"

DEPENDS += "libjpeg-turbo pixman"

inherit meson

SRC_URI = " \
    git://github.com/any1/aml;protocol=https;rev=d1783ccb26ad094858633401095085c9de683841 \
"

S = "${WORKDIR}/git/"

