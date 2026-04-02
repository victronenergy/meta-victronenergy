DESCRIPTION = "OpenEEBUS by NIBE is a Apache 2.0 licensed software library that implements the EEBUS protocol for communication between devices in a home automation environment."

LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=86d3f3a95c324c9479bd8986968f4327"
SRC_URI = " \
    git://github.com/NIBEGroup/openeebus.git;protocol=https;branch=main \
    file://0001-add-an-install-target.patch \
    file://0002-add-pkgconfig-support.patch \
"
SRCREV = "d9a9253678e2e252534eec7a0ae6c1f60a06b785"

S = "${WORKDIR}/git"

DEPENDS = "avahi cjson libwebsockets openssl"

inherit cmake pkgconfig

