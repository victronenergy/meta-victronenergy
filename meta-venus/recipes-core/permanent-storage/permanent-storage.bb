DESCRIPTION = "creates / updates the permanent storage (/data)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

ALLOW_EMPTY_${PN} = "1"

pkg_postinst_${PN}() {
	# Execute on first boot after installing/updating rootfs
	if [ "x$D" = "x" ]; then
		# Data should alway be mounted. Create data structure and symlinks
		[ -d /data/conf ] || mkdir -p /data/conf
		[ -d /data/db ] || mkdir -p /data/db; chown www-data /data/db
		[ -d /data/themes/overlay ] || mkdir -p /data/themes/overlay; chown www-data /data/themes/overlay

		# This is double with the initscripts bbappend (in Jethro), and left here
		# for now for as long as we still use Danny.
		[ -d /data/log ] || mkdir -p /data/log

		# All services should use /var/log as their log location. Not /log, and
		# not /data/log. /var/log symlinks to /data/log, and that is taken care of
		# in the bbappend on initscripts.
		# For now, leave /log available. It can be removed once we are sure that
		# nothing writes to /log anymore.
		[ -d /log ] || ln -sf /data/log /log

		mkdir -p /data/home/root
		mkdir -p /data/home/vnctunnel
		mkdir -p /data/var/lib
		: exit 0
	else
		# Exit 1 is used to set the status of the package on unpacked in rootfs image
		# The result is that the package will be installed on first boot
		exit 1
	fi
}




