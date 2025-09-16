# Use U-Boot 2025.04 that supports Raspberry Pi 5
# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV:raspberrypi5 = "502120fe1cbadcb49dbafe15860ae14ce87287f0"

SRC_URI:raspberrypi5 = "git://github.com/victronenergy/u-boot;protocol=https;branch=rpi_v2025.04"

DEPENDS += "gnutls-native"
