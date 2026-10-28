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
SRC_URI[sha256sum] = "0bd4be13d1f73d4cc50db7c24f2041db78ff9d5348bdd024cb5844f447d9e085"

S = "${UNPACKDIR}/npm"

inherit npm-online-install
