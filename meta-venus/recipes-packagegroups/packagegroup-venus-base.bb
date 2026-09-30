SUMMARY = "Venus OS for hardware images"
DESCRIPTION = " \
    packagegroup-venus-core plus everything only real Venus hardware \
    needs: kernel support, local networking (connman, bluez5, dnsmasq, \
    avahi-autoipd), on-board wireless firmware, a local display fallback \
    (javascript-vnc-client, pointercal), a watchdog device, and the like. \
"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

RDEPENDS:${PN} += "\
    avahi-autoipd \
    bluez5 \
    bluez5-noinst-tools \
    connman \
    connman-tools \
    dnsmasq \
    javascript-vnc-client \
    linux-firmware-mt7601u \
    linux-firmware-mt7662 \
    linux-firmware-rt2800 \
    linux-firmware-rt73 \
    linux-firmware-rtl8192cu \
    linux-firmware-rtl-bt \
    opkg \
    pointercal \
    ppp \
    rtl8192eu \
    watchdog \
    wireless-regdb-static \
"

RDEPENDS:${PN} += "\
    packagegroup-base \
    packagegroup-venus-core \
    packagegroup-venus-machine \
"
