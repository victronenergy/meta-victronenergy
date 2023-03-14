require icu.inc

LIC_FILES_CHKSUM = "file://../LICENSE;md5=002d2fdc32d17f0ec06e9a47f2c0c8d0"

def icu_download_version(d):
    pvsplit = d.getVar('PV').split('.')
    return pvsplit[0] + "_" + pvsplit[1]

def icu_download_folder(d):
    pvsplit = d.getVar('PV').split('.')
    return pvsplit[0] + "-" + pvsplit[1]

ICU_PV = "${@icu_download_version(d)}"
ICU_FOLDER = "${@icu_download_folder(d)}"

# http://errors.yoctoproject.org/Errors/Details/20486/
ARM_INSTRUCTION_SET_armv4 = "arm"
ARM_INSTRUCTION_SET_armv5 = "arm"

BASE_SRC_URI = "https://github.com/unicode-org/icu/releases/download/release-${ICU_FOLDER}/icu4c-${ICU_PV}-src.tgz"
DATA_SRC_URI = "https://github.com/unicode-org/icu/releases/download/release-${ICU_FOLDER}/icu4c-${ICU_PV}-data.zip"
SRC_URI = "${BASE_SRC_URI};name=code \
           ${DATA_SRC_URI};name=data \
           file://filter.json \
           file://fix-install-manx.patch \
           file://0001-icu-Added-armeb-support.patch \
           "

SRC_URI_append_class-target = "\
           file://0001-Disable-LDFLAGSICUDT-for-Linux.patch \
          "
SRC_URI[code.sha256sum] = "4cba7b7acd1d3c42c44bb0c14be6637098c7faf2b330ce876bc5f3b915d09745"
SRC_URI[data.sha256sum] = "4fc2d8cfc3343673123586fca3967404abd4e346fba5515829204533b3bae4bf"

UPSTREAM_CHECK_REGEX = "icu4c-(?P<pver>\d+(_\d+)+)-src"
UPSTREAM_CHECK_URI = "https://github.com/unicode-org/icu/releases"

EXTRA_OECONF_append_libc-musl = " ac_cv_func_strtod_l=no"

PACKAGECONFIG ?= ""
PACKAGECONFIG[make-icudata] = ",,,"

do_make_icudata_class-target () {
    cd ${S}
    rm -rf data
    cp -a ${WORKDIR}/data .
    ${@bb.utils.contains('PACKAGECONFIG', 'make-icudata', '', 'exit 0', d)}
    AR='${BUILD_AR}' \
    CC='${BUILD_CC}' \
    CPP='${BUILD_CPP}' \
    CXX='${BUILD_CXX}' \
    RANLIB='${BUILD_RANLIB}' \
    CFLAGS='${BUILD_CFLAGS}' \
    CPPFLAGS='${BUILD_CPPFLAGS}' \
    CXXFLAGS='${BUILD_CXXFLAGS}' \
    LDFLAGS='${BUILD_LDFLAGS}' \
    ICU_DATA_FILTER_FILE=${WORKDIR}/filter.json \
    ./runConfigureICU Linux --with-data-packaging=archive
    oe_runmake
    install -Dm644 ${S}/data/out/icudt${ICU_MAJOR_VER}l.dat ${S}/data/in/icudt${ICU_MAJOR_VER}l.dat
}

do_make_icudata() {
    :
}

addtask make_icudata before do_configure after do_patch
