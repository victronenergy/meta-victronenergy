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

PV = "2025.01-k3-v5"
SRC_URI = "git://github.com/victronenergy/u-boot.git;protocol=https;branch=k3"
SRCREV = "517f194e458a7c51bdacd978f1defb6660dcafd3"
S = "${WORKDIR}/git"
