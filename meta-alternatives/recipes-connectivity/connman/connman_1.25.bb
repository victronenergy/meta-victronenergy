require connman.inc

SRC_URI  = "https://www.kernel.org/pub/linux/network/connman/connman-${PV}.tar.xz \
            file://0001-ntp-max-poll-set-to-16.patch \
            file://0001-Add-set-hwclock-when-time-is-decoded.patch \
            file://gweb-multi-header-workaround.patch \
            file://main.conf \
            file://connman \
            file://connmand-watch.sh \
            file://connman_IgnoreInvalidKey_Arfailure.patch \
           "

SRC_URI[md5sum] = "a449d2e49871494506e48765747e6624"
SRC_URI[sha256sum] = "c1d266d6be18d2f66231f3537a7ed17b57637ca43c27328bc13c508cbeacce6e"

PR = "${INC_PR}.1"

do_install_append() {
	install -d ${D}${sysconfdir}/connman
	install -m 0644 ${WORKDIR}/main.conf ${D}${sysconfdir}/connman/main.conf
}

pkg_postinst_${PN}() {
	if test "x$D" == "x"; then
		# Remove ntp cronjob
		echo "Removing ntpdate-sync cronjob"
		croncmd="/usr/bin/ntpdate-sync"
		( crontab -l | grep -v "$croncmd" ) | crontab -
	fi
}

