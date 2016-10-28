SUMMARY = "Open source MQTT v3.1 implemention"
DESCRIPTION = "Mosquitto is an open source (BSD licensed) message broker that implements the MQ Telemetry Transport protocol version 3.1. MQTT provides a lightweight method of carrying out messaging using a publish/subscribe model. "
HOMEPAGE = "http://mosquitto.org/"
SECTION = "console/network"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=62ddc846179e908dc0c8efec4a42ef20"

DEPENDS = "openssl util-linux python"

PR = "r7"

SRC_URI = " \
	http://mosquitto.org/files/source/mosquitto-${PV}.tar.gz \
	file://build.patch \
	file://mosquitto.conf \
	file://mosquitto.service \
"

export LIB_SUFFIX="${@d.getVar('baselib', True).replace('lib', '')}"

SRC_URI[md5sum] = "cd879f5964311501ba8e2275add71484"
SRC_URI[sha256sum] = "591f3adcb6ed92c01f7ace1c878af728b797fe836892535620aa6106f42dbcc6"

inherit useradd
USERADD_PACKAGES = "mosquitto"
USERADD_PARAM_mosquitto = "-u 98 -d /nonexistant -r -s /bin/false mosquitto"

do_compile() {
    oe_runmake PREFIX=/usr
}

MOSQUITTO_D = "/data/conf/mosquitto.d"

do_install() {
	oe_runmake install DESTDIR=${D}
	install -d ${D}${libdir}
	install -m 0644 lib/libmosquitto.a ${D}${libdir}/

	#install -d ${D}${systemd_unitdir}/system/
	#install -m 0644 ${WORKDIR}/mosquitto.service ${D}${systemd_unitdir}/system/

	install -m 0644 ${WORKDIR}/mosquitto.conf ${D}${sysconfdir}/mosquitto
	echo "include_dir ${MOSQUITTO_D}" >> ${D}${sysconfdir}/mosquitto/mosquitto.conf
}

PACKAGES += "libmosquitto1 libmosquittopp1 ${PN}-clients ${PN}-python"

FILES_${PN} = "${sbindir}/mosquitto \
               ${bindir}/mosquitto_passwd \
               ${sysconfdir}/mosquitto \
               ${systemd_unitdir}/system/mosquitto.service \
"

FILES_libmosquitto1 = "${libdir}/libmosquitto.so.1"

FILES_libmosquittopp1 = "${libdir}/libmosquittopp.so.1"

FILES_${PN}-clients = "${bindir}/mosquitto_pub \
                       ${bindir}/mosquitto_sub \
"

FILES_${PN}-staticdev += "${libdir}/libmosquitto.a"

FILES_${PN}-python = "/usr/lib/python2.7/site-packages"

DAEMONTOOLS_SERVICE_DIR = "${sysconfdir}/mosquitto/service"
DAEMONTOOLS_SCRIPT = "mkdir -p ${MOSQUITTO_D} && exec softlimit -d 100000000 -s 1000000 -a 100000000 ${sbindir}/mosquitto -c ${sysconfdir}/mosquitto/mosquitto.conf"
DAEMONTOOLS_DOWN = "1"

#inherit systemd
inherit daemontools

SYSTEMD_SERVICE_${PN} = "mosquitto.service"
