SUMMARY = "Venus SDK"
LICENSE = "MIT"

# no magic please, the required packages will be mentioned explicitly.
SDKIMAGE_FEATURES = ""

inherit populate_sdk_qt6_base populate_sdk

TOOLCHAIN_TARGET_TASK += "venus-sdk-target-packages"
TOOLCHAIN_HOST_TASK = "packagegroup-cross-canadian-${MACHINE}"

TOOLCHAIN_HOST_TASK += " \
    nativesdk-autoconf \
    nativesdk-automake \
    nativesdk-cmake \
    nativesdk-libtool \
    nativesdk-meson \
    nativesdk-pkgconfig \
"

TOOLCHAIN_HOST_TASK += " \
    nativesdk-qt4-tools \
    nativesdk-qtbase-tools \
"

# qt4 paths, copied from meta-qt4 / meta-toolchain-qte.bb to prevent
# their additional includes.

QT4_NAME = "qte"
QT4_DIR_NAME = "qtopia"
QT4_TOOLS_PREFIX = "$OECORE_NATIVE_SYSROOT${bindir_nativesdk}"

create_sdk_files:append() {
    mkdir -p ${SDK_OUTPUT}${SDKPATHNATIVE}/environment-setup.d/
    script=${SDK_OUTPUT}${SDKPATHNATIVE}/environment-setup.d/${QT4_DIR_NAME}.sh

    echo 'export OE_QMAKE_CFLAGS="$CFLAGS"' > $script
    echo 'export OE_QMAKE_CXXFLAGS="$CXXFLAGS"' >> $script
    echo 'export OE_QMAKE_LDFLAGS="$LDFLAGS"' >> $script
    echo 'export OE_QMAKE_CC=$CC' >> $script
    echo 'export OE_QMAKE_CXX=$CXX' >> $script
    echo 'export OE_QMAKE_LINK=$CXX' >> $script
    echo 'export OE_QMAKE_AR=$AR' >> $script
    echo 'export OE_QMAKE_LIBDIR_QT=$OECORE_TARGET_SYSROOT${libdir}' >> $script
    echo 'export OE_QMAKE_INCDIR_QT=$OECORE_TARGET_SYSROOT${includedir}/${QT4_DIR_NAME}' >> $script
    echo 'export OE_QMAKE_MOC=${QT4_TOOLS_PREFIX}/moc4' >> $script
    echo 'export OE_QMAKE_UIC=${QT4_TOOLS_PREFIX}/uic4' >> $script
    echo 'export OE_QMAKE_UIC3=${QT4_TOOLS_PREFIX}/uic34' >> $script
    echo 'export OE_QMAKE_RCC=${QT4_TOOLS_PREFIX}/rcc4' >> $script
    echo 'export OE_QMAKE_QDBUSCPP2XML=${QT4_TOOLS_PREFIX}/qdbuscpp2xml4' >> $script
    echo 'export OE_QMAKE_QDBUSXML2CPP=${QT4_TOOLS_PREFIX}/qdbusxml2cpp4' >> $script
    echo 'export OE_QMAKE_QT_CONFIG=$OECORE_TARGET_SYSROOT${datadir}/${QT4_DIR_NAME}/mkspecs/qconfig.pri' >> $script
    echo 'export QMAKESPEC=$OECORE_TARGET_SYSROOT${datadir}/${QT4_DIR_NAME}/mkspecs/linux-g++' >> $script
    echo 'export QT_CONF_PATH=$OECORE_NATIVE_SYSROOT${sysconfdir}/qt.conf' >> $script

    # make a symbolic link to mkspecs for compatibility with Qt SDK
    # and Qt Creator
    (cd ${SDK_OUTPUT}/${SDKPATHNATIVE}${bindir_nativesdk}/..; ln -s ${SDKTARGETSYSROOT}/usr/share/${QT4_DIR_NAME}/mkspecs mkspecs;)
}
