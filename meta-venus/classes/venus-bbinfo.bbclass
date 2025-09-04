# add the subdir of the layer and the name of the bbfile within it.

python() {
    bb_absfile = d.getVar("FILE")
    bb_layers = d.getVar("BBLAYERS").split()

    layer_absdir = None
    for layer in bb_layers:
        if os.path.realpath(bb_absfile).startswith(os.path.realpath(layer) + os.sep):
            layer_absdir = layer
            break

    if not layer_absdir:
        bb.warn("Could not find layer for %s" % bbfile)
        return

    sources = os.path.join(d.getVar("COREBASE"), "..")
    layer_dir = os.path.relpath(layer_absdir, sources)
    bb_file = os.path.relpath(bb_absfile, layer_absdir)

    d.setVar("VENUS_BBFILE", bb_file)
    d.setVar("VENUS_LAYER", layer_dir)
}
