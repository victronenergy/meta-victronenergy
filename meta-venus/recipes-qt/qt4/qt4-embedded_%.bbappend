DEPENDS = "freetype jpeg libpng zlib dbus tiff tslib"
RDEPENDS_libqt-embeddedcore4 += "qt4-machine-conf"

require qt4_ve_patches.inc

# only build what is needed on the target (and a bit more I guess)
QT_CONFIG_FLAGS = " \
	-embedded $QT_ARCH \
	-qtlibinfix ${QT_LIBINFIX} \
	-plugin-gfx-transformed -plugin-gfx-qvfb -plugin-gfx-vnc \
	-no-svg -no-javascript-jit -optimized-qmake \
	-plugin-mouse-tslib -qt-mouse-pc -qt-mouse-qvfb -qt-mouse-linuxinput \
   	-qt-kbd-tty -qt-kbd-linuxinput \
   	-DQT_KEYPAD_NAVIGATION \
	-no-accessibility -no-sm \
	-release -no-cups -reduce-relocations -fast \
 	-shared -no-nas-sound -no-nis \
	-system-libjpeg -system-libpng -system-libtiff -system-zlib \
	-no-pch -stl -no-glib \
	-no-rpath -silent -no-scripttools \
	-nomake examples -nomake demos -nomake tests \
	-no-multimedia -no-audio-backend -no-phonon-backend \
	-qdbus -no-openssl -no-qt3support -no-xmlpatterns \
	-no-webkit -no-phonon -no-qt3support \
	-svg -no-javascript-jit -optimized-qmake -no-xvideo \
	${QT_DISTRO_FLAGS} \
	${QT_GLFLAGS}"




