require php.inc

LIC_FILES_CHKSUM = "file://LICENSE;md5=464ca70af214d2407f6b7d4458158afd"

PR = "${INC_PR}.0"

SRC_URI += " \
		file://acinclude-xml2-config.patch \
		file://0001-php-don-t-use-broken-wrapper-for-mkdir.patch \
		file://0001-PATCH-3-8-acinclude-use-pkgconfig-for-libxml2-config.patch \
		file://0001-brutal-hack-to-make-cross-compiling-work-for-linux.patch \
		file://0001-workaround-dlopen-not-correctly-polled.patch \
		file://0001-AC_TRY_RUN-won-t-work-when-cross-compiling.patch \
	"

SRC_URI_append_pn-php += "file://iconv.patch \
            file://pear-makefile.patch \
            file://phar-makefile.patch \
            file://php_exec_native.patch \
            file://php-fpm.conf \
            file://php-fpm-apache.conf \
"

SRC_URI[md5sum] = "2a0eadad872978ae57e6756187625c00"
SRC_URI[sha256sum] = "fbcee579ecc77cad6960a541116aee669cf145c2cd9a54bf60503a870843b946"
