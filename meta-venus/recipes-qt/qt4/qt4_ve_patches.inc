FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# https://github.com/victronenergy/qt/compare/v4.8.7_oe...victronenergy:v4.8.7_ve
SRC_URI += " \
    file://0001-add-insert-remove-append-clear-to-the-VisualItemMode.patch \
    file://0002-qml-add-the-show-property.patch \
    file://0003-add-mkspecs-for-the-victron-sdk.patch \
    file://0004-qws-vncserver-bind-to-localhost-instead-of-all-inter.patch \
    file://0005-qws-vncserver-add-vnc-authentication.patch \
    file://0006-PATCH-QNativeSocketEngine-fix-SO_REUSEPORT-problems-.patch \
    file://0007-qws-don-t-clear-the-screen-and-re-enable-the-cursor.patch \
    file://0008-ignore-multitouch-events.patch \
    file://0009-add-VisualModels.patch \
    file://0010-silence-expansion-to-defined-nullptr-warnings.patch \
"
