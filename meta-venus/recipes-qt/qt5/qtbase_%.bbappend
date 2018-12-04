PACKAGECONFIG_DISTRO = "accessibility openssl sql-sqlite widgets"
# Disable getentropy because of older than 3.17 kernel
QT_CONFIG_FLAGS_ccgx += "--no-feature-getentropy -no-opengl"
