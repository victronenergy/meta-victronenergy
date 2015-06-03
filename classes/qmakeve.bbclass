inherit qt4e
inherit ve_package

qmakeve_do_install() {
	oe_runmake INSTALL_ROOT=${D} install
}

inherit siteconfig

EXPORT_FUNCTIONS do_install
