# Add the custom volatiles for all machines except bpp3
FOO = "${THISDIR}/files:"
FOO_bpp3 = ""
FOO_ccgx = ""
FILESEXTRAPATHS_prepend := "${FOO}"

VELIB_DEFAULT_DIRS = "1"
inherit ve_package

do_install_append() {
	echo RANDOM_SEED_FILE=${permanentlocalstatedir}/lib/random-seed \
		>${D}${sysconfdir}/default/urandom
}
