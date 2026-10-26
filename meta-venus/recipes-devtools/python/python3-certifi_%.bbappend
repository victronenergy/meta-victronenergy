FILESEXTRAPATHS:prepend := "${THISDIR}/python3-certifi:"

SRC_URI += "file://0001-Remove-the-emSign-ECC-Root-CA-G3-certificate.patch \
            file://0002-Remove-the-Comodo-AAA-Services-root-certificate.patch \
"

PR = "2"
