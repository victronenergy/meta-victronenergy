DESCRIPTION = "creates / updates the permanent storage (/data)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

ALLOW_EMPTY = "1"

pkg_postinst_${PN}() {
	if [ "x$D" = "x" ]; then
		# Data should alway be mounted. Create data structure and symlinks
		[ -d /data/conf ] || mkdir -p /data/conf
		[ -d /conf ] || ln -sf /data/conf /conf
		[ -d /data/log ] || mkdir -p /data/log
		[ -d /log ] || ln -sf /data/log /log
		[ -d /data/db ] || mkdir -p /data/db; chown www-data /data/db
		[ -d /db ] || ln -sf /data/db /db
		mkdir -p /data/home/root
		mkdir -p /data/var/lib
		: exit 0
	else
		# Exit 1 is used to set the status of the package on unpacked in rootfs image
		# The result is that the package will be installed on first boot
		exit 1
	fi
}




