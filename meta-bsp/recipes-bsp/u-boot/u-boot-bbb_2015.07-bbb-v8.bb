require u-boot.inc

LICENSE = "GPL-2.0-or-later"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=0507cd7da8e7ad6d6701926ec9b84c95"

COMPATIBLE_MACHINE = "beaglebone"

DEPENDS += "bc-native u-boot-mkimage-native"

S = "${WORKDIR}/u-boot-${PV}"

SRC_URI = " \
    https://github.com/victronenergy/u-boot/archive/v${PV}.tar.gz \
    file://uEnv.txt \
    file://install.cmds \
"
SRC_URI[sha256sum] = "35f2d883ec2a0f839811fc97a735e087e626370bca41ba0c0e4f97a46adbc626"

do_compile:append () {
    mkimage -A arm -T script -C none -n 'Install Script' \
        -d ${WORKDIR}/install.cmds ${WORKDIR}/install.scr
}

do_deploy:append () {
    install -d ${DEPLOYDIR}
    install -m 0644 ${WORKDIR}/uEnv.txt ${DEPLOYDIR}/uEnv.txt
    install -m 0644 ${WORKDIR}/install.scr ${DEPLOYDIR}/install.scr
}
