SUMMARY = "Node-RED nodes for the S2 energy management protocol"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=876b44dded698736d08769eeb7c62258"

# The npm registry tarball contains the compiled dist/, the GitHub tag tarball does not.
SRC_URI = " \
    npm://registry.npmjs.org;package=${BPN};version=${PV} \
    file://remove-prepack-for-venus.patch \
    file://npm-shrinkwrap.json;subdir=${S} \
"

# Careful! When updating the version, also npm-shrinkwrap.json must be updated
SRC_URI[sha256sum] = "9ba88e98296fbc370a22be9cd3fa44d6543c9afaccc04c375d3e5c98e838a107"

S = "${UNPACKDIR}/npm"

inherit npm-online-install
