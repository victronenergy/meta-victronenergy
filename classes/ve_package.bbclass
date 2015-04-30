# changes bindir to point to a per package location

bindir := "${@base_conditional('VELIB_DEFAULT_DIRS', '1', '${bindir}', '/opt/victronenergy/${PN}', d)}"
bindir_bpp3 := '${@base_conditional("VELIB_DEFAULT_DIRS", "1", "${bindir}", "/opt/color-control/${PN}", d)}'
