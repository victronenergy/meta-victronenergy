FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

VELIB_DEFAULT_DIRS = "1"
inherit ve_package

SRC_URI += "\
	file://static-nodes.sh \
	file://test-data-partition.sh \
"

SRC_URI_append_ccgx = "\
	file://usbcheck.sh \
"

RDEPENDS_${PN} += "is-ro-partition u-boot-fw-utils"

do_install_append() {
	echo RANDOM_SEED_FILE=${permanentlocalstatedir}/lib/random-seed \
		>${D}${sysconfdir}/default/urandom

	install -m 0755 ${WORKDIR}/static-nodes.sh ${D}${sysconfdir}/init.d
	update-rc.d -r ${D} static-nodes.sh start 20 S .

	install -m 0755 ${WORKDIR}/test-data-partition.sh ${D}${sysconfdir}/init.d
	update-rc.d -r ${D} test-data-partition.sh start 03 S .
}

do_install_append_ccgx() {
	install -m 0755 ${WORKDIR}/usbcheck.sh ${D}${sysconfdir}/init.d
	update-rc.d -r ${D} usbcheck.sh start 02 S .
}
