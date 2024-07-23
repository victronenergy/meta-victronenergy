QT6_DEPENDS ?= ""
QT6_RDEPENDS ?= ""

DEPENDS += "${QT6_DEPENDS}"
RDEPENDS:${PN} += "${QT6_RDEPENDS}"

inherit qt6-qmake
