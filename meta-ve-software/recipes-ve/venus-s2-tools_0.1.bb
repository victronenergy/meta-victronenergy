SUMMARY = "Command line tools for Victron S2 over D-Bus communication."

SRC_URI = "git://github.com/victronenergy/venus-s2-tools.git;protocol=https;branch=main"
SRCREV = "093b55c806e6bded7de38d23872f08bbb2b2b73a"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

RDEPENDS:${PN} = " \
    localsettings \
    python3-asyncio \
    python3-core \
    python3-datetime \
    python3-dbus-fast \
    python3-json \
    python3-logging \
    python3-s2 \
    python3-typing-extensions \
"

