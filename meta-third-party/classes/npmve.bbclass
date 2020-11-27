DEPENDS_prepend = "nodejs-native "
RDEPENDS_${PN}_prepend = "nodejs "

# function maps arch names to npm arch names
def npm_oe_arch_map(target_arch, d):
    import re
    if   re.match('p(pc|owerpc)(|64)', target_arch): return 'ppc'
    elif re.match('i.86$', target_arch): return 'ia32'
    elif re.match('x86_64$', target_arch): return 'x64'
    elif re.match('arm64$', target_arch): return 'arm'
    return target_arch

NPM_ARCH ?= "${@npm_oe_arch_map(d.getVar('TARGET_ARCH'), d)}"
NPM_INSTALL_DEV ?= "0"
NPM_ORG ?= ""
NPM_ARGS ?= ""
NPM_INSTALL_IN_PACKAGE ?= ""

def get_org(prefix, org, posfix):
      return '' if org == '' else prefix + org + posfix
      
NPM_FILES_PREFIX="${@get_org('@', d.getVar('NPM_ORG'), '/')}"
NPM_PACK_PREFIX="${@get_org('', d.getVar('NPM_ORG'), '-')}"

npmve_do_compile() {
	# changing the home directory to the working directory, the .npmrc will
	# be created in this directory
	export HOME=${WORKDIR}
	if [  "${NPM_INSTALL_DEV}" = "1" ]; then
		npm config set dev true
	else
		npm config set dev false
	fi
	npm set cache ${WORKDIR}/npm_cache
	# clear cache before every build
	npm cache clear --force

	if [ -f ${WORKDIR}/npm-shrinkwrap.json ]; then
	  cp ${WORKDIR}/npm-shrinkwrap.json .
	fi
	
	npm pack .
}

npmve_do_install() {
	# changing the home directory to the working directory, the .npmrc will
	# be created in this directory
	export HOME=${WORKDIR}
	if [ "${NPM_INSTALL_IN_PACKAGE}" = "" ]; then
		npm install --prefix ${D}${prefix} -g --arch=${NPM_ARCH} --target_arch=${NPM_ARCH} --production --no-bin-links ${NPM_ARGS} ${NPM_PACK_PREFIX}${PN}-${PV}.tgz
	else
		(cd ${NPM_INSTALL_IN_PACKAGE}; npm install --arch=${NPM_ARCH} --target_arch=${NPM_ARCH} --production --no-bin-links ${NPM_ARGS} ${S}/${NPM_PACK_PREFIX}${PN}-${PV}.tgz)
	fi
}

do_unpack_append() {
    import os
    prefix = d.getVar('WORKDIR') + '/' + d.getVar('PN') + '-' + d.getVar('PV')
    os.system('tar xzf ' + prefix + '.tgz' + ' -C ' + prefix + ' --strip-components 1')
}

FILES_${PN} += " \
    ${libdir}/node_modules/${NPM_FILES_PREFIX}${PN} \
"

EXPORT_FUNCTIONS do_compile do_install
