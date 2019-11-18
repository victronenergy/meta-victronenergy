DESCRIPTION = "packages for the SDK"
LICENSE = "MIT"

inherit packagegroup

# note: these end up in the target sysroot of the SDK!
RDEPENDS_packagegroup-sdk-packages += " \
    boost-staticdev \
    connman \
    dbus \
    libevent \
    lua-staticdev \
    mtd-utils-staticdev \
    packagegroup-core-standalone-sdk-target \
    qt4-embedded-mkspecs \
    qt4-embedded-plugin-gfxdriver-gfxvnc \
    qt4-embedded-plugin-imageformat-gif \
    qt4-embedded-plugin-imageformat-ico \
    qt4-embedded-plugin-imageformat-jpeg \
    qt4-embedded-plugin-imageformat-svg \
    qt4-embedded-plugin-imageformat-tiff \
"

# swu related builds with NAND env
RDEPENDS_packagegroup-sdk-packages_append_ccgx += "u-boot-fw-utils-staticdev"
