SDKIMAGE_FEATURES = "dev-pkgs"

# add a custom target task to remove all default qt libs
TOOLCHAIN_TARGET_TASK = "packagegroup-sdk-packages"
TOOLCHAIN_TARGET_TASK += "packagegroup-qt5-toolchain-target"

TOOLCHAIN_HOST_TASK += "nativesdk-packagegroup-qt5-toolchain-host qt5-conf"
TOOLCHAIN_HOST_TASK += "nativesdk-python-importlib nativesdk-python-json"
