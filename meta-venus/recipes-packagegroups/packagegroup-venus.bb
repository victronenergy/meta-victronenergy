SUMMARY = "Complete venus build per machine without sdk"

DESCRIPTION = " \
	Jethro can only build one MACHINE per bitbake run. For a single MACHINE \
	there typically is either an image to install or an initial image to run \
	from, e.g. a MMC card. \
	\
	Although bitbake accepts multiple recipes, you and up with per MACHINE \
	logic outside of bitbake recipes. This packagegroup tries to eliminate that \
	by providing a single packagroup which builds everything for the MACHINE. \
	\
	It doesn't build the SDK, since that is per (compatible?) ARCH not per \
	MACHINE. So the idea is to bitbake this package group per MACHINE and an SDK \
	per (compatible) ARCH \
	\
	Note: building this recipe takes a considerable amount of time, if you only \
	want a custom rootfs have a look at venus-image / venus-swu. \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"
inherit packagegroup
LICENSE = "MIT"

DEPENDS = "packagegroup-venus-optional-packages"

# installer images
DEPENDS_append_beaglebone += " venus-install-sdcard"
DEPENDS_append_ccgx += "venus-install-sdcard"
DEPENDS_append_canvu500 += "venus-install-sdcard"
DEPENDS_append_nanopi += "venus-install-sdcard"

# "live" initial image
DEPENDS_append_raspberrypi2 += "venus-image"

do_package[depends] =+ "venus-swu:do_swuimage"
