DESCRIPTION = "packages for the SDK"
LICENSE = "MIT"

PR = "r1"

inherit packagegroup

# note: these end up in the target sysroot of the SDK!
RDEPENDS_packagegroup-sdk-packages += " \
	boost-staticdev \
	lua-staticdev \
	mtd-utils-staticdev \
	packagegroup-core-standalone-sdk-target \
	packagegroup-venus-base-dev \
	qt4-embedded-mkspecs \
"

# swu related builds with NAND env
RDEPENDS_packagegroup-sdk-packages_append_bpp3 += "u-boot-env-tools-staticdev"
RDEPENDS_packagegroup-sdk-packages_append_ccgx += "u-boot-env-tools-staticdev"
