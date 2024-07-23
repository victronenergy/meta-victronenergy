# make sure the host tools don't change bindir, only the target bindir
QT6_INSTALL_BINDIR := "${QT6_INSTALL_BINDIR}"

# changes bindir to point to a per package location
vedir := "/opt/victronenergy"
bindir := "${@oe.utils.conditional('VELIB_DEFAULT_DIRS', '1', '${bindir}', '${vedir}/${PN}', d)}"

# During image updates the complete rootfs gets reflashed. The permanent storage
# location will not get erased and is also available after such an update.
permanentdir = "/data"

# like sysconfdir, but will survive a image update (etc files go here)
permanentsysconfdir = "${permanentdir}/conf"

# persistant storage for applications (typically ${permanentlocalstatedir}lib/${PN} is used)
permanentlocalstatedir = "${permanentdir}/var"
