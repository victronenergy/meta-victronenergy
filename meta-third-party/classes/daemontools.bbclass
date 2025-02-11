DAEMON_PN ?= "${PN}"

# venus gets daemontools-run-venus and all other distro's daemontools-run
DAEMONTOOLS = "daemontools-run"
DAEMONTOOLS:venus = "daemontools-run-venus"

DAEMONTOOLS_virtclass-cross = ""
DAEMONTOOLS_virtclass-native = ""
DAEMONTOOLS_virtclass-nativesdk = ""

DAEMONTOOLS_SERVICES_DIR ?= "/service"
DAEMONTOOLS_LOG_DIR_PREFIX = "${localstatedir}/log"
DAEMONTOOLS_SERVICE_SYMLINK ?= "1"

DAEMONTOOLS_DOWN ?= ""

# to allow the rootfs to be readonly in venus, /service might be in RAM
DAEMONTOOLS_TMPFS = "0"
DAEMONTOOLS_TMPFS:venus = "1"

DAEMONTOOLS_COMMON_SERVICES_DIR = "/opt/victronenergy/service"
DAEMONTOOLS_SERVICE_DIR ?= "${DAEMONTOOLS_COMMON_SERVICES_DIR}/${PN}"

DAEMONTOOLS_COMMON_TEMPLATES_DIR = "/opt/victronenergy/service-templates"
DAEMONTOOLS_TEMPLATE_CONF_DIR = "${DAEMONTOOLS_COMMON_TEMPLATES_DIR}/conf"
DAEMONTOOLS_TEMPLATE_DIR ?= "${DAEMONTOOLS_COMMON_TEMPLATES_DIR}/${PN}"

python () {
    pkg = d.getVar('DAEMON_PN', True)
    d.appendVar('RDEPENDS:' + pkg, ' ${DAEMONTOOLS}')
    d.appendVar('FILES:' + pkg, ' ${DAEMONTOOLS_SERVICES_DIR} ${DAEMONTOOLS_SERVICE_DIR} ${DAEMONTOOLS_COMMON_TEMPLATES_DIR}')
}

DAEMONTOOLS_preinst() {
    if test "x$D" = "x"; then
        if [ -d ${DAEMONTOOLS_SERVICES_DIR}/${PN} ]; then
            echo "Stopping ${PN}"
            svc -d ${DAEMONTOOLS_SERVICES_DIR}/${PN}
            svc -d ${DAEMONTOOLS_SERVICES_DIR}/${PN}/log
        fi
    fi
}

DAEMONTOOLS_postinst() {
    if test "x$D" = "x"; then
        if [ "${DAEMONTOOLS_TMPFS}" -eq "1" ]; then
            mkdir -p ${DAEMONTOOLS_SERVICES_DIR}/${PN}
            cp -a ${DAEMONTOOLS_SERVICE_DIR}/* ${DAEMONTOOLS_SERVICES_DIR}/${PN}
        fi
    fi
}

DAEMONTOOLS_prerm() {
    if test "x$D" = "x"; then
        echo "Stopping ${PN}"
        svc -d ${DAEMONTOOLS_SERVICES_DIR}/${PN}
        svc -d ${DAEMONTOOLS_SERVICES_DIR}/${PN}/log
    fi
}

# opkg forgets to remove symlinks, dpkg doesn't so check if still there
DAEMONTOOLS_postrm() {
    if [ -d ${DAEMONTOOLS_SERVICES_DIR}/${PN} ]; then
        rm -rf ${DAEMONTOOLS_SERVICES_DIR}/${PN}
    fi
}

def daemontools_after_parse(d):
    if d.getVar('DAEMONTOOLS_SERVICE_DIR', True) == None:
        raise bb.build.FuncFailed("%s inherits daemontools but doesn't set DAEMONTOOLS_SERVICE_DIR" % d.getVar('FILE', True))

python __anonymous() {
    daemontools_after_parse(d)
}

PACKAGESPLITFUNCS:prepend = "populate_packages_daemontools "

# NOTE: since OVERRIDES depends on MACHINEOVERRIDES which depends on MACHINE,
# then all stamps become machine specific and as a consequence any depending
# task as well, so exclude OVERRIDES.
populate_packages_daemontools[vardepsexclude] += "OVERRIDES"

python populate_packages_daemontools () {
    def update_rcd_package(pkg):
        bb.debug(1, 'adding daemontools calls to postinst/postrm for %s' % pkg)
        localdata = bb.data.createCopy(d)
        overrides = localdata.getVar("OVERRIDES", True)
        localdata.setVar("OVERRIDES", "%s:%s" % (pkg, overrides))

        preinst = localdata.getVar('pkg_preinst', True)
        if not preinst:
            preinst = '#!/bin/sh\n'
        preinst += localdata.getVar('DAEMONTOOLS_preinst', True)
        d.setVar('pkg_preinst:%s' % pkg, preinst)

        postinst = localdata.getVar('pkg_postinst', True)
        if not postinst:
            postinst = '#!/bin/sh\n'
        postinst += localdata.getVar('DAEMONTOOLS_postinst', True)
        d.setVar('pkg_postinst:%s' % pkg, postinst)

        prerm = localdata.getVar('pkg_prerm', True)
        if not prerm:
            prerm = '#!/bin/sh\n'
        prerm += localdata.getVar('DAEMONTOOLS_prerm', True)
        d.setVar('pkg_prerm:%s' % pkg, prerm)

        postrm = localdata.getVar('pkg_postrm', True)
        if not postrm:
                postrm = '#!/bin/sh\n'
        postrm += localdata.getVar('DAEMONTOOLS_postrm', True)
        d.setVar('pkg_postrm:%s' % pkg, postrm)

    pkgs = d.getVar('DAEMONTOOLS_SERVICES', True)
    if pkgs == None:
        pkgs = d.getVar('DAEMON_PN', True)
        packages = (d.getVar('PACKAGES', True) or "").split()
        if not pkgs in packages and packages != []:
            pkgs = packages[0]
    for pkg in pkgs.split():
        update_rcd_package(pkg)
}


do_install:append() {
    SERVICE="${D}${DAEMONTOOLS_SERVICE_DIR}"

    install -d ${SERVICE}
    echo "#!/bin/sh" > ${SERVICE}/run
    echo "echo \"*** starting ${PN} ***\"" >> ${SERVICE}/run
    echo "exec 2>&1" >> ${SERVICE}/run
    echo ". ${sysconfdir}/profile.d/profile.sh" >> ${SERVICE}/run

    if [ "x${DAEMONTOOLS_SCRIPT}" = "x" ]; then
        DAEMONTOOLS_SCRIPT="exec ${DAEMONTOOLS_RUN}"
    fi
    echo "${DAEMONTOOLS_SCRIPT}" >> ${SERVICE}/run
    chmod 755 ${SERVICE}/run

    install -d ${SERVICE}/log
    echo "#!/bin/sh" > ${SERVICE}/log/run
    echo "exec 2>&1" >> ${SERVICE}/log/run
    if [ "x${DAEMONTOOLS_LOG_DIR}" = "x" ]; then
        DAEMONTOOLS_LOG_DIR="${DAEMONTOOLS_LOG_DIR_PREFIX}/${PN}"
    fi
    echo "exec multilog t s25000 n4 ${DAEMONTOOLS_LOG_DIR}" >> ${SERVICE}/log/run
    chmod 755 ${SERVICE}/log/run

    if [ "x${DAEMONTOOLS_DOWN}" != "x" ]; then
        touch ${SERVICE}/down
        touch ${SERVICE}/log/down
    fi

    if [ ${DAEMONTOOLS_SERVICE_SYMLINK} = "1" ]; then
        install -d ${D}${DAEMONTOOLS_SERVICES_DIR}
        if [ ${DAEMONTOOLS_TMPFS} = "0" ]; then
            ln -s ${DAEMONTOOLS_SERVICE_DIR} ${D}${DAEMONTOOLS_SERVICES_DIR}/${PN}
        fi
    fi
}


