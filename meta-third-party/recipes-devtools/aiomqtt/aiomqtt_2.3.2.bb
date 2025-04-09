SRC_URI = "git://github.com/empicano/aiomqtt.git;protocol=https;branch=main"
SRCREV = "04fa87cd61e8d5ada03e3decb3cf080f321f19cd"
S = "${WORKDIR}/git"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a462083fa4d830bdcf8c22a8ddf453cf"

DEPENDS = "python3-hatchling-native"

inherit python_pep517 python_setuptools_build_meta

