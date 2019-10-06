#
# Copyright (C) 2014 Wind River Systems, Inc.
#
DESCRIPTION = "ANSII Color formatting for output in terminal"
HOMEPAGE = "https://pypi.python.org/pypi/termcolor"
SECTION = "devel/python"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING.txt;md5=809e8749b63567978acfbd81d9f6a27d"

SRCNAME = "termcolor"
SRC_URI = "https://pypi.python.org/packages/source/t/${SRCNAME}/${SRCNAME}-${PV}.tar.gz"
SRC_URI[md5sum] = "043e89644f8909d462fbbfa511c768df"
SRC_URI[sha256sum] = "1d6d69ce66211143803fbc56652b41d73b4a400a2891d7bf7a1cdf4c02de613b"

S = "${WORKDIR}/${SRCNAME}-${PV}"

inherit distutils3

DEPENDS += "python-pip"
