SUMMARY = " gwsocket is a standalone, simple, yet powerful rfc6455 compliant WebSocket Server."
SECTION = "networking"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=f1761bc50122921c99a05384a66bf422"

inherit autotools

SRC_URI = " \
    http://tar.gwsocket.io/gwsocket-${PV}.tar.gz \
    file://0001-Add-support-for-stdin-stdout.patch \
"

SRC_URI[md5sum] = "5272d0dfd95cb652ad330eec730f7776"
SRC_URI[sha256sum] = "b2a46bbbc99faaf2dd99095d81bc29128fe54298ba6f4f9203ec3d2e4cc4c528"


