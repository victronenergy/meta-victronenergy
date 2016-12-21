require linux-ccgx.inc

# Mind it, this recipe is not installed itself but provides kernel-image etc.
# Hence RPEDEND on that one....
RDEPENDS_kernel-image += "linux-backports"
RDEPENDS_kernel-image += "kernel-modules"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "ec42ea15cc6004589fc4f57a53dba4da"
SRC_URI[sha256sum] = "01463ee772fd78baaede692561977dbab8bb219290bea0d4d0c4f79bd0f7d7e8"

# This was introduced to remove uImage from /boot and save 3MB
KERNEL_DROPIMAGE = ""

S = "${WORKDIR}/linux-${PV}"
