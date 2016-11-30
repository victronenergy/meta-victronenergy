DESCRIPTION = "creates / updates the permanent storage (/data)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://volatiles"

do_install() {
	install -d ${D}${sysconfdir}/default/volatiles
	install -m 0644 ${WORKDIR}/volatiles ${D}${sysconfdir}/default/volatiles/30_${PN}

	# All services should use /var/log as their log location. Not /log, and
	# not /data/log. /var/log symlinks to /data/log, and that is taken care
	# of in the bbappend on initscripts.
	# For now, leave /log available. It can be removed once we are sure that
	# nothing writes to /log anymore.
	ln -sf data/log ${D}/log
}

FILES_${PN} = "${sysconfdir} /log"
