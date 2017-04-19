require u-boot-rpi.inc

DEPENDS += "dtc-native"
do_deploy[depends] += "bcm2835-bootfiles:do_deploy"

SRC_URI += " \
    file://uEnv.txt \
    file://config.txt \
"

COMPATIBLE_MACHINE = "raspberrypi"

# This revision corresponds to the tag "v2017.01"
# We use the revision in order to avoid having to fetch it from the
# repo during parse
SRCREV = "a705ebc81b7f91bbd0ef7c634284208342901149"

# Install required file for Raspberry Pi bootloader, to indicate that it should
# load u-boot.
do_deploy_append() {
	install ${WORKDIR}/config.txt ${DEPLOYDIR}/config.txt
	${S}/tools/mkenvimage -s 16384 -o ${DEPLOYDIR}/uboot.env ${WORKDIR}/uEnv.txt

	# Also deploy a symlink to make it easier to build a boot image later.
	install -d ${DEPLOYDIR}/boot
	ln -sf ../config.txt ${DEPLOYDIR}/boot
	ln -sf ../u-boot.bin ${DEPLOYDIR}/boot
	ln -sf ../uEnv.txt ${DEPLOYDIR}/boot
	ln -sf ../uboot.env ${DEPLOYDIR}/boot
}
