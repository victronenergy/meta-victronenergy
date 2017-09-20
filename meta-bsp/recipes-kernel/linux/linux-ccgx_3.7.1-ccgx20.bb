require linux-ccgx.inc

# Mind it, this recipe is not installed itself but provides kernel-image etc.
# Hence RPEDEND on that one....
RDEPENDS_kernel-image += "linux-backports"
RDEPENDS_kernel-image += "kernel-modules"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "868ad1c52f8aa34933bce5a7b81bb5e0"
SRC_URI[sha256sum] = "cb2c0908a2af0f0e03c84a3058bb1fbbe6d1a260775765a448313b21ecbf5e6b"

# This was introduced to remove uImage from /boot and save 3MB
KERNEL_DROPIMAGE = "1"

S = "${WORKDIR}/linux-${PV}"
