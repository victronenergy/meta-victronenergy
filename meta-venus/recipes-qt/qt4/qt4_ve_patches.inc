FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# NOTE: mind the $PV in the second patch, it depends on the version!
SRC_URI += " \
	file://0001-add-insert-remove-append-clear-to-the-VisualItemMode.patch \
	file://0002-${PV}-qml-add-the-show-property.patch \
	file://0003-add-mkspecs-for-the-victron-sdk.patch \
	file://0004-qws-vncserver-bind-to-localhost-instead-of-all-inter.patch \
	file://0005-qws-vncserver-add-vnc-authentication.patch \
	file://0006-PATCH-QNativeSocketEngine-fix-SO_REUSEPORT-problems-.patch \
	file://0001-VisualItemModel-fix-updateIndices.patch \
	file://0001-qws-don-t-clear-the-screen-and-reanable-the-cursor.patch \
"
