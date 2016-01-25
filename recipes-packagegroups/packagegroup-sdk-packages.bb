DESCRIPTION = "packages for the SDK"
LICENSE = "MIT"

PR = "r1"

inherit packagegroup

RDEPENDS_${PN} += " \
	boost-staticdev \
	lua-staticdev \
	mtd-utils-staticdev \
	packagegroup-core-standalone-sdk-target \
	packagegroup-venus-base-dev \
	qt4-embedded-mkspecs \
	u-boot-env-tools-staticdev \
"

