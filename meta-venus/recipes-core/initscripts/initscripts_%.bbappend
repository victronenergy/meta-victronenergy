FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

VELIB_DEFAULT_DIRS = "1"
inherit ve_package

do_install_append() {
	echo RANDOM_SEED_FILE=${permanentlocalstatedir}/lib/random-seed \
		>${D}${sysconfdir}/default/urandom
}
