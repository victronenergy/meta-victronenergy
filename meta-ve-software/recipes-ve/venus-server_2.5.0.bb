SUMMARY = "server to extract data from VE systems via MQTT and save in a DB" 
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=256e550bc3ed3651fbbe7ac4cac3b8f2"

RDEPENDS:${PN} += "bash nodejs-npm util-linux-setpriv"

SRC_URI = " \
    gitsm://github.com/mman/venus-docker-grafana-images.git;branch=venus-grafana-server-2.5;protocol=https \
    file://npm-shrinkwrap.json;subdir=${S} \
    file://config.json \
    file://prepare-venus-server.sh \
"
#    gitsm://github.com/victronenergy/venus-docker-grafana-images;branch=master;protcol=https;tag=v${PV} 
# Careful! When updating the version, also npm-shrinkwrap.json must be updated
SRCREV = "35551b5c7962404b8d7439f9d5a3a4a479402b91"
S = "${WORKDIR}/git"

inherit daemontools npm-online-install useradd

DAEMONTOOLS_SCRIPT = "${bindir}/prepare-venus-server.sh && exec setpriv --init-groups --reuid vserver --regid vserver ${bindir}/venus-grafana-server -c /data/conf/venus-server"
DAEMONTOOLS_DOWN = "1"

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "-d /data/conf/venus-server -r -p '*' -s /bin/false -G vserver vserver"

DEFAULTS = "${D}${nonarch_libdir}/node_modules/${PN}/defaults"
PACKAGE = "venus-docker-grafana-server"

do_compile() {
    export HOME=${WORKDIR}

    if [ ! -f ${S}/npm-shrinkwrap.json ]; then
        bbfatal "No npm-shrinkwrap.json found for ${PN}"
    fi
    
    npm install \
        --prefix="${S}" \
        --arch=${NPM_ARCH} \
        --target_arch=${NPM_ARCH} \
        ${S}

    tar=$(npm pack ${S})

    npm install \
        --global \
        --prefix="${NPM_BUILD}" \
        --arch=${NPM_ARCH} \
        --target_arch=${NPM_ARCH} \
        --production \
        "$tar"
}


do_install:append() {
#    rm -rf ${D}${nonarch_libdir}/node_modules/${PACKAGE}/config
#    rm ${D}${nonarch_libdir}/node_modules/${PACKAGE}/docker-compose-dev.yaml 
#    rm ${D}${nonarch_libdir}/node_modules/${PACKAGE}/docker-compose-playground.yaml
#    rm -rf ${D}${nonarch_libdir}/node_modules/${PACKAGE}/docker-grafana
#    rm -rf ${D}${nonarch_libdir}/node_modules/${PACKAGE}/docker-server
#    rm -rf ${D}${nonarch_libdir}/node_modules/${PACKAGE}/docker-upnp

#    cp -r ${S}/dist ${D}${nonarch_libdir}/node_modules/venus-docker-grafana-server

    install -d ${DEFAULTS}
    install -m 0644 ${WORKDIR}/config.json ${DEFAULTS}

    mkdir -p ${D}${bindir}
    install -m 0755 ${WORKDIR}/prepare-venus-server.sh ${D}${bindir}
}
