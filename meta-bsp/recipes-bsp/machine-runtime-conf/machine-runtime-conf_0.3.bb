DESCRIPTION = "Creates the config files which are used runtime by Venus"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PACKAGE_ARCH = "${MACHINE_ARCH}"

python do_install () {
    machine_features = set(d.getVar("MACHINE_FEATURES", True).split())

    path = d.getVar("D", True) + d.getVar("sysconfdir", True) + "/venus/"
    os.makedirs(path)

    open(path + "files.created.by.machine-conf-runtime", "w")

    f = open(path + "machine", "w")
    f.write(d.getVar("MACHINE", True) + "\n")
    f.close()

    # headless vs heafull device. Used by gui
    if "headless" in machine_features:
        open(path + "headless", "w")

    # mk2/mk3 port. Used by vecan-dbus aka mk2-dbus, as well as mk2vsc and some more
    mkx_port = d.getVar("VE_MKX_PORT", True)
    if mkx_port:
        f = open(path + "mkx_port", "w")
        f.write(mkx_port + "\n")
        f.close()

    # vedirect ports. Used by serialstarter script
    vedirect_ports = d.getVar("VE_VEDIRECT_PORTS", True)
    if vedirect_ports:
        f = open(path + "vedirect_ports", "w")
        f.write(vedirect_ports + "\n")
        f.close()

    # vedirect port which is also used as console port. Like on ccgx/bbp3. Used
    # by the serial starter script.
    vedirect_and_console_port = d.getVar("VE_VEDIRECT_AND_CONSOLE_PORT", True)
    if vedirect_and_console_port:
        f = open(path + "vedirect_and_console_port", "w")
        f.write(vedirect_and_console_port + "\n")
        f.close()

    # gpio pins with a relay connected
    relays = d.getVar("VE_RELAYS", True)
    if relays:
        f = open(path + "relays", "w")
        f.write(relays)
        f.close()
}
