FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

VELIB_DEFAULT_DIRS = "1"
inherit ve_package

SRC_URI += "\
	file://static-nodes.sh \
	file://test-data-partition.sh \
	file://report-data-failure.sh \
	file://update-data.sh \
"

SRC_URI_append_ccgx = "\
	file://usbcheck.sh \
"

RDEPENDS_${PN} += "curl is-ro-partition"

do_install_append() {
	echo RANDOM_SEED_FILE=${permanentlocalstatedir}/lib/random-seed \
		>${D}${sysconfdir}/default/urandom

	echo TIMESTAMP_FILE=${permanentdir}/etc/timestamp \
		>${D}${sysconfdir}/default/timestamp

	install -m 0755 ${WORKDIR}/static-nodes.sh ${D}${sysconfdir}/init.d
	update-rc.d -r ${D} static-nodes.sh start 20 S .

	install -m 0755 ${WORKDIR}/test-data-partition.sh ${D}${sysconfdir}/init.d
	update-rc.d -r ${D} test-data-partition.sh start 03 S .

	install -m 0755 ${WORKDIR}/report-data-failure.sh ${D}${sysconfdir}/init.d
	install -m 0755 ${WORKDIR}/update-data.sh ${D}${sysconfdir}/init.d

	update-rc.d -r ${D} update-data.sh start 30 5 .
	update-rc.d -r ${D} report-data-failure.sh start 82 5 .

	rm ${D}${sysconfdir}/init.d/banner.sh
	rm ${D}${sysconfdir}/rc*.d/*banner.sh
}

do_install_append_ccgx() {
	install -m 0755 ${WORKDIR}/usbcheck.sh ${D}${sysconfdir}/init.d
	update-rc.d -r ${D} usbcheck.sh start 02 S .
}
