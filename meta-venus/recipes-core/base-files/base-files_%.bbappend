VELIB_DEFAULT_DIRS = "1"
inherit ve_package

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://fstab.mmc1 \
    file://fstab.ubi \
"

# Add the mount point for the data partition
dirs755 += " ${permanentdir}"

# mount point for u-boot FAT partition on raspberrypi2.
dirs755:append:rpi = " /u-boot"

do_compile:append() {
    for f in ${FSTAB}; do
        cat fstab.${FSTAB} >>fstab
    done
}

# Replace home dir with symlink to persistent volume
do_install:append() {
    if [ -d ${D}/home/root ]; then
        rmdir ${D}/home/root
        ln -s ${permanentdir}/home/root ${D}/home/root
    fi

    if [ -d ${D}/media ]; then
        rmdir ${D}/media
        ln -s /run/media ${D}/media
    fi

    if [ -d ${D}${localstatedir}/log ]; then
        rm -rf ${D}${localstatedir}/log
    fi

    ln -sf ${permanentdir}/log ${D}${localstatedir}/log
}
