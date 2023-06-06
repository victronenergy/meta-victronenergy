DESCRIPTION = "Andri's Main Loop."
LICENSE = "ISC"
LIC_FILES_CHKSUM = "file://COPYING;md5=af623d135dc7dd7e8963c0051f96aa37"

DEPENDS += "libjpeg-turbo pixman"

inherit meson

SRC_URI = " \
    git://github.com/any1/aml;protocol=https;rev=3afc3aacce6d0342bdff71c38cc477d6e23b9be3 \
"

S = "${WORKDIR}/git"

