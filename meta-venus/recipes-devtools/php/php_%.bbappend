FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

PACKAGECONFIG="sqlite3"

# remove these, when mysql is not enabled building 7.2.9 fails with
# undefined reference to symbol 'dlopen@@GLIBC_2.4' otherwise.
CACHED_CONFIGUREVARS_remove = "ac_cv_func_dlopen=yes"
CACHED_CONFIGUREVARS_remove = "ac_cv_lib_dl_dlopen=yes"

php_sstate_postinst() {
    if [ "${BB_CURRENTTASK}" = "populate_sysroot_setscene" ]
    then
        head -n1 ${SYSROOT_DESTDIR}${sysconfdir}/pear.conf > ${SYSROOT_DESTDIR}${sysconfdir}/pear.tmp.conf
        for p in `tail -n1  ${SYSROOT_DESTDIR}${sysconfdir}/pear.conf | sed -s 's/;/ /g'`; do
            echo $p | awk -F: 'BEGIN {OFS = ":"; ORS = ";"}{if(NF==3){print $1, length($3)-2*match($3, /^"/), $3} else {print $0}}';
        done >> ${SYSROOT_DESTDIR}${sysconfdir}/pear.tmp.conf
        mv -f ${SYSROOT_DESTDIR}${sysconfdir}/pear.tmp.conf ${SYSROOT_DESTDIR}/${sysconfdir}/pear.conf
    fi
}
