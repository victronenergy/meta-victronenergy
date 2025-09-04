inherit venus-bbinfo

python venus_ipkg_info() {
    bb_file = d.getVar("VENUS_BBFILE")
    layer_dir = d.getVar("VENUS_LAYER")

    add_info = f"Venus-Layer: {layer_dir}\nVenus-Source: {bb_file}"
    d.appendVar("PACKAGE_ADD_METADATA_IPK", add_info)
}

do_package_write_ipk[prefuncs] += "venus_ipkg_info"
