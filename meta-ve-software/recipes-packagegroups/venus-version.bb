LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"
S = "${WORKDIR}"

# Not really machine specific actually, but the nostamp will cause the task to be
# rerun per machine per build in a multiconfig setup instead of only once per build,
# since the signature is changed per machine because of the nostamp (which causes
# trouble, the same tasks are run on the same directory in parallel).
#
# That is not easily solved:
#  - The recipe cannot depend on DATATIME itself, since bitbake will consider the
#    meta-data changing and doesn't like that.
#  - The mechanism to rerun the task is by changing the signature (so dependend
#    tasks rerun as well).
#  - The mechanism to check that tasks don't need to be run per MACHINE is based
#    on checking that their signatures are equal.
#
# So just make it machine specific, despite the task being completely the same.
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit ve_package

do_configure () {
    printf "${DISTRO_VERSION}\n${DISTRO_NAME}\n${DATE}${TIME}\n" > version
}

do_configure[nostamp] = "1"
do_configure[vardepsexclude] = "DATE TIME"

do_install () {
    install -d ${D}${vedir}
    install -m 644 version ${D}${vedir}
}

FILES:${PN} += "${vedir}"
