inherit pkgconfig

# run ./configure && make && make install

inherit ve_package

DEPENDS += "python3-native"

CFLAGS += "${TOOLCHAIN_OPTIONS} ${TARGET_CC_ARCH} ${LDFLAGS}"

# Note: When building in a subdir of the fetched sources, please set
# VE_PROJECT_DIR to the subdir instead of changing ${S}.
# pseudo can crash otherwise and OE will complain about TMPDIR being
# in the debug files, since only ${S} and ${B} get substituted.

VE_PROJECT_DIR ?= "${S}"
B = "${VE_PROJECT_DIR}"

oe_runconf () {
    cfgscript="${VE_PROJECT_DIR}/configure"
    if [ -x "$cfgscript" ] ; then
        bbnote "Running $cfgscript ${CONFIGUREOPTS} ${EXTRA_OECONF} $@"
        set +e
        cd "${VE_PROJECT_DIR}"
        $cfgscript ${CONFIGUREOPTS} ${EXTRA_OECONF} "$@"
        if [ "$?" != "0" ]; then
            echo "Configure failed. The contents of all config.log files follows to aid debugging"
            find ${S} -name config.log -print -exec cat {} \;
            bbfatal "oe_runconf failed"
        fi
        set -e
    else
        bbfatal "no configure script found at $cfgscript"
    fi
}

gmakevelib_do_configure() {
    if [ -e ${VE_PROJECT_DIR}/configure ]; then
        oe_runconf

        # force python3, OE lacks a python symlink
        velib=$(sed -e 's/VELIB_PATH := //' velib_path.mk)
        find ${VE_PROJECT_DIR}/$velib -name define2make.py -exec sed -i -e 's,#!/usr/bin/env python$,#!/usr/bin/env python3,' {} \;
    else
        bbnote "nothing to configure"
    fi

    # if distclean fails, try clean instead.
    if ! make distclean; then
        make clean
    fi
}

EXTRA_OEMAKE:append = " -C ${B}"

gmakevelib_do_install() {
    oe_runmake install 'DESTDIR=${D}'
}

EXPORT_FUNCTIONS do_configure do_install
