PACKAGECONFIG_DEFAULT = "accessibility dbus evdev freetype kms libs linuxfb sql-sqlite openssl udev widgets"
PACKAGECONFIG_GL = "eglfs gles2"

FILESEXTRAPATHS_prepend := "${THISDIR}/qtbase:"

SRC_URI += "file://0001-Revert-Remove-support-for-qml1-plugins-and-modules.patch"
