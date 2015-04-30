PR = "r0"
LICENSE = "MIT"

inherit packagegroup

PACKAGES = "\
         packagegroup-custom-image-achilles \
         "

RDEPENDS_packagegroup-custom-image-achilles = "\
	packagegroup-core-boot \
	packagegroup-venus-base \
	imx-kobs \
"

