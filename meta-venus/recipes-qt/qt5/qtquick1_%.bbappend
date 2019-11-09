FILESEXTRAPATHS_prepend := "${THISDIR}/../qt4/files:"

# no webkit
PACKAGECONFIG = ""

SRC_URI += " \
    file://0001-add-insert-remove-append-clear-to-the-VisualItemMode.patch \
    file://0002-4.8.6-qml-add-the-show-property.patch \
"
