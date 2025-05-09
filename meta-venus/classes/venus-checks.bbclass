python () {
    # To be compatible with newer OE versions, not part of scarthgap, but
    # checked to facilitate upgrading.
    file = d.getVar('FILE')
    if "meta-victronenergy" in file or "meta-swupdate" in file:
        sourcedir = d.getVar("S")
        workdir = d.getVar("WORKDIR")
        if sourcedir == workdir:
            bb.warn("Using S = ${WORKDIR} is no longer supported: " + file)
}
