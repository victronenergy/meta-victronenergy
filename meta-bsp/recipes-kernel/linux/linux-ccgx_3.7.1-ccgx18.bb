require linux-ccgx.inc

# Mind it, this recipe is not installed itself but provides kernel-image etc.
# Hence RPEDEND on that one....
RDEPENDS_kernel-image += "linux-backports"
RDEPENDS_kernel-image += "kernel-modules"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "3f0c43ee027708c2995a533e0379519d"
SRC_URI[sha256sum] = "2a12d128d78edecae0440bceb4bb528b8d008f567e3a86199426dac6c1a53f17"

# This was introduced to remove uImage from /boot and save 3MB
KERNEL_DROPIMAGE = ""

S = "${WORKDIR}/linux-${PV}"
