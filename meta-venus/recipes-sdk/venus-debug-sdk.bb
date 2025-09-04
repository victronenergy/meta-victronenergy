SDK_VERSION:append = "-debug"
SDKPATHINSTALL:append = "-debug"

require venus-sdk.bb

SDKIMAGE_FEATURES:append = " dbg-pkgs"
