
# Since /var/lib is an overlay with an readonly rootfs, move opkg data to a
# different place. See also venus.conf.
#
# TODO: check why this much be adjusted manually, OPKGLIBDIR is an OE variable.
do_install_append() {
    echo "option info_dir ${OPKGLIBDIR}/opkg/info" >>${D}${sysconfdir}/opkg/opkg.conf
    echo "option status_file ${OPKGLIBDIR}/opkg/status" >>${D}${sysconfdir}/opkg/opkg.conf
}
