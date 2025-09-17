FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

VELIB_DEFAULT_DIRS = "1"
inherit ve_package

SRC_URI += "\
    file://overlays.sh \
    file://static-nodes.sh \
    file://test-data-partition.sh \
    file://report-data-failure.sh \
    file://update-data.sh \
    file://clean-data.sh \
    file://usb.rules \
"

SRC_URI:append:ccgx = "\
    file://usbcheck.sh \
"

RDEPENDS:${PN} += "curl is-ro-partition"

do_install:append() {
    echo RANDOM_SEED_FILE=${permanentlocalstatedir}/lib/random-seed \
        >${D}${sysconfdir}/default/urandom

    echo TIMESTAMP_FILE=${permanentdir}/etc/timestamp \
        >${D}${sysconfdir}/default/timestamp

    install -m 0755 ${UNPACKDIR}/static-nodes.sh ${D}${sysconfdir}/init.d
    install -m 0755 ${UNPACKDIR}/test-data-partition.sh ${D}${sysconfdir}/init.d
    install -m 0755 ${UNPACKDIR}/overlays.sh ${D}${sysconfdir}/init.d
    install -m 0755 ${UNPACKDIR}/clean-data.sh ${D}${sysconfdir}/init.d

    update-rc.d -r ${D} overlays.sh start 03 S .
    update-rc.d -r ${D} static-nodes.sh start 20 S .
    update-rc.d -r ${D} test-data-partition.sh start 03 S .
    update-rc.d -r ${D} clean-data.sh start 30 S .

    install -m 0755 ${UNPACKDIR}/report-data-failure.sh ${D}${sysconfdir}/init.d
    install -m 0755 ${UNPACKDIR}/update-data.sh ${D}${sysconfdir}/init.d

    install -m 0644 ${UNPACKDIR}/usb.rules ${D}/${sysconfdir}/udev/rules.d

    update-rc.d -r ${D} update-data.sh start 30 5 .
    update-rc.d -r ${D} report-data-failure.sh start 82 5 .

    rm ${D}${sysconfdir}/init.d/banner.sh
    rm ${D}${sysconfdir}/rc*.d/*banner.sh
}

do_install:append:ccgx() {
    install -m 0755 ${UNPACKDIR}/usbcheck.sh ${D}${sysconfdir}/init.d
    update-rc.d -r ${D} usbcheck.sh start 02 S .
}
