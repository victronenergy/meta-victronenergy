FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:class-target = " file://npmrc"

# cctest and embedtest are test programs that most targets will never use,
# and linking them, at the same time as node and node_mksnapshot, takes
# the most memory of the whole build. Have gyp generate only node and what
# it needs.
PACKAGECONFIG[node-only] = "--root-target=node,,"
# the nodejs ptest needs cctest
PACKAGECONFIG:append = "${@bb.utils.contains('PTEST_ENABLED', '1', '', ' node-only', d)}"

# npm reads its builtin configuration from <npm>/npmrc, below the global
# (/usr/etc/npmrc) and user (~/.npmrc) configuration.
do_install:append:class-target() {
    install -m 0644 ${UNPACKDIR}/npmrc ${D}${nonarch_libdir}/node_modules/npm/npmrc
}

FILES:${PN}-npm += "${nonarch_libdir}/node_modules/npm/npmrc"
