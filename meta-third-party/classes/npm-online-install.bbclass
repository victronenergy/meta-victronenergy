inherit npm

# The default npm class is so slow, that it is pratically unusable.
# Just run pack + install instead, this cannot be done offline!!!
# Well plus a bit more foo, since a tarball install doesn't respect
# the npm-shrinkwrap.json, see https://github.com/npm/cli/issues/5349

# Documentation:
# https://github.com/victronenergy/venus/wiki/npm-and-nodejs-packages

do_configure() {
    :
}

# a global install (which is no longer done), will place files under the
# "name" json entry. Store its value in a seperate file so do_compile can
# use it.
python do_getname() {
    import json

    s = d.getVar("S")
    w = d.getVar("WORKDIR")
    pn = d.getVar("PN")
    with open(os.path.join(s, "package.json"), "r") as f:
        package = json.load(f)
    with open(os.path.join(w, "nodename"), "w") as f:
        f.write(package["name"] if "name" in package else pn)
    if "version" in package:
        pkgv = package["version"]
        pv = d.getVar("PV")
        if not pv == pkgv:
            bb.warn(f"PV {pv} doesn't match the package.json its version {pkgv}")

    shrinkwrap = os.path.join(s, "npm-shrinkwrap.json")
    if not os.path.isfile(shrinkwrap):
        return

    with open(shrinkwrap, "r") as f:
        package = json.load(f)

        if not "version" in package:
            bb.warn(f"no version field found in {shrinkwrap}")
            return

        with open(os.path.join(w, "schrinkwrapv"), "w") as f:
            f.write(package["version"])
}

addtask getname after do_unpack before do_compile

B = "${WORKDIR}/npm-pack"
NPM_TMP = "${WORKDIR}/npm-tmp"

DEBUG_PREFIX_MAP += " \
    -fmacro-prefix-map=${NPM_TMP}=${TARGET_DBGSRC_DIR} \
    -fdebug-prefix-map=${NPM_TMP}=${TARGET_DBGSRC_DIR} \
"

NPM_SHRINKMAP_SRC = "${@bb.utils.which(d.getVar('FILESPATH'), 'npm-shrinkwrap.json')}"

do_compile() {

    if [ "$1" = "update_shrinkwrap" ]; then
        if [ -f ${S}/npm-shrinkwrap.json ]; then
            rm ${S}/npm-shrinkwrap.json
        fi
        npm_cmd="install"
    else
        if [ ! -f ${S}/npm-shrinkwrap.json ]; then
            bbfatal "No npm-shrinkwrap.json found for ${PN}"
        fi
        shrinkwrapv="$(cat ${WORKDIR}/schrinkwrapv)"
        if [ "${PV}" != "$shrinkwrapv" ]; then
            bbwarn "PV ${PV} doesn't match the shrinkwrap version $shrinkwrapv, run 'bitbake -c update_shrinkwrap ${PN}' first"
        fi
        npm_cmd="clean-install"
    fi

    tar=$(npm pack ${S})

    # This used to install the tarball directly in the right place, but there is a bug
    # in more recent versions, ignoring the npm-shrinkwrap.json, see
    # https://github.com/npm/cli/issues/5349. So instead work around it, by extracting
    # the tarball to a temporary location, run a npm install on the dir and thereafter
    # move the files to where OE expects them (not that is in lib; they will be moved
    # again to populate D, to end up in /usr/lib eventually)
    tar -xzf $tar -C ${WORKDIR}/npm-extracted --strip-components 1 -C "${NPM_TMP}"

    if [ "$1" != "update_shrinkwrap" ]; then
        # make sure npm-shrinkwrap is there for a normal install. Not sure how this works,
        # it is in the tar for node-red, but not node-red-venus-contrib.
        # Maybe https://github.com/npm/cli/issues/6803 ?
        cp ${S}/npm-shrinkwrap.json "${NPM_TMP}"
    fi

    cd "${NPM_TMP}" && npm $npm_cmd \
        --arch=${NPM_ARCH} \
        --target_arch=${NPM_ARCH} \
        --omit=dev \
        --omit=optional

    # mimic a global install
    NAME="$(cat ${WORKDIR}/nodename)"
    install -d ${NPM_BUILD}/lib/node_modules/$NAME
    cp --no-preserve=ownership --recursive ${NPM_TMP}/. ${NPM_BUILD}/lib/node_modules/$NAME
}
do_compile[network] = "1"
do_compile[cleandirs] += "${NPM_BUILD} ${NPM_TMP}"

python do_binlinks() {
    import json

    s = d.getVar("S")
    b = d.getVar("NPM_BUILD")
    pn = d.getVar("PN")
    with open(os.path.join(s, "package.json"), "r") as f:
        package = json.load(f)
    if "bin" in package:
        bb.utils.mkdirhier(os.path.join(b, "bin"))
        for tg, src in package["bin"].items():
            bin = os.path.abspath(os.path.join(b, "lib", "node_modules", pn, src))
            link = os.path.join(b, "bin", tg)
            rel = os.path.relpath(bin, os.path.dirname(link))
            oe.path.symlink(rel, link, True)
}
addtask binlinks after do_compile before do_install

NPM_SHRINKWRAP_SRC = "${@bb.utils.which(d.getVar('FILESPATH'), 'npm-shrinkwrap.json')}"

do_update_shrinkwrap() {
    if [ "${NPM_SHRINKWRAP_SRC}" = "" ]; then
         bbfatal "no shrinkmap source file found"
    fi

    do_compile update_shrinkwrap
    npm shrinkwrap

    if ! diff -q ${NPM_TMP}/npm-shrinkwrap.json ${NPM_SHRINKWRAP_SRC}; then
        bbwarn updating ${NPM_SHRINKWRAP_SRC}
        cp ${NPM_TMP}/npm-shrinkwrap.json ${NPM_SHRINKWRAP_SRC}
    else
        bbwarn "shrinkwrap not changed ${S}"
        cp ${NPM_SHRINKWRAP_SRC} ${S}/npm-shrinkwrap.json
    fi
}
addtask update_shrinkwrap after do_getname do_prepare_recipe_sysroot
do_update_shrinkwrap[cleandirs] += "${NPM_BUILD} ${NPM_TMP}"
do_update_shrinkwrap[dirs] = "${NPM_TMP}"
do_update_shrinkwrap[network] = "1"
do_update_shrinkwrap[nostamp] = "1"

INSANE_SKIP:${PN} += "already-stripped"
