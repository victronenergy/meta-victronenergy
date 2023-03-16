DESCRIPTION = "InfluxDB"
SUMMARY = "InfluxDB is a time series database designed to handle high write and query loads."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=f39a8d10930fb37bd59adabb3b9d0bd6"

RDEPENDS:${PN} += "bash"
RDEPENDS:${PN}-dev += "bash"

GO_IMPORT = "github.com/influxdata/influxdb"

GO_INSTALL = "\
    ${GO_IMPORT}/cmd/influx \
    ${GO_IMPORT}/cmd/influxd \
"

SRC_URI = "\
   git://${GO_IMPORT};protocol=https;branch=1.8;destsuffix=${BPN}-${PV}/src/${GO_IMPORT} \
   file://influxdb.conf \
   file://prepare-influxdb.sh \
"
SRCREV = "688e697c51fd5353725da078555adbeff0363d01"

inherit daemontools useradd go-mod pkgconfig

# Workaround for network access issue during compile step
# this needs to be fixed in the recipes buildsystem to move
# this such that it can be accomplished during do_fetch task
do_compile[network] = "1"

DAEMONTOOLS_SCRIPT = "export HOME=/data/home/influxdb && ${bindir}/prepare-influxdb.sh && exec setpriv --init-groups --reuid influxdb --regid influxdb ${bindir}/influxd"
DAEMONTOOLS_DOWN = "1"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "-d /data/home/influxdb -r -p '*' -s /bin/false -G influxdb influxdb"

do_install:prepend() {
    rm ${B}/src/${GO_IMPORT}/build.py
    rm ${B}/src/${GO_IMPORT}/build.sh
    rm ${B}/src/${GO_IMPORT}/Dockerfile*
    sed -i -e "s#usr/bin/sh#bin/sh#g" ${B}/src/${GO_IMPORT}/scripts/ci/run_perftest.sh
}

do_install:append() {
    # /etc
    install -d ${D}${sysconfdir}/influxdb
    install -m 0644 ${WORKDIR}/influxdb.conf ${D}${sysconfdir}/influxdb/

    # /usr/bin
    mkdir -p ${D}${bindir}
    install -m 0755 ${WORKDIR}/prepare-influxdb.sh ${D}${bindir}/
}
