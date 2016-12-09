require linux-ccgx.inc

# Mind it, this recipe is not installed itself but provides kernel-image etc.
# Hence RPEDEND on that one....
RDEPENDS_kernel-image += "linux-backports"
RDEPENDS_kernel-image += "kernel-modules"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "9e66514f7cce9cc6cebe58c647ca8505"
SRC_URI[sha256sum] = "dfa59c8e38b0a1368bc396bd2f8650edf931a29fcf6c64f90026e5de2485b6e9"

# This was introduced to remove uImage from /boot and save 3MB
KERNEL_DROPIMAGE = ""

S = "${WORKDIR}/linux-${PV}"