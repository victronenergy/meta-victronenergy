LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

INHIBIT_DEFAULT_DEPS = "1"

inherit qmake5_paths nativesdk

SRC_URI += "file://qt.conf.orig"

do_install () {
    cp ${WORKDIR}/qt.conf.orig qt.conf

    # FIXME
    echo 'Sysroot = ../../../../${TUNE_PKGARCH}${TARGET_VENDOR}-${TARGET_OS}-gnueabi' >> qt.conf
    echo 'HostData = ../${TUNE_PKGARCH}${TARGET_VENDOR}-${TARGET_OS}-gnueabi/usr/lib/qt5' >> qt.conf

    mkdir -p ${D}${OE_QMAKE_PATH_HOST_BINS}
    install qt.conf ${D}${OE_QMAKE_PATH_HOST_BINS}/qt.conf
}

FILES_${PN} = "${OE_QMAKE_PATH_HOST_BINS}/*"
