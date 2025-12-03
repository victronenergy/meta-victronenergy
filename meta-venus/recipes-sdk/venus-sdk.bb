SUMMARY = "Venus SDK"
LICENSE = "MIT"

# no magic please, the required packages will be mentioned explicitly.
SDKIMAGE_FEATURES = "dev-pkgs"

inherit populate_sdk_qt6_base populate_sdk

TOOLCHAIN_TARGET_TASK += "venus-sdk-target-packages"
TOOLCHAIN_HOST_TASK:remove = "nativesdk-packagegroup-sdk-host"

TOOLCHAIN_HOST_TASK += " \
    nativesdk-autoconf \
    nativesdk-automake \
    nativesdk-cmake \
    nativesdk-libtool \
    nativesdk-meson \
    nativesdk-ninja \
    nativesdk-pkgconfig \
    nativesdk-perl-module-integer \
"

TOOLCHAIN_HOST_TASK += " \
    nativesdk-qtbase-tools \
    nativesdk-qtdeclarative-tools \
    nativesdk-qtshadertools-dev \
    nativesdk-qtshadertools-tools \
    nativesdk-qttools-dev \
    nativesdk-qttools-tools \
"

# note: arm32 linux builds might require gcc-plugins to work.
# That requires the host-gcc to be of the same version as the cross-gcc,
# so ship the host-gcc as well (and some required dev packages).
TOOLCHAIN_HOST_TASK += " \
    nativesdk-binutils \
    nativesdk-binutils-symlinks \
    nativesdk-cpp \
    nativesdk-cpp-symlinks \
    nativesdk-gcc \
    nativesdk-gcc-plugins \
    nativesdk-gcc-symlinks \
    nativesdk-g++ \
    nativesdk-g++-symlinks \
    nativesdk-gmp-dev \
    nativesdk-libmpc-dev \
    nativesdk-ncurses-dev \
    nativesdk-openssl-dev \
"

# gdb depends on this.. "could not convert 'main' from the host encoding (UTF-8) to UTF-32."
TOOLCHAIN_HOST_TASK += " \
    nativesdk-glibc-gconv-utf-32 \
"
