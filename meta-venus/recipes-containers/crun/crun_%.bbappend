REQUIRED_DISTRO_FEATURES:remove = "systemd"
DEPENDS:remove = "systemd"
EXTRA_OECONF:append = " --disable-systemd"
