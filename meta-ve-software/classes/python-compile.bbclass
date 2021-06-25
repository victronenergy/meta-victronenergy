DEPENDS += "python-native"

do_install_append() {
    case "${RDEPENDS_${PN}}" in
      *python3-core*)
        # force python3, OE lacks a python symlink
        find ${D} -exec sed -i -e 's,#!\s\?/usr/bin/python$,#!/usr/bin/env python3,' -e 's,#!\s\?/usr/bin/env python$,#!/usr/bin/python3,' {} \;
        ;;
    esac

    python -m compileall ${D}
}
