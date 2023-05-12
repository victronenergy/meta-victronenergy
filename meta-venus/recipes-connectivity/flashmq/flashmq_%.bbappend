FILESEXTRAPATHS:prepend := "${THISDIR}/flashmq:"
SRC_URI += "file://flashmq.conf"

inherit daemontools useradd

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "flashmq"
USERADD_PARAM:${PN} = "--no-create-home --shell /bin/false -g flashmq flashmq"

DAEMONTOOLS_RUN = "softlimit -d 100000000 -s 10000000 -a 100000000 setuidgid flashmq ${bindir}/flashmq"

RDEPENDS:${PN} += "dbus-flashmq"

do_install:append() {
	install ${WORKDIR}/flashmq.conf ${D}/${sysconfdir}/flashmq/flashmq.conf
}
