QT_PROVIDER ?= "${@bb.utils.contains('DISTRO_FEATURES', 'x11', 'qt4x11', 'qt4e', d)}"
inherit ${QT_PROVIDER}
