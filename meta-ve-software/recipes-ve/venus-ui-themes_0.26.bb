LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
UPSTREAM_CHECK_GITTAGREGEX = "v(?P<pver>\S+)"
SRC_URI = "git://github.com/victronenergy/venus-ui-themes.git;branch=master;protocol=ssh;user=git"
SRCREV = "9a8173d7603c98583ac01de84723821b3270e4e7"
S = "${WORKDIR}/git"

inherit allarch ve_package www

# Use the ccgx its theme, since they are svg's now, resolution doesn't matter.
MACH = "ccgx"
THEME_PATH = "${vedir}/themes/${MACH}"
FONT_PATH = "/usr/lib/fonts"

do_install () {
    install -d ${D}${FONT_PATH}
    cp ${S}/*.ttf ${D}${FONT_PATH}
    install -d ${D}${WWW_ROOT}/fonts
    find ${D}${FONT_PATH} -name "*.ttf" -exec ln -sr {} ${D}${WWW_ROOT}/fonts \;

    install -d ${D}${THEME_PATH}
    cp -r ${S}/${MACH}/* ${D}${THEME_PATH}

    # allow custom modification of the theme
    ln -s ${permanentdir}/themes/overlay ${D}${THEME_PATH}/overlay
}

FILES:${PN} += "${THEME_PATH}"
FILES:${PN} += "${FONT_PATH}"

