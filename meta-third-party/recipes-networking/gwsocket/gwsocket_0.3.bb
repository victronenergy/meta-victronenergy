SUMMARY = " gwsocket is a standalone, simple, yet powerful rfc6455 compliant WebSocket Server."
SECTION = "networking"
LICENSE = "CLOSED"
#LIC_FILES_CHKSUM = "file://LICENSE;md5=c5ad90debd10be20aa95189a0a7c4253"

inherit autotools

SRC_URI = " \
	http://tar.gwsocket.io/gwsocket-${PV}.tar.gz \
	file://0001-Add-support-for-stdin-stdout.patch \
"

SRC_URI[md5sum] = "5272d0dfd95cb652ad330eec730f7776"
SRC_URI[sha256sum] = "b2a46bbbc99faaf2dd99095d81bc29128fe54298ba6f4f9203ec3d2e4cc4c528"


