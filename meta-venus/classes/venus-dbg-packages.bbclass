#
# This class allows to create empty dbg on a layer or recipe basis.
#
# Debug symbols can become quite large, they be an order of maginitute
# larger than the stripped version. Since this is easily several gigabytes
# per arch even when compressed per arch / machine, creating debug packages
# was simply disabled on the builder with INHIBIT_PACKAGE_DEBUG_SPLIT = "1".
#
# It turns out there are some downsides to that:
#  - The .gnu_debuglink section in the stipped file contains the filename of
#    the debug symbols file and a CRC. When instructing OE not to created
#    debug images, this section is not created either. Hence even after
#    installing the proper debug package, gdb etc won't find the debug
#    symbols. [it can be found build-id if you really want to, but that
#    should simply work]. The following readelf from binutils can be used
#    to verify if the sectio is present with:
#      readelf --string-dump=.gnu_debuglink /path/to/elf
#  - valgrind needs the dbg package of glibc, which wasn't build / available.
#
# So this takes a different approach, instead of disabling them all, dbg
# packages can be emptied at a more fine grained level. The will be emptied
# _after_ splitting, so the stripped version do always have a .gnu_debuglink
# link section.
#
# VENUS_DBG_PACKAGES can be set to pick such a selection.
# By default the following options are available:
#
# "minimal": Only the dbg-packages which are needed for programs to function
#            correctly, like valgrind.
#
# "medium":  Only the dbg symbols which might fit on a target device,
#            saving space, bandwidth and deploy time.
#
# "large":   All the opensource ones. Not that useful for a package feed,
#            but can be useful for special purposes.
#
#
# Filter is done on PN first, as in the name of the recipe, not the package.
#
#   If it is in VENUS_DBG_PN_EXCLUDE_[category] where category is the value
#   of VENUS_DBG_PACKAGES the dbg packages will be emptied.
#
#   If it is VENUS_DBG_PN_KEEP_DEFAULT_[category] this class does nothing.
#   It depends on OE if there is or isn't an empty dbg package.
#
# If above doesn't match filtering is done on the layer with:
#   VENUS_DBG_LAYERS_EXCLUDE_[category] and VENUS_DBG_LAYERS_KEEP_DEFAULT_[category].
#   The longest pattern wins, wildcards are supported, e.g.
#
#     VENUS_DBG_LAYERS_KEEP_DEFAULT_[category] = "example/*"
#     VENUS_DBG_LAYERS_EXCLUDE_[category] = "example/but-not-me"
#
# Can selectively exclude parts of some meta while keeping the rest.
#
# If you don't want to clean any dbg, just don't inherit this class. 

inherit venus-bbinfo

VENUS_DBG_PACKAGES ?= "minimal"

### Exclude everything by default, only include pn which are needed.

# glibc-dbg is needed for valgrind to work.
VENUS_DBG_PN_EXCLUDE_minimal = ""
VENUS_DBG_PN_KEEP_DEFAULT_minimal = " \
    glibc \
"
VENUS_DBG_LAYERS_EXCLUDE_minimal = "*"
VENUS_DBG_LAYERS_KEEP_DEFAULT_minimal = ""

### medium level, have dbg packages for core libraries / tools

# disable the large dbg packages
VENUS_DBG_PN_EXCLUDE_medium = " \
    gcc \
    nodejs \
    spirv-tools \
"
VENUS_DBG_PN_KEEP_DEFAULT_medium = ""

# mind the /* vs none btw, the /* wont match a layer without subdirs!
VENUS_DBG_LAYERS_KEEP_DEFAULT_medium = " \
    openembedded-core/* \
    meta-openembedded/* \
    meta-venus-backports \
    meta-victronenergy/* \
"
VENUS_DBG_LAYERS_EXCLUDE_medium = "*"

### large, just include all open source layers, this will be several gigabytes.

# Typically there is no space on the target to install the large ones.
# iow, this is only needed for special purposes.
VENUS_DBG_PN_EXCLUDE_large = ""
VENUS_DBG_PN_KEEP_DEFAULT_large = ""
VENUS_DBG_LAYERS_KEEP_DEFAULT_large = "*"
VENUS_DBG_LAYERS_EXCLUDE_large = "meta-victronenergy-private"

def venus_dbg_var(name, d):
    dbg_pkgs = d.getVar("VENUS_DBG_PACKAGES") or "minimal"
    varname = f"{name}_{dbg_pkgs}"
    var = d.getVar(varname)
    if var == None:
        raise Exception(f"{varname} doesn't exists")
    return var

def venus_dbg_match(layer, patterns):
    import fnmatch

    patterns.sort(reverse=True, key=len)
    for pattern in patterns:
        if fnmatch.fnmatch(layer, pattern):
            return len(pattern)
    return -1

def venus_should_empty_dbg(d):
    pn = bb.parse.vars_from_file(d.getVar('FILE', False), d)[0] or None

    pn_excluded = venus_dbg_var("VENUS_DBG_PN_EXCLUDE", d).split()
    if pn in pn_excluded:
        return True

    pn_default = venus_dbg_var("VENUS_DBG_PN_KEEP_DEFAULT", d).split()
    if pn in pn_default:
        return False

    # the most specific one wins..
    layer = d.getVar("VENUS_LAYER")
    keep = venus_dbg_var("VENUS_DBG_LAYERS_KEEP_DEFAULT", d).split()
    exclude = venus_dbg_var("VENUS_DBG_LAYERS_EXCLUDE", d).split()
    keep_len = venus_dbg_match(layer, keep)
    exclude_len = venus_dbg_match(layer, exclude)

    if keep_len <= exclude_len or keep_len == -1:
        # bb.warn(f"disable debug pkg for {pn} in {layer} keep:{keep_len} exclude:{exclude_len}")
        return True
    
    return False

python venus_empty_dbg_pkgs() {
    if venus_should_empty_dbg(d) == False:
        return

    pkgs = d.getVar("PACKAGES")
    for pkg in pkgs.split():
        if pkg.endswith("-dbg"):
            dir = os.path.join(d.getVar('PKGDEST'), pkg)
            oe.path.remove(dir, recurse=True)
            os.mkdir(dir)
}

# Since the dbg files are removed, rerun do_package if something changed
# to restore them.
do_package[vardeps] += "VENUS_DBG_PACKAGES \
    VENUS_DBG_LAYERS_KEEP_DEFAULT_${VENUS_DBG_PACKAGES} VENUS_DBG_LAYERS_EXCLUDE_${VENUS_DBG_PACKAGES} \
    VENUS_DBG_PN_KEEP_DEFAULT_${VENUS_DBG_PACKAGES} VENUS_DBG_PN_EXCLUDE_${VENUS_DBG_PACKAGES}"

# Make removing part of do_package, so sstate works as well etc.
# This runs after process_split_and_strip_files, so the regular binaries
# do have the proper .gnu_debuglink section.
PACKAGESPLITFUNCS:append = " venus_empty_dbg_pkgs"

# Alternatively prepend to PACKAGEFUNCS but pkgfiles should then likely
# be adjusted as well...
