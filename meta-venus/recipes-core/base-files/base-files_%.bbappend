VELIB_DEFAULT_DIRS = "1"
inherit ve_package

FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

# Add the mount point for the data partition
dirs755 += " ${permanentdir}"

# mount point for the scratch partition
dirs755 += "/scratch"

# mount point for u-boot FAT partition on raspberrypi2.
dirs755_append_raspberrypi2 += "/u-boot"

# Replace home dir with symlink to persistent volume
do_install_append() {
    if [ -d ${D}/home/root ]; then
        rmdir ${D}/home/root
        ln -s ${permanentdir}/home/root ${D}/home/root
    fi

    # FIXME: be compatible with the ccgx locations for now
    # Mind it, don't make it an absolute link. When mounting the backup partition
    # it will point to the active one in that case!
    mkdir -p ${D}/opt
    ln -s victronenergy ${D}/opt/color-control

    if [ -d ${D}/media ]; then
        rmdir ${D}/media
        ln -s /run/media ${D}/media
    fi

    if [ -d ${D}${localstatedir}/log ]; then
        rmdir ${D}${localstatedir}/log
    fi
    ln -sf ${permanentdir}/log ${D}${localstatedir}/log
}
