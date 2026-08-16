# Venus does not expose Docker compatibility
PACKAGECONFIG:remove = "docker"

# Device-mapper graph driver is already excluded and
# Venus uses dedicated filesystem/overlay storage.
RDEPENDS:${PN}:remove = "libdevmapper"

# Support rootless
PACKAGECONFIG:append = " rootless"
