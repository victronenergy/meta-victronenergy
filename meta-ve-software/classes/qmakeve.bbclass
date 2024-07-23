inherit ve_package

QT_PROVIDER ?= "${@oe.utils.conditional('VENUS_QT_VERSION', '4', 'qt4venus', 'qt6venus', d)}"
inherit ${QT_PROVIDER}
