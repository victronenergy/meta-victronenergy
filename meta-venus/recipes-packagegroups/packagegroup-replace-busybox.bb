inherit packagegroup
LICENSE = "MIT"

RDEPENDS_${PN} += "\
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
