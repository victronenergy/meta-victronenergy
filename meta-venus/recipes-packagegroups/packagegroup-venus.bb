SUMMARY = "Complete venus build per machine without sdk"

PACKAGE_ARCH = "${MACHINE_ARCH}"
LICENSE = "MIT"

inherit nopackages

DEPENDS = "packagegroup-venus-optional-packages venus-swu"

# large image
DEPENDS:append:beaglebone = " venus-swu-large"
DEPENDS:append:sunxi = " venus-swu-large"
DEPENDS:append:raspberrypi2 = " venus-swu-large"
DEPENDS:append:raspberrypi4 = " venus-swu-large"

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
            d.appendVarFlag('do_prepare_recipe_sysroot', 'depends', ' %s:do_swuimage' % pkg)
}
