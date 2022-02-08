PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} += "\
    binutils \
    coreutils \
    cpio \
    diffutils \
    findutils \
    gawk \
    grep \
    gzip \
    inetutils \
    iputils \
    net-tools \
    procps \
    psmisc \
    sed \
    tar \
    time \
    unzip \
    util-linux \
    vlock \
    which \
"
