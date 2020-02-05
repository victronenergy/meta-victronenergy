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

inherit nopackages packagegroup

DEPENDS = "packagegroup-venus-optional-packages venus-swu"

# large image
DEPENDS:append:beaglebone += "venus-swu-large"
DEPENDS:append:sunxi += "venus-swu-large"
DEPENDS:append:raspberrypi2 += "venus-swu-large"
DEPENDS:append:raspberrypi4 += "venus-swu-large"

# installer images
DEPENDS:append:beaglebone = " venus-install-sdcard"
DEPENDS:append:ccgx = " venus-install-sdcard"
DEPENDS:append:canvu500 = " venus-install-sdcard venus-swu-1"
DEPENDS:append:sunxi = " venus-install-sdcard"

# "live" initial image
DEPENDS:append:rpi = " venus-image"

python () {
    deps = d.getVar('DEPENDS').split()
    for pkg in deps:
        if pkg.startswith('venus-swu'):
            d.appendVarFlag('do_package', 'depends', ' %s:do_swuimage' % pkg)
}
