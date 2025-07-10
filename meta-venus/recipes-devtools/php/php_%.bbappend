FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PACKAGECONFIG = ""

inherit useradd

USERADD_PACKAGES = "${PN}-fpm"
USERADD_PARAM:${PN}-fpm = "--system --no-create-home --shell /bin/false --user-group php-fpm"

do_install:append:class-target() {
    sed -e 's/^pm = dynamic$/pm = ondemand/g' -i "${D}${sysconfdir}/php-fpm.conf"
}

# Common EXTRA_OECONF
COMMON_EXTRA_OECONF = " \
                       --enable-shared \
                       --disable-rpath \
                       --with-pic \
                       --libdir=${PHP_LIBDIR} \
"
EXTRA_OECONF = "--enable-fpm \
                --with-libdir=${baselib} \
                --with-config-file-path=${sysconfdir}/php/apache2-php${PHP_MAJOR_VERSION} \
                ${@oe.utils.conditional('SITEINFO_ENDIANNESS', 'le', 'ac_cv_c_bigendian_php=no', 'ac_cv_c_bigendian_php=yes', d)} \
                ${@bb.utils.contains('PACKAGECONFIG', 'pam', '', 'ac_cv_lib_pam_pam_start=no', d)} \
                ${COMMON_EXTRA_OECONF} \
"

PACKAGECONFIG[opcache] = "--enable-opcache,--disable-opcache --disable-opcache-jit"
PACKAGECONFIG[sockets] = "--enable-sockets,--disable-sockets"
PACKAGECONFIG[zlib] = "--with-zlib=${STAGING_LIBDIR}/..,-without-zlib,zlib"
PACKAGECONFIG[iconv] = "--with-iconv=${STAGING_LIBDIR}/..,--without-iconv,virtual/libiconv"
PACKAGECONFIG[gettext] = "--with-gettext=${STAGING_LIBDIR}/..,--without-gettext"
PACKAGECONFIG[bz2] = "--with-bz2=${STAGING_DIR_TARGET}${exec_prefix},--without-bz2,bzip2"
PACKAGECONFIG[libxml] = ",--without-libxml --disable-dom --disable-xml --disable-simplexml --disable-xmlreader --disable-xmlwriter,libxml2"
PACKAGECONFIG[pcntl] = "--enable-pcntl"
PACKAGECONFIG[fileinfo] = ",--disable-fileinfo"
PACKAGECONFIG[pdo] = ",--disable-pdo"
PACKAGECONFIG[phar] = ",--disable-phar"
PACKAGECONFIG[filter] = ",--disable-filter"
PACKAGECONFIG[posix] = ",--disable-posix"
PACKAGECONFIG[tokenizer] = ",--disable-tokenizer"
