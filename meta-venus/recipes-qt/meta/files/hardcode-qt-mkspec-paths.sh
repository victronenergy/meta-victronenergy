set -e

# The default meta-toolchain-qte relies on the environmental script being sourced.
# Although that works, it doesn't work well in combination with Qt Creator, since
# an user can no longer switch between Qt version, or has to manually set a whole
# bunch of build environments.
#
# In order to prevent that all paths / commands are hardcoded in the mkspecs after
# the SDK has been installed. In order to do that the SDK calls this script as a
# post install command with the envirmental script name as argument. This script
# is sourced and required OE variables in the mkspecs are replaced with their values
# so sourcing the script is no longer needed for Qt builds.

environment="$1"
. "${environment}"

printf "Updating qt4 mkspecs\n"
sed -i \
	-e "s|\$(OE_QMAKE_CFLAGS)|${CFLAGS}|g" \
	-e "s|\$(OE_QMAKE_CXXFLAGS)|${CXXFLAGS}|g" \
	-e "s|\$(OE_QMAKE_LDFLAGS)|${LDFLAGS}|g" \
	-e "s|\$(OE_QMAKE_CC)|${CC}|g" \
	-e "s|\$(OE_QMAKE_CXX)|${CXX}|g" \
	-e "s|\$(OE_QMAKE_LINK)|${CXX}|g" \
	-e "s|\$(OE_QMAKE_AR)|${AR}|g" \
	-e "s|\$(OE_QMAKE_LIBDIR_QT)|$OECORE_TARGET_SYSROOT/usr/lib|g" \
	-e "s|\$(OE_QMAKE_INCDIR_QT)|$OECORE_TARGET_SYSROOT/usr/include/qtopia|g" \
	-e "s|\$(OE_QMAKE_MOC)|$OECORE_NATIVE_SYSROOT/usr/bin/moc4|g" \
	-e "s|\$(OE_QMAKE_UIC)|$OECORE_NATIVE_SYSROOT/usr/bin/uic4|g" \
	-e "s|\$(OE_QMAKE_UIC3)|$OECORE_NATIVE_SYSROOT/usr/bin/uic34|g" \
	-e "s|\$(OE_QMAKE_RCC)|$OECORE_NATIVE_SYSROOT/usr/bin/rcc4|g" \
	-e "s|\$(OE_QMAKE_QDBUSCPP2XML)|$OECORE_NATIVE_SYSROOT/usr/bin/qdbuscpp2xml4|g" \
	-e "s|\$(OE_QMAKE_QDBUSXML2CPP)|$OECORE_NATIVE_SYSROOT/usr/bin/qdbusxml2cpp4|g" \
	-e "s|\$(OE_QMAKE_QT_CONFIG)|$OECORE_TARGET_SYSROOT/usr/share/qtopia/mkspecs/qconfig.pri|g" \
	${SDKTARGETSYSROOT}/usr/share/qtopia/mkspecs/common/*.conf
