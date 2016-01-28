require venus-install-initramfs.inc

SRC_URI += "file://interfaces"

IMAGE_INSTALL += " \
	busybox-udhcpc \
	ca-certificates \
	netbase \
"

add_program_instructions_append () {
	mkdir -p $D${sysconfdir}/network
	install -m 644 ${WORKDIR}/interfaces $D${sysconfdir}/network
}
