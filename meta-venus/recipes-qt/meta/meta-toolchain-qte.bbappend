SDKIMAGE_FEATURES = "dev-pkgs"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

# add a custom target task to remove all default qt libs
TOOLCHAIN_TARGET_TASK = "packagegroup-sdk-packages"
TOOLCHAIN_TARGET_TASK += "packagegroup-venus-qt5-toolchain-target"

TOOLCHAIN_HOST_TASK += "nativesdk-packagegroup-qt5-toolchain-host qt5-conf"
TOOLCHAIN_HOST_TASK += "nativesdk-python-json"

# The part below hardcodes the paths / commands in the mkspecs of the Qt SDK,
# so they work from Qt Creator without having to source the envirmental script.
# The script below runs after extracting the SDK so its installed location
# is known.
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

SRC_URI += "file://hardcode-qt-mkspec-paths.sh"

create_sdk_files_append() {
    cp ${WORKDIR}/hardcode-qt-mkspec-paths.sh ${SDK_OUTPUT}/${SDKPATH}/
}

# See openembedded-core/meta/files/toolchain-shar-extract.sh and
# populate_sdk.bbclass. The @SDK_POST_INSTALL_COMMAND@ in toolchain-shar-extract.sh
# is replaced by the SDK_POST_INSTALL_COMMAND variable. Since SDK_POST_INSTALL_COMMAND
# already contains the relocate code, append to it..
SDK_POST_INSTALL_COMMAND_append = "bash $target_sdk_dir/hardcode-qt-mkspec-paths.sh ${env_setup_script}"
