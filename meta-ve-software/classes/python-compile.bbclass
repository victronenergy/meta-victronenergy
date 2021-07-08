DEPENDS += "python3-native"

do_install_append() {
    nativepython3 -m compileall ${D}
}
