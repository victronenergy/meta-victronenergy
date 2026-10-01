SUMMARY = "Containerized Venus image"
DESCRIPTION = "Venus OS image for OCI-compatible container runtimes."

LICENSE = "MIT"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-base \
    packagegroup-venus-core \
    venus-container-sysfs \
    venus-container-volatile \
    venus-version \
"
IMAGE_FEATURES += "package-management"

IMAGE_LINGUAS = "en-us"
COPY_LIC_DIRS = "0"

IMGCLASSES:remove = "populate_sdk_ext"
inherit core-image

deltask populate_sdk

IMAGE_BASENAME = "venus-oci"
IMAGE_NAME = "${IMAGE_BASENAME}-${MACHINE}-${DATETIME}-${DISTRO_VERSION}"
IMAGE_NAME[vardepsexclude] += "DATETIME"

IMAGE_FSTYPES = "container oci"

# image-container.bbclass checks PREFERRED_PROVIDER_virtual/kernel for every MACHINE at parse
# time; am62xx never sets it, so it crashes instead of warning. We do set it (linux-dummy).
IMAGE_CONTAINER_NO_DUMMY = "1"

# meta-virtualization predates the classes-recipe/classes-global split, so its image-oci
# class isn't auto-discovered per IMAGE_FSTYPES entry the way core classes are.
IMAGE_CLASSES += "image-oci"

# umoci (the default OCI_IMAGE_BACKEND) can't set the arch variant, so armv7 would lose its
# /v7 platform tag - sloci-image does via --arch-variant.
OCI_IMAGE_BACKEND = "sloci-image"

# image-oci.bbclass's own OCI_IMAGE_ARCH default is already Go/OCI-translated (e.g. "arm64"),
# which is correct for umoci (direct pass-through) but wrong for sloci-image: its own oci_arch()
# does the raw->OCI translation itself and expects raw input (aarch64/x86_64/arm*) - "arm64"
# collides with its arm* catch-all and silently comes out as plain "arm". Feed it raw instead.
OCI_IMAGE_ARCH = "${TARGET_ARCH}"

# image-oci.bbclass's OCI_IMAGE_SUBARCH auto-detection checks TUNE_FEATURES for the literal
# substring "armv7", but armv7's tune (tune-cortexa8.inc, an armv7a-profile tune) only sets
# TUNE_FEATURES to "arm vfp cortexa8 neon callconvention-hard" - no such substring exists, so
# the heuristic silently resolves to empty and the OCI image loses its /v7 platform tag. Set it
# explicitly since we already know which machine needs it.
OCI_IMAGE_SUBARCH:armv7 = "v7"

# ~ isn't a legal OCI/Docker tag character; DISTRO_VERSION uses it for beta builds (v3.80~46).
OCI_IMAGE_TAG = "${@d.getVar('DISTRO_VERSION').replace('~', '-')}"

OCI_IMAGE_ENTRYPOINT = "/sbin/init"
OCI_IMAGE_PORTS = "80/tcp 1883/tcp 8883/tcp 9001/tcp"
