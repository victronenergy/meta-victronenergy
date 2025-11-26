SUMMARY = "Python Wrapper for S2 Flexibility Protocol"
SECTION = "devel/python"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=86d3f3a95c324c9479bd8986968f4327"

DEPENDS += "python3-setuptools-scm-native"
RDEPENDS:${PN} += " \
    python3-asyncio \
    python3-core \
    python3-io \
    python3-json \
    python3-logging \
    python3-netclient \
    python3-pprint \
    python3-pydantic \
    python3-threading \
    python3-typing-extensions \
"

SRC_URI = "git://github.com/flexiblepower/s2-python.git;branch=main;protocol=https"
SRC_URI += "file://0001-fix-license-entry.patch"
SRCREV = "169af01bf46d13d48834f18ed135a8f2ba91638c"
S = "${WORKDIR}/git"

inherit python_setuptools_build_meta
