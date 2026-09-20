SUMMARY = "Venus OS for OCI images"
DESCRIPTION = "packagegroup-venus-base plus the runtime support for running as an OCI container."

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} += "\
    machine-runtime-conf \
    packagegroup-core-boot \
    packagegroup-ve-console-apps \
    packagegroup-venus-base \
    venus-oci-gui-version \
    venus-oci-versions \
    venus-oci-volatile \
    venus-version \
"
