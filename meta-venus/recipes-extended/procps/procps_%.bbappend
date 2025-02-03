# This package is only installed on a non-busybox version, and will attempt
# to install /etc/sysctl.conf. That file is already installed by sysctl-conf,
# so remove it here.

do_install:append() {
	rm  ${D}${sysconfdir}/sysctl.conf
}
