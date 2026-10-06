SUMMARY = "Container GX"
DESCRIPTION = "packagegroup-venus-base plus the runtime support for running in a container."

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} += "\
    machine-runtime-conf \
    packagegroup-core-boot \
    packagegroup-ve-console-apps \
    packagegroup-venus-base \
    container-gx-env \
    container-gx-gui-version \
    container-gx-init \
    container-gx-sysfs \
    container-gx-versions \
    container-gx-volatile \
    venus-version \
"
