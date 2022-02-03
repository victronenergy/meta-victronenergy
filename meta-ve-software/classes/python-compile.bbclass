DEPENDS += "python3-native"

do_install:append() {
    nativepython3 -m compileall ${D}
}
