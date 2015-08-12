DESCRIPTION = "inotify tools"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=ac6c26e52aea428ee7f56dc2c56424c6"

inherit autotools
BBCLASSEXTEND = "native nativesdk"

PR = "r0"
SRC_URI = "http://github.com/downloads/rvoicilas/inotify-tools/inotify-tools-3.14.tar.gz"
SRC_URI[md5sum] = "b43d95a0fa8c45f8bab3aec9672cf30c"
SRC_URI[sha256sum] = "222bcca8893d7bf8a1ce207fb39ceead5233b5015623d099392e95197676c92f"
