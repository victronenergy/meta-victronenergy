DESCRIPTION = "A plugin that turns FlashMQ into a dbus aware MQTT broker."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a5815abd99fd5defcd36000125b24f99"
DEPENDS = "dbus"

inherit cmake pkgconfig

SRC_URI = "git://github.com/victronenergy/dbus-flashmq.git;branch=feat-auth-with-vncpassword-from-file;protocol=https;tag=f62ef731c4803d0cf399c79dd3856a5589fda224"
SRC_URI += "file://0001-decline-authentication-when-the-password-file-doesn-.patch"

S = "${WORKDIR}/git"
EXTRA_OECMAKE = "-DCMAKE_BUILD_TYPE=Release"

PACKAGES =+ "${PN}-test"

FILES:${PN}-test = "${bindir}/flashmq-dbus-plugin-tests"
