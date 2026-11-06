FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI:append:class-target = " file://npmrc"

# cctest and embedtest are test programs that most targets will never use,
# and linking them, at the same time as node and node_mksnapshot, takes
# the most memory of the whole build. Have gyp generate only node and what
# it needs.
PACKAGECONFIG[node-only] = "--root-target=node,,"
# the nodejs ptest needs cctest
PACKAGECONFIG:append = "${@bb.utils.contains('PTEST_ENABLED', '1', '', ' node-only', d)}"

# A compile job takes about 1.7 GB on average; the link of node about
# 10 GB on its own. Run one make job per 2.5 GB of RAM, which leaves room
# for whatever else runs on the host, and at most one per core.
# Set PARALLEL_MAKE:pn-nodejs in local.conf to override.
PARALLEL_MAKE = "-j ${@max(1, min(oe.utils.cpu_count(), int(open('/proc/meminfo').readline().split()[1]) * 2 // (5 * 1024 * 1024)))} -l ${@oe.utils.cpu_count()}"

# Unless DEBUG_BUILD is enabled, pass -g1: debug info without local
# variables and types, which makes the build faster and use less memory.
DEBUG_FLAGS:append = "${@oe.utils.vartrue('DEBUG_BUILD', '', ' -g1', d)}"

# npm reads its builtin configuration from <npm>/npmrc, below the global
# (/usr/etc/npmrc) and user (~/.npmrc) configuration.
do_install:append:class-target() {
    install -m 0644 ${UNPACKDIR}/npmrc ${D}${nonarch_libdir}/node_modules/npm/npmrc
}

FILES:${PN}-npm += "${nonarch_libdir}/node_modules/npm/npmrc"
