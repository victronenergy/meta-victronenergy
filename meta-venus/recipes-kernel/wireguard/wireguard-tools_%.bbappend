# The wireguard module is included in mailine linux nowadays;
# the out of tree module no longer compiles..
DEPENDS:remove = "wireguard-module"
RDEPENDS:${PN}:remove = "wireguard-module"

