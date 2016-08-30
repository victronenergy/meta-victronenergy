inherit ve_package

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Add the mount point for the data partition
dirs755 += " /data"

# mount point for the scratch partition
dirs755 += "/scratch"

# Replace home dir with symlink to persistent volume
do_install_append() {
	if [ -d ${D}/home/root ]; then
		rmdir ${D}/home/root
		ln -s ${permanentdir}/home/root ${D}/home/root
	fi
}
