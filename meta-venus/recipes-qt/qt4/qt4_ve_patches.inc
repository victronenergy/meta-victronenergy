FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# https://github.com/victronenergy/qt/compare/v4.8.7_oe...victronenergy:v4.8.7_ve
SRC_URI += " \
	file://0001-add-insert-remove-append-clear-to-the-VisualItemMode.patch \
	file://0002-add-mkspecs-for-the-victron-sdk.patch \
	file://0003-qws-vncserver-bind-to-localhost-instead-of-all-inter.patch \
	file://0004-qws-vncserver-add-vnc-authentication.patch \
	file://0005-PATCH-QNativeSocketEngine-fix-SO_REUSEPORT-problems-.patch \
	file://0006-qws-don-t-clear-the-screen-and-re-enable-the-cursor.patch \
	file://0007-ignore-multitouch-events.patch \
	file://0008-add-VisualModels.patch \
	file://0009-silence-expansion-to-defined-nullptr-warnings.patch \
	file://0010-qdeclarativelistview-set-currentItem-to-false-in-cle.patch \
	file://0011-QDeclarativeVisualDataModel-crashes-getting-QAbstrac.patch \
	file://0012-add-.model-to-QDeclarativeVisualDataModelData.patch \
	file://0013-QDeclarativeVisualDataModel-add-missing-itemsRemoved.patch \
	file://0014-mouse-don-t-warn-about-sound-events.patch \
	file://0015-QSortFilterProxyModel-does-not-sort-in-some-cases.patch \
	file://0016-drop-the-register-storage-specifier.patch \
	file://0017-add-VisibleItemModel.patch \
"
