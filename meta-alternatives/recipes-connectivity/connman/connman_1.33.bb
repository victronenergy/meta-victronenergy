require connman.inc

SRC_URI  = "https://www.kernel.org/pub/linux/network/connman/connman-${PV}.tar.xz \
            file://connman \
            file://0001-test-use-python3.patch \
            file://CVE-2017-12865.patch \
            file://CVE-2021-26675.patch \
            file://0001-gdhcp-Avoid-reading-invalid-data-in-dhcp_get_option.patch \
            file://CVE-2021-26676.patch \
            file://CVE-2022-23096_CVE-2022-23097.patch \
            file://CVE-2022-23098.patch \
            file://0002-dnsproxy-Keep-timeout-in-TCP-case-even-after-connect.patch \
            file://0001-dnsproxy-Check-the-length-of-buffers-before-memcpy.patch \
            file://0002-gweb-Fix-OOB-write-in-received_data.patch \
            file://0003-wispr-Add-reference-counter-to-portal-context.patch \
            file://0004-wispr-Update-portal-context-references.patch \
            file://0005-gdhcp-Verify-and-sanitize-packet-length-first.patch \
            file://CVE-2025-32743.patch \
           "

SRC_URI[sha256sum] = "bc8946036fa70124d663136f9f6b6238d897ca482782df907b07a428b09df5a0"

PR = "${INC_PR}.0"
