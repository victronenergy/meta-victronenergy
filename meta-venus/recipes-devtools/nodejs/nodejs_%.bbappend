FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:class-target = " file://npmrc"

# Unless DEBUG_BUILD is enabled, pass -g1: debug info without local
# variables and types, which makes the build faster and use less memory.
DEBUG_FLAGS:append = "${@oe.utils.vartrue('DEBUG_BUILD', '', ' -g1', d)}"

# npm reads its builtin configuration from <npm>/npmrc, below the global
# (/usr/etc/npmrc) and user (~/.npmrc) configuration.
do_install:append:class-target() {
    install -m 0644 ${UNPACKDIR}/npmrc ${D}${nonarch_libdir}/node_modules/npm/npmrc
}

FILES:${PN}-npm += "${nonarch_libdir}/node_modules/npm/npmrc"
