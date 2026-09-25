DESCRIPTION = "Storage management service for Venus OS"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=73df9b989287b041065826041f5c002a"

inherit ve_package
inherit daemontools
inherit python-compile

SRC_URI = "gitsm://github.com/nmbath/venus-storage.git;branch=main;protocol=ssh;user=git"
SRCREV = "ec40224402983ab7865e81d2879ea5a85331450b"
S = "${WORKDIR}/git"

RDEPENDS:${PN} = " \
    python3-core \
    python3-dbus \
    python3-pygobject \
    e2fsprogs-mke2fs \
    dosfstools \
    eudev \
    parted \
    util-linux-blockdev \
    util-linux-lsblk \
    util-linux-mount \
    util-linux-umount \
    util-linux-nsenter \
    util-linux-sfdisk \
"

# Keep the early-boot protection used by the upstream service script, but
# reduce the USB-storage hotplug delay once normal userspace is running.
DAEMONTOOLS_SCRIPT = "usb_storage_delay=/sys/module/usb_storage/parameters/delay_use; if [ -w \"\${usb_storage_delay}\" ]; then echo 1 > \"\${usb_storage_delay}\"; fi; exec ${bindir}/dbus-storage"

do_install() {
    oe_runmake \
        DESTDIR=${D} \
        bindir=${bindir} \
        install_velib_python \
        install_app \
        install_handlers
}
