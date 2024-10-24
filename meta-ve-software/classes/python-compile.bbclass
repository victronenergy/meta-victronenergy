DEPENDS += "python3-native"

do_install:append() {
	nativepython3 -s -m compileall -d / ${D} -f
}
