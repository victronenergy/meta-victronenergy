SUMMARY = "Container GX"
DESCRIPTION = "packagegroup-venus-base plus the runtime support for running in a container."

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} += "\
    machine-runtime-conf \
    packagegroup-core-boot \
    packagegroup-ve-console-apps \
    packagegroup-venus-base \
    container-gx-gui-version \
    container-gx-versions \
    venus-version \
"
