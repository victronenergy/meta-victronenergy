# canutils and can-utils conflict, but only cansequence is needed
# so install that..
do_install () {
    install -d ${D}${bindir}
    install ${B}/src/cansequence ${D}/${bindir}
}
