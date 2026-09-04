DESCRIPTION = "Confirmation-gated local file import service"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=e32777d272d59cf1a64aed9ee0ad1fc1"

# venus-import is a private repository. The build host provides this SSH
# alias with a dedicated read-only deploy key, matching the other private
# Venus services.
VENUS_IMPORT_SRC_URI ?= "gitsm://github.com-venus-import/nmbath/venus-import.git;branch=main;protocol=ssh;user=git"
SRC_URI = "${VENUS_IMPORT_SRC_URI}"

SRCREV = "0401190bdb89a26ae00b3f5db945dc72b8c2d556"
S = "${WORKDIR}/git"

inherit daemontools python-compile useradd ve_package

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "venus-import"
USERADD_PARAM:${PN} = "--no-create-home --shell /bin/false -g venus-import venus-import"

RDEPENDS:${PN} = " \
    python3-core \
    python3-dbus \
    python3-json \
    python3-pygobject \
    python3-threading \
    venus-containers \
"

DAEMONTOOLS_RUN = "${bindir}/start-venus-import"

do_install() {
    oe_runmake DESTDIR=${D} prefix=/opt/victronenergy/venus-import install

    install -d ${D}${bindir}
    install -m 0755 ${S}/packaging/venus/run ${D}${bindir}/start-venus-import
}

FILES:${PN} += "/opt/victronenergy/venus-import"
