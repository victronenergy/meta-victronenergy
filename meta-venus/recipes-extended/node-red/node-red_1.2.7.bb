DESCRIPTION = "Node-RED"
HOMEPAGE = "http://nodered.org"
LICENSE = "Apache-2.0"
LIC_FILES_CHKSUM = "file://LICENSE;md5=d6f37569f5013072e9490d2194d10ae6"

DEPENDS = "nodejs-native"

RDEPENDS_${PN} += "\
	nodejs (>= 4.0.0) \
"

PR = "r0"

SRC_URI = "\
	https://registry.npmjs.org/${PN}/-/${PN}-${PV}.tgz;unpack=0 \
	file://node-red.service \
"

SRC_URI[md5sum] = "9e4e183b252c89059035394cc172b1bf"
SRC_URI[sha256sum] = "0565a54a07a769b527b4d0c48e4d5c8dbdf030912ca324c43cc7cdc5394b2871"

S = "${WORKDIR}/${PN}"

def get_nodejs_arch(target_arch, d):
    import re
    if re.match('p(pc|owerpc)(|64)', target_arch): return 'ppc'
    elif re.match('i.86$', target_arch): return 'ia32'
    elif re.match('x86_64$', target_arch): return 'x64'
    elif re.match('arm64$', target_arch): return 'arm'
    return target_arch

NODE_MODULES_DIR = "${prefix}/lib/node_modules/"
NPM_CACHE_DIR ?= "${WORKDIR}/npm_cache"
NPM_REGISTRY ?= "http://registry.npmjs.org/"
NPM_ARCH ?= "${@get_nodejs_arch(d.getVar('TARGET_ARCH'), d)}"
NPM_INSTALL_FLAGS ?= "--production --without-ssl --insecure --no-optional --verbose"

do_compile() {
	export HOME=${WORKDIR}

	# Set and clear cache
	npm set cache ${NPM_CACHE_DIR}
	npm cache clear --force

	# Install
	npm --registry=${NPM_REGISTRY} --arch=${NPM_ARCH} --target_arch=${NPM_ARCH} ${NPM_INSTALL_FLAGS} install
}

do_install() {
	export HOME=${WORKDIR}

	# Install
	install -d ${D}${NODE_MODULES_DIR}${PN}
	echo ${D}${NODE_MODULES_DIR}${PN}
	ls ${D}${NODE_MODULES_DIR}${PN}
	cp -r ${S}/* ${D}${NODE_MODULES_DIR}${PN}

	# Remove hardware specific files
	rm -rf ${D}${NODE_MODULES_DIR}${PN}/bin
	rm -rf ${D}${NODE_MODULES_DIR}${PN}/nodes/core/hardware

	# Set permissions
	chmod 0755 ${D}${NODE_MODULES_DIR}${PN}/red.js

	# Symlinks
	install -d ${D}${bindir}
	ln -s ${NODE_MODULES_DIR}${PN}/red.js ${D}${bindir}/${PN}

	# Service
	install -d ${D}${systemd_unitdir}/system/
	install -m 0644 ${WORKDIR}/${PN}.service ${D}${systemd_unitdir}/system/
}

inherit systemd

SYSTEMD_AUTO_ENABLE = "enable"
SYSTEMD_SERVICE_${PN} = "${PN}.service"

PACKAGES = "${PN}"

FILES_${PN} += "\
	${NODE_MODULES_DIR} \
	${bindir} \
	${systemd_unitdir} \
"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
