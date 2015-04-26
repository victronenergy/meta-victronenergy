inherit gmakevelib

# makes gmakevelib MACHINE specific. Try to keep this to a minimum,
# because such projects must be compiled again for each MACHINE.
TARGET="$(MACHINE)"
TARGET_bpp3 = "ccgx"
PACKAGE_ARCH = "${MACHINE_ARCH}"
EXTRA_OEMAKE += "TARGET=${TARGET}"
