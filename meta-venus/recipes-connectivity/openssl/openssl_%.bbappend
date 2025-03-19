# lets not recommend installing legacy modules.
RRECOMMENDS:libcrypto:remove = "${PN}-ossl-module-legacy"
DEPRECATED_CRYPTO_FLAGS = "no-dtls1 no-dtls1-method no-tls1 no-tls1-method no-tls1_1 no-tls1_1-method"

do_install:append() {
    sed -i 's/\[openssl_init\]/[openssl_init]\nssl_conf = ssl_configuration/' ${D}${sysconfdir}/ssl/openssl.cnf

    cat <<EOF >> ${D}${sysconfdir}/ssl/openssl.cnf
[ssl_configuration]
system_default = tls_system_default

[tls_system_default]
MinProtocol = TLSv1.2
CipherString = ${OPENSSL_CIPHERSTRING_COLONS}
Ciphersuites = ${OPENSSL_CIPHERSUITES_COLONS}
EOF
}
