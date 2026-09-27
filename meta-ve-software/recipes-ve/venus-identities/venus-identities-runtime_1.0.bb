SUMMARY = "Venus OS boot integration for persistent extension identities"
DESCRIPTION = "Installs immutable extension identity policy, creates live \
account files at early boot, and bind-mounts them over the system account files."
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = " \
    file://venus-identities.init \
    file://venus-identities-bind \
"
S = "${S_UNUSED}"

inherit allarch update-rc.d

RDEPENDS:${PN} = " \
    venus-identities \
    shadow \
    util-linux-mount \
    util-linux-umount \
"

INITSCRIPT_NAME = "venus-identities"
# /data validation starts at 03 and cleanup at 30. Identity publication must
# complete before runlevel-5 services can resolve or switch to extension users.
INITSCRIPT_PARAMS = "start 31 S ."

IDENTITIES_DIR = "/opt/victronenergy/venus-identities"

# Defaults permit the recipe to parse outside the Venus distro. venus.conf is
# authoritative for distributed Venus images and supplies the same variables.
VENUS_EXTENSION_UID_MIN ??= "2000"
VENUS_EXTENSION_UID_MAX_EXCLUSIVE ??= "3000"
VENUS_EXTENSION_GID_MIN ??= "2000"
VENUS_EXTENSION_GID_MAX_EXCLUSIVE ??= "3000"
VENUS_EXTENSION_SUBID_MIN ??= "165536"
VENUS_EXTENSION_SUBID_MAX_EXCLUSIVE ??= "65701536"
VENUS_EXTENSION_SUBID_COUNT ??= "65536"
VENUS_EXTENSION_APPROVED_GROUPS ??= "venus-dbus"

do_install() {
    install -d ${D}${IDENTITIES_DIR}
    install -m 0755 ${UNPACKDIR}/venus-identities-bind \
        ${D}${IDENTITIES_DIR}/venus-identities-bind

    printf '%s\n' \
        '# Generated from the Venus distro extension identity policy.' \
        'uid_min=${VENUS_EXTENSION_UID_MIN}' \
        'uid_max_exclusive=${VENUS_EXTENSION_UID_MAX_EXCLUSIVE}' \
        'gid_min=${VENUS_EXTENSION_GID_MIN}' \
        'gid_max_exclusive=${VENUS_EXTENSION_GID_MAX_EXCLUSIVE}' \
        'subid_min=${VENUS_EXTENSION_SUBID_MIN}' \
        'subid_max_exclusive=${VENUS_EXTENSION_SUBID_MAX_EXCLUSIVE}' \
        'subid_count=${VENUS_EXTENSION_SUBID_COUNT}' \
        'approved_groups=${VENUS_EXTENSION_APPROVED_GROUPS}' \
        'state_dir_mode=0700' \
        'state_file_mode=0600' \
        'live_dir_mode=0755' \
        'live_file_mode=0644' \
        'home_mode=0700' \
        'publish_command=${IDENTITIES_DIR}/venus-identities-bind' \
        > ${D}${IDENTITIES_DIR}/policy.conf

    install -d ${D}${sysconfdir}/init.d
    install -m 0755 ${UNPACKDIR}/venus-identities.init \
        ${D}${sysconfdir}/init.d/${INITSCRIPT_NAME}
}

FILES:${PN} = " \
    ${IDENTITIES_DIR}/venus-identities-bind \
    ${IDENTITIES_DIR}/policy.conf \
    ${sysconfdir}/init.d/${INITSCRIPT_NAME} \
"
