SUMMARY = "Persistent extension identity library and command-line tools"
DESCRIPTION = "Shared Python implementation for allocating, validating, and \
publishing persistent post-image Venus OS identities."
HOMEPAGE = "https://github.com/nmbath/venus-identities"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=33303c0aa278f183b10d87e9a89b0b88"

# The private source repository is fetched with the build host's dedicated
# read-only deploy key. github.com-venus-identities is an SSH config alias;
# builds on another host must provide an equivalent alias and credential.
VENUS_IDENTITIES_SRC_URI ?= "gitsm://github.com-venus-identities/nmbath/venus-identities.git;branch=main;protocol=ssh;user=git"
SRC_URI = "${VENUS_IDENTITIES_SRC_URI}"
SRCREV = "48a3716d795fc16d633802940f5c523cda2a3293"
S = "${WORKDIR}/git"

inherit python_setuptools_build_meta

RDEPENDS:${PN} += " \
    python3-core \
    python3-crypt \
    python3-fcntl \
"

IDENTITIES_DIR = "/opt/victronenergy/venus-identities"

do_install:append() {
    install -d ${D}${IDENTITIES_DIR}
    install -m 0755 ${D}${bindir}/venus-user \
        ${D}${IDENTITIES_DIR}/venus-user
    install -m 0755 ${D}${bindir}/venus-identities-publish \
        ${D}${IDENTITIES_DIR}/venus-identities-publish
    rm ${D}${bindir}/venus-user ${D}${bindir}/venus-identities-publish
    rmdir ${D}${bindir}

    install -d ${D}${sbindir}
    ln -s ${IDENTITIES_DIR}/venus-user ${D}${sbindir}/venus-user
    ln -s ${IDENTITIES_DIR}/venus-user ${D}${sbindir}/venus-user-add
    ln -s ${IDENTITIES_DIR}/venus-user ${D}${sbindir}/venus-user-check
    ln -s ${IDENTITIES_DIR}/venus-user ${D}${sbindir}/venus-user-remove
}

FILES:${PN} += " \
    ${IDENTITIES_DIR} \
    ${sbindir}/venus-user \
    ${sbindir}/venus-user-add \
    ${sbindir}/venus-user-check \
    ${sbindir}/venus-user-remove \
"
