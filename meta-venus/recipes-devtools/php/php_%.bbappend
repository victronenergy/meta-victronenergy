FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PACKAGECONFIG:remove = "mysql imap"

inherit useradd

USERADD_PACKAGES = "${PN}-fpm"
USERADD_PARAM:${PN}-fpm = "--system --no-create-home --shell /bin/false --user-group php-fpm"

SRC_URI_append = " \
    file://CVE-2024-5458.patch \
    file://CVE-2026-109618-CVE-2026-109619.patch \
"

# not-applicable-platform: Issue only applies on Windows (BatBadBut command injection via cmd.exe)
CVE_CHECK_WHITELIST += "CVE-2024-3566"

PR = "1"
