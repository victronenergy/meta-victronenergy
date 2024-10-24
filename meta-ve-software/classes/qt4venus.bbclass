QT4_DEPENDS ?= ""
QT4_RDEPENDS ?= ""

DEPENDS += "${QT4_DEPENDS}"
RDEPENDS:${PN} += "${QT4_RDEPENDS}"

qt4venus_do_install() {
    oe_runmake INSTALL_ROOT=${D} install
}

inherit qt4e

EXPORT_FUNCTIONS do_install
