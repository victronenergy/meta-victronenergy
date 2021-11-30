SDKIMAGE_FEATURES = "dev-pkgs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

# add a custom target task to remove all default qt libs
TOOLCHAIN_TARGET_TASK = "packagegroup-sdk-packages"
TOOLCHAIN_TARGET_TASK += "packagegroup-venus-qt6-toolchain-target"

TOOLCHAIN_HOST_TASK += "nativesdk-packagegroup-qt6-toolchain-host"

# The part below hardcodes the paths / commands in the mkspecs of the Qt SDK,
# so they work from Qt Creator without having to source the envirmental script.
# The script below runs after extracting the SDK so its installed location
# is known.
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

inherit populate_sdk populate_sdk_qt6_base

