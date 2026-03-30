DESCRIPTION = "An abstract library implementation of a VT220/xterm/ECMA-48 terminal emulator."

inherit cmake

SRC_URI = "gitsm://github.com/jhofstee/libvterm-cmake;branch=master;protocol=https"
SRCREV = "850f909efd6c8f6bd1cc2b789f48886c4608ac78"
S = "${WORKDIR}/git"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://ext/LICENSE;md5=be5681ffe0dc58ccc9756bc6260fe0cd"
