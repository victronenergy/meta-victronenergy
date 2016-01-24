LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb"

SRC_URI += "https://github.com/victronenergy/u-boot/archive/v2013.01.01-ccgx-v3.tar.gz"
SRC_URI[sha256sum] = "ee3d73d6f05879426a0d60df56562b5500f8df475921af185a3301abb44f505b"
SRC_URI[md5sum] = "730e07ad5b5122fdf1d8f99588c2e9cc"
S = "${WORKDIR}/u-boot-2013.01.01-ccgx-v3"

FILESEXTRAPATHS_prepend := "${THISDIR}/u-boot-ccgx:"

SRC_URI += " \
	file://0001-import-compiler-gcc5-from-v2015.10.patch \
	file://0002-net-davinci_emac-don-t-teardown-inactive-rx-channel.patch \
	file://0003-inline-use-the-gcc-inline-version-instead-of-the-c99.patch \
	file://0004-don-t-error-on-a-warning.patch \
	file://0005-add-libubootenv.a-needed-by-swupdate.patch \
"
