DESCRIPTION = "packages for the SDK"
LICENSE = "MIT"

PR = "r1"

inherit packagegroup

RDEPENDS_packagegroup-sdk-packages += " \
	boost-staticdev \
	lua-staticdev \
	mtd-utils-staticdev \
	packagegroup-core-standalone-sdk-target \
	packagegroup-venus-base-dev \
	qt4-embedded-mkspecs \
	u-boot-env-tools-staticdev \
"

# qtcreator loads python files to gdb needing these..
RDEPENDS_packagegroup-sdk-packages += " \
	python-importlib \
	python-json \
"
