# changes bindir to point to a per package location
# XXX: keep /opt/color-control for the ccgx and bbb for now since it might
# depend on hardcoded paths.

vedir := "/opt/victronenergy"
vedir_bpp3 := "/opt/color-control"
vedir_beaglebone := "/opt/color-control"
PACKAGE_ARCH_bpp3 = "${MACHINE_ARCH}"

bindir := "${@base_conditional('VELIB_DEFAULT_DIRS', '1', '${bindir}', '${vedir}/${PN}', d)}"

