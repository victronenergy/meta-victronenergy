PR = "r0"
LICENSE = "MIT"

DESCRIPTION = " \
	This can be moved to packagegroup-venus-machine. \
	It only adds imx-kobs, which is needed to program the nand on the imx. \
	It also has a emmc chip, so beter test and use that one instead. \
"

inherit packagegroup

PACKAGES = "\
         packagegroup-custom-image-achilles \
         "

RDEPENDS_packagegroup-custom-image-achilles = "\
	packagegroup-core-boot \
	packagegroup-venus-base \
	imx-kobs \
"

