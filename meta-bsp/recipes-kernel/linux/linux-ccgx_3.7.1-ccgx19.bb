require linux-ccgx.inc

# Mind it, this recipe is not installed itself but provides kernel-image etc.
# Hence RPEDEND on that one....
RDEPENDS_kernel-image += "linux-backports"
RDEPENDS_kernel-image += "kernel-modules"

SRC_URI = "https://github.com/victronenergy/linux/archive/v${PV}.tar.gz"
SRC_URI[md5sum] = "8fe90da914c4748cc6b389a15bf9623e"
SRC_URI[sha256sum] = "74053acb928380c3677c6b704eb823db9338a042cd7a150056df9e7cde7838a6"

# This was introduced to remove uImage from /boot and save 3MB
KERNEL_DROPIMAGE = "1"

S = "${WORKDIR}/linux-${PV}"
