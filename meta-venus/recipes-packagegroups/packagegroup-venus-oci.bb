SUMMARY = "Venus OS for OCI images"
DESCRIPTION = "packagegroup-venus-core plus the runtime support for running as an OCI container."

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} += "\
    packagegroup-venus-core \
    venus-oci-env \
    venus-oci-gui-version \
    venus-oci-sysfs \
    venus-oci-versions \
    venus-oci-volatile \
"
