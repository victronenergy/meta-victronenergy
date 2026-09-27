# shadow ships /etc/subuid and /etc/subgid as empty stub files. Add the
# subordinate UID/GID range for the rootless "container" user (see
# podman-rootless_1.0.bb) here, since that recipe can't install its own
# copies without clashing with these.
do_install:append() {
    echo "container:100000:65536" >> ${D}${sysconfdir}/subuid
    echo "container:100000:65536" >> ${D}${sysconfdir}/subgid
}
