SUMMARY = "Complete venus build per machine without sdk"

PACKAGE_ARCH = "${MACHINE_ARCH}"
LICENSE = "MIT"
INHIBIT_DEFAULT_DEPS = "1"

# Note: when using existing tasks OE will typically complain with one of these, so
# all normal tasks are disabled and a custom task is added which does nothing,
# besides triggering that the DEPENDS for the machine in question are build.
#
# WARNING: packagegroup-venus-1.0-r0 do_package:Manifest sstate-control/manifest-x86_64_x86_64-nativesdk-venus-swu-3.packagedata
# not found in raspberrypi2 cortexa7hf-neon-vfpv4 cortexa7hf-neon cortexa7hf-vfp armv7vehf-neon-vfpv4 armv7vehf-neon armv7vehf-vfp
# armv7ahf-vfp armv6hf-vfp armv5ehf-vfp armv5hf-vfp allarch x86_64_x86_64-nativesdk (variant '')?
#
# ERROR: packagegroup-venus-1.0-r0 do_prepare_recipe_sysroot: The file /lib/firmware/cypress/cyfmac43430-sdio.clm_blob is
# installed by both linux-firmware and linux-firmware-rpidistro, aborting
#
# Note: the file should actually be renamed, since it is no packagegroup anymore.

inherit nopackages

deltask do_populate_lic
deltask do_fetch
deltask do_unpack
deltask do_patch
deltask do_configure
deltask do_compile
deltask do_install
deltask do_populate_sysroot
deltask do_package

DEPENDS = "packagegroup-venus-optional-packages venus-swu"

# large image
DEPENDS:append:beaglebone = " venus-swu-large"
DEPENDS:append:sunxi = " venus-swu-large"
DEPENDS:append:raspberrypi2 = " venus-swu-large"
DEPENDS:append:raspberrypi4 = " venus-swu-large"
DEPENDS:append:raspberrypi5 = " venus-swu-large"

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
            d.appendVarFlag('do_build_venus', 'depends', ' %s:do_swuimage' % pkg)
}

addtask build_venus before do_build
do_build_venus[noexec] = "1"
do_build_venus() {
    :
}
