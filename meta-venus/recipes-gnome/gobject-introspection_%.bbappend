# split the tools and their dependencies in a seperated package
# so they are not installed on the target, not needed anyway.
PACKAGE_BEFORE_PN = "${PN}-bin"

FILES:${PN}-bin = "${bindir} ${libdir}/gobject-introspection/giscanner"
RDEPENDS:${PN} = ""
RDEPENDS:${PN}-bin = "python3-pickle python3-xml python3-setuptools"
RDEPENDS:${PN}:append:class-native = " python3-setuptools-native"

