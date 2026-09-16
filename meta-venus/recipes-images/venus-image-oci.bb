SUMMARY = "Containerized Victron image - core D-Bus services + WASM GUI, no GUIv2/Qt"
DESCRIPTION = "No kernel/bootloader/wic - the container runtime supplies the kernel. Exported as a rootfs tarball for docker/podman import."

LICENSE = "MIT"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-base \
    packagegroup-venus-core \
    venus-version \
"
IMAGE_FEATURES += "package-management"

IMAGE_LINGUAS = "en-us"
COPY_LIC_DIRS = "0"

inherit core-image

IMAGE_NAME = "${IMAGE_BASENAME}-${MACHINE}-${DATETIME}-${DISTRO_VERSION}"
IMAGE_NAME[vardepsexclude] += "DATETIME"

IMAGE_FSTYPES = "container"

# image-container.bbclass checks PREFERRED_PROVIDER_virtual/kernel for every MACHINE at parse
# time; am62xx never sets it, so it crashes instead of warning. We do set it (linux-dummy).
IMAGE_CONTAINER_NO_DUMMY = "1"
