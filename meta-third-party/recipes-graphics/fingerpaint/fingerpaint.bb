include recipes-qt/qt6/qt6-git.inc

inherit qt6-qmake

SUMMARY = "qt6 example to check touch support"
DEPENDS = "qtbase"

LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://main.cpp;endline=49;md5=e0c1dea774f102c041d4f7f00053bd5f"

QT_MODULE = "qtbase"
S = "${WORKDIR}/fingerpaint"
SRC_URI:append = ";subpath=examples/widgets/touch/fingerpaint"
CXXFLAGS += "-std=c++17"

FILES_${PN} = "/usr/share/examples"

