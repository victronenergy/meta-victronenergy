DESCRIPTION = "An abstract library implementation of a VT220/xterm/ECMA-48 terminal emulator."

inherit cmake

SRC_URI = "gitsm://github.com/jhofstee/libvterm-cmake;branch=master;protocol=https"
SRCREV = "7f0ed3711252ece4dbc81bd3b8aff4398dfc8652"
S = "${WORKDIR}/git"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://ext/LICENSE;md5=be5681ffe0dc58ccc9756bc6260fe0cd"
