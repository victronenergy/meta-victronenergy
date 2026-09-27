DESCRIPTION = "A fast and low-memory footprint OCI Container Runtime fully written in C."
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"
PRIORITY = "optional"

SRCREV_crun = "fb54bc00bf4a9139e74ba68e00da22b424b90912"
SRCREV_libocispec = "b465698c1d4d2be50016e6739cc7832563994760"
SRCREV_ispec = "af26a05fba5ee648512f4ea3c9fda1fcc1b6d6dc"
SRCREV_rspec = "6999a89a76a0329f440d5740497bedb9dd431297"
SRCREV_yajl = "f344d21280c3e4094919fd318bc5ce75da91fc06"

SRCREV_FORMAT = "crun_rspec"
SRC_URI = "git://github.com/containers/crun.git;branch=main;name=crun;protocol=https \
           git://github.com/containers/libocispec.git;branch=main;name=libocispec;destsuffix=git/libocispec;protocol=https \
           git://github.com/opencontainers/runtime-spec.git;branch=main;name=rspec;destsuffix=git/libocispec/runtime-spec;protocol=https \
           git://github.com/opencontainers/image-spec.git;branch=main;name=ispec;destsuffix=git/libocispec/image-spec;protocol=https \
           git://github.com/containers/yajl.git;branch=main;name=yajl;destsuffix=git/libocispec/yajl;protocol=https \
           file://0001-libocispec-correctly-parse-JSON-schema-references.patch;patchdir=libocispec \
           file://0002-libocispec-fix-array-items-parsing.patch;patchdir=libocispec \
          "

PV = "1.28.0+git"

S = "${WORKDIR}/git"

inherit autotools-brokensep pkgconfig features_check

# crun ships a GNUmakefile that aborts if ./configure hasn't run yet,
# which breaks autotools_preconfigure's "make clean" on rebuild.
CLEANBROKEN = "1"

# if this is true, we'll symlink crun to runc for easier integration
# with container stacks
CRUN_AS_RUNC ?= "true"

PACKAGECONFIG ??= " \
    caps external-yajl man \
    ${@bb.utils.contains('DISTRO_FEATURES', 'seccomp', 'seccomp', '', d)} \
    ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'systemd', '', d)} \
"

PACKAGECONFIG[caps] = "--enable-caps,--disable-caps,libcap"
PACKAGECONFIG[external-yajl] = "--disable-embedded-yajl,--enable-embedded-yajl,yajl"
# whether to regenerate manpages that are already present in the repo
PACKAGECONFIG[man] = ",,go-md2man-native"
PACKAGECONFIG[seccomp] = "--enable-seccomp,--disable-seccomp,libseccomp"
PACKAGECONFIG[systemd] = "--enable-systemd,--disable-systemd,systemd"

DEPENDS = "m4-native json-c"
DEPENDS:append:libc-musl = " argp-standalone"

do_configure:prepend () {
    # extracted from autogen.sh in crun source. This avoids
    # git submodule fetching.
    mkdir -p m4
    autoreconf -fi
}

do_install() {
    oe_runmake 'DESTDIR=${D}' install
    if [ -n "${CRUN_AS_RUNC}" ]; then
        ln -sr "${D}/${bindir}/crun" "${D}${bindir}/runc"
    fi
}

# When crun provides /usr/bin/runc symlink, it conflicts with the runc package
RCONFLICTS:${PN} = "${@'runc' if d.getVar('CRUN_AS_RUNC') else ''}"

REQUIRED_DISTRO_FEATURES:class-native ?= ""
DEPENDS:class-native += "yajl libcap go-md2man m4 libseccomp"
BBCLASSEXTEND = "native"

CVE_STATUS[CVE-2025-24965] = "fixed-version: Fixed since 1.20"
