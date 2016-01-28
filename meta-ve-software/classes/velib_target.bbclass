# makes gmakevelib MACHINE specific. Try to keep this to a minimum,
# because such projects must be compiled again for each MACHINE.
VELIB_TARGET ??= "${MACHINE}"
VELIB_TARGET_bpp3 = "ccgx"
PACKAGE_ARCH = "${MACHINE_ARCH}"
EXTRA_OEMAKE += "TARGET=${VELIB_TARGET}"
