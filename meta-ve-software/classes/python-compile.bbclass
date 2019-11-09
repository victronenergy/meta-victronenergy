DEPENDS += "python-native"

do_install_append() {
    python -m compileall ${D}
}
