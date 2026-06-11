LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=2ca5f2c35c8cc335f0a19756634782f1"

COMPATIBLE_MACHINE = "^k3$"

DEPENDS += "bison-native python3-jsonschema-native python3-pyelftools-native python3-yamllint-native"

inherit python3native

PV = "2025.01-k3-v1"
SRC_URI = "git://github.com/victronenergy/u-boot.git;protocol=https;nobranch=1"
SRCREV = "8549c23bf1f684b873e97fbbf5430d1b45e1720b"
S = "${WORKDIR}/git"
