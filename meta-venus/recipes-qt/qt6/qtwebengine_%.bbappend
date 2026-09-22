# This feature branch always uses the proven GX-minimal WebEngine profile.
# It can still be overridden explicitly for comparison builds.
VENUS_WEBENGINE_PROFILE ?= "gxmin"

# PPAPI test-support code requires Chromium printing, so pepper-plugins and
# printing-and-pdf must be removed together. WebChannel remains enabled: it
# is part of the embedded browser capability used by GUIv2.
PACKAGECONFIG:remove = "${@bb.utils.contains('VENUS_WEBENGINE_PROFILE', 'gxmin', 'spellchecker webrtc geolocation pepper-plugins printing-and-pdf', '', d)}"

# Venus already ships system FreeType for Qt text rendering, so use it rather
# than carrying Chromium's bundled copy.
PACKAGECONFIG:append = "${@bb.utils.contains('VENUS_WEBENGINE_PROFILE', 'gxmin', ' freetype', '', d)}"

# Fixed GX hardware does not need Chromium's PCI-ID GPU blocklist lookup.
PACKAGECONFIG:remove = "${@bb.utils.contains('VENUS_WEBENGINE_PROFILE', 'gxmin', 'libpci', '', d)}"

# QtWebEngineView launches this renderer helper at runtime. The upstream
# recipe installs it but does not otherwise assign it to a package.
FILES:${PN} += "${libexecdir}/QtWebEngineProcess"

do_install:append() {
    if [ "${VENUS_WEBENGINE_PROFILE}" = "gxmin" ]; then
        # DevTools are not exposed by the production GUI.
        rm -f ${D}${QT6_INSTALL_DATADIR}/resources/qtwebengine_devtools_resources.pak

        # GUIv2 web content only needs the English Chromium locale packs.
        find ${D}${QT6_INSTALL_TRANSLATIONSDIR}/qtwebengine_locales -type f \
            ! -name 'en-US.pak' ! -name 'en-GB.pak' -delete
    fi
}
