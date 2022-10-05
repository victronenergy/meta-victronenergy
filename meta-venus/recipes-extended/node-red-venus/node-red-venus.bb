DESCRIPTION = "Node-RED with venus integration"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

RDEPENDS:${PN} += "bash node-red nginx node-red-contrib-victron util-linux-setpriv"

SRC_URI = "\
	file://50x-node-red.html \
    file://nginx-rules \
    file://node-red-venus.sh \
    file://prepare-node-red-venus.sh \
    file://user-authentication.js \
    file://venus-settings.js \
"

SRC_URI[sha256sum] = "e840fa1c7d7b25b0565551ad3582e24214cefb772a9af0238a9f7dac94f4dabb"

inherit daemontools useradd www

DAEMONTOOLS_SCRIPT = "export HOME=/data/home/nodered && ${bindir}/prepare-node-red-venus.sh && exec setpriv --init-groups --reuid nodered --regid nodered ${bindir}/node-red-venus.sh"
DAEMONTOOLS_DOWN = "1"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "-d /data/home/nodered -r -p '*' -s /bin/false -G dialout nodered"

# Note: installed in node-red otherwise more copies of the dependencies need to be installed.
do_install:append() {
    install -d ${D}${nonarch_libdir}/node_modules/node-red
    install -m 0644 ${WORKDIR}/venus-settings.js ${D}${nonarch_libdir}/node_modules/node-red
    install -m 0644 ${WORKDIR}/user-authentication.js ${D}${nonarch_libdir}/node_modules/node-red

    # Startup script
    mkdir -p ${D}${bindir}
    install -m 0755 ${WORKDIR}/node-red-venus.sh ${D}${bindir}
    install -m 0755 ${WORKDIR}/prepare-node-red-venus.sh ${D}${bindir}

    # https support
    install -d ${D}${sysconfdir}/nginx/sites-available
    install ${WORKDIR}/nginx-rules ${D}${sysconfdir}/nginx/sites-available/node-red
    install -d ${D}${sysconfdir}/nginx/sites-enabled
    ln -s ${sysconfdir}/nginx/sites-available/node-red ${D}${sysconfdir}/nginx/sites-enabled

    install -d ${D}${WWW_LOCALHOST}/html
    install ${WORKDIR}/50x-node-red.html ${D}${WWW_LOCALHOST}/html
}

FILES:${PN} += "${nonarch_libdir}/node_modules/node-red"
