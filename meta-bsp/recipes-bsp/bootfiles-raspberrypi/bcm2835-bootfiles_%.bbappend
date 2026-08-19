FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://config.txt"

# The RPi firmware injects cgroup_disable=memory into the DTB-provided
# base cmdline ahead of whatever is in this file (confirmed by RPi's own
# maintainers: it's deliberate upstream behaviour, "memory cgroup
# available but disabled by default"). cgroup_enable=memory is the
# sanctioned override - it works because this file's content is applied
# after the DTB's bootargs, so ordering makes the override win. This
# applies to every board that boots via the RPi firmware/DTB chain.
#
# CONFIG_CPUSETS_V1 is compiled out for raspberrypi4-64 and raspberrypi5
# (see the kernel recipe patches), so cgroup_no_v1=all is no longer
# carried here - if v1 leakage from some other controller turns up in
# testing, reinstate it.
RPI_EXTRA_CMDLINE = ""
RPI_EXTRA_CMDLINE:raspberrypi4-64 = " cgroup_enable=memory"
RPI_EXTRA_CMDLINE:raspberrypi5 = " cgroup_enable=memory"
RPI_EXTRA_CMDLINE:raspberrypi4 = " cgroup_enable=memory"

do_deploy:append() {
    install -m 640 ${UNPACKDIR}/config.txt ${DEPLOYDIR}/config.txt
    echo "dwc_otg.lpm_enable=0 console=serial0,115200 usb-storage.delay_use=20${RPI_EXTRA_CMDLINE}" > ${DEPLOYDIR}/cmdline.txt
}
