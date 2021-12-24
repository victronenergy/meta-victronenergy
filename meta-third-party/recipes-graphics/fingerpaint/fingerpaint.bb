include recipes-qt/qt6/qt6-git.inc

inherit qt6-qmake

SUMMARY = "qt6 example to check touch support"
DEPENDS = "qtbase"

LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://main.cpp;endline=49;md5=aeafed859c0c994cc23debcc70f81fdd"

QT_MODULE = "qtbase"
S = "${WORKDIR}/fingerpaint"
SRC_URI_append = ";subpath=examples/widgets/touch/fingerpaint"

FILES_${PN} = "/usr/share/examples"

