# disallow root logins with a password
do_install_append () {
    if [ -e ${D}${datadir}/base-passwd/passwd.master ]; then
        sed -i 's#root::0:0:root:#root:*:0:0:root:#' ${D}${datadir}/base-passwd/passwd.master
    fi
}
