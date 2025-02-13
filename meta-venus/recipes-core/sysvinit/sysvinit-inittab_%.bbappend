FILESEXTRAPATHS:prepend := "${THISDIR}/sysvinit-inittab:"

SRC_URI += "file://autologin"

do_install:append () {
    install -d ${D}/${base_sbindir}
    install -m 0755 ${UNPACKDIR}/autologin ${D}/${base_sbindir}

    ORIG_IFS="$IFS"
    IFS='|'
    inittab="${INITTAB}"
    for line in $inittab; do
        line="$(printf "%s" "$line" | sed 's/^[[:blank:]]*//;s/[[:blank:]]*$//')"
        printf "%s\n" "$line" >> ${D}${sysconfdir}/inittab
    done
    IFS="${ORIG_IFS}"
}

FILES:${PN} += "${base_sbindir}"
