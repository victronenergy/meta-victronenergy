inherit npm

# The default npm class is so slow, that it is pratically unusable.
# Just run pack + install instead, this cannot be done offline!!!

do_configure() {
    :
}

B = "${WORKDIR}/npm-pack"
do_compile() {
    if [ ! -f ${S}/npm-shrinkwrap.json ]; then
        bbfatal "No npm-shrinkwrap.json found for ${PN}"
    fi

    tar=$(npm pack ${S})

    npm install \
        --global \
        --prefix="${NPM_BUILD}" \
        --arch=${NPM_ARCH} \
        --target_arch=${NPM_ARCH} \
        --production \
        "$tar"
}
do_compile[network] = "1"
