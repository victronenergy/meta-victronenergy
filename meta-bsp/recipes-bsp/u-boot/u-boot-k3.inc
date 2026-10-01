LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"

COMPATIBLE_MACHINE = "^k3$"

DEPENDS += "\
    bison-native \
    python3-jsonschema-native \
    python3-pyelftools-native \
    python3-yamllint-native \
"

inherit python3native

PV = "2025.01-k3-v4"
SRC_URI = "git://github.com/victronenergy/u-boot.git;protocol=https;branch=k3"
SRCREV = "0a7f4761eac23cbed8e18995c4d22f448dd22fe8"
S = "${WORKDIR}/git"
