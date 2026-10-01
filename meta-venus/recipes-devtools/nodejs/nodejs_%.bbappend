FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:class-target = " file://npmrc"

# npm reads its builtin configuration from <npm>/npmrc, below the global
# (/usr/etc/npmrc) and user (~/.npmrc) configuration.
do_install:append:class-target() {
    install -m 0644 ${UNPACKDIR}/npmrc ${D}${nonarch_libdir}/node_modules/npm/npmrc
}

FILES:${PN}-npm += "${nonarch_libdir}/node_modules/npm/npmrc"
