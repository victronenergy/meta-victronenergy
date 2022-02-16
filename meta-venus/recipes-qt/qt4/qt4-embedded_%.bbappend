DEPENDS = "freetype jpeg libpng zlib dbus tiff tslib"
RDEPENDS:libqt-embeddedcore4 += "qt4-machine-conf"

require qt4_ve_patches.inc

# note: the only packageconfig qt4 has is tslib. So it is bypassed since
# qt3, demos, examples etc are all not needed. Do set packageconfig though,
# since tslib depends on MACHINE_FEATURES and hence makes it machine specific,
# which is not needed. Simply always compile with tslib support, a machine
# can always decide to simply not use it.
PACKAGECONFIG = ""

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
    -nomake examples -nomake demos -nomake tests -nomake docs \
    -no-multimedia -no-audio-backend -no-phonon-backend \
    -qdbus -no-openssl -no-qt3support -no-xmlpatterns \
    -no-webkit -no-phonon -no-qt3support \
    -svg -no-javascript-jit -optimized-qmake -no-xvideo \
    ${QT_DISTRO_FLAGS} \
    ${QT_GLFLAGS}"




