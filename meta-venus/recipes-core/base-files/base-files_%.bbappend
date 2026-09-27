VELIB_DEFAULT_DIRS = "1"
inherit ve_package

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "\
    file://fstab.mmc0 \
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

# Mount the cgroup v2 unified hierarchy at boot, for containers -
# meta-victronenergy's own container-cgroup-delegation init script
# (meta-venus's podman-rootless recipe) depends on /sys/fs/cgroup already
# being a live cgroup2 mount by the time it runs, and silently no-ops
# through every step otherwise. Was raspberrypi4-64-only for a while;
# missed when raspberrypi5 support was added, which left that whole
# delegation chain silently broken there - found live (2026-09-01) on a
# fresh raspberrypi5 test device via its knock-on effects (podman/crun
# cgroup writes falling back to the undelegated root cgroup, inflating
# `podman stats` CPU% for every container sharing it, and "Permission
# denied" writing a managed child's own nested cgroup.procs). Extended to
# every other currently supported machine at the same time, rather than
# rediscovering this one machine at a time - each machine gets its own
# override (not a single case-statement do_install:append()) so this
# never collides with the unconditional do_install:append() below.
do_install:append:raspberrypi4-64() {
    echo "cgroup2                /sys/fs/cgroup       cgroup2    defaults              0  0" >> ${D}${sysconfdir}/fstab
}

do_install:append:raspberrypi5() {
    echo "cgroup2                /sys/fs/cgroup       cgroup2    defaults              0  0" >> ${D}${sysconfdir}/fstab
}

do_install:append:raspberrypi4() {
    echo "cgroup2                /sys/fs/cgroup       cgroup2    defaults              0  0" >> ${D}${sysconfdir}/fstab
}

do_install:append:am62xx() {
    echo "cgroup2                /sys/fs/cgroup       cgroup2    defaults              0  0" >> ${D}${sysconfdir}/fstab
}

do_install:append:am62xx-k3r5() {
    echo "cgroup2                /sys/fs/cgroup       cgroup2    defaults              0  0" >> ${D}${sysconfdir}/fstab
}

do_install:append:einstein() {
    echo "cgroup2                /sys/fs/cgroup       cgroup2    defaults              0  0" >> ${D}${sysconfdir}/fstab
}

do_install:append:ekrano() {
    echo "cgroup2                /sys/fs/cgroup       cgroup2    defaults              0  0" >> ${D}${sysconfdir}/fstab
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
