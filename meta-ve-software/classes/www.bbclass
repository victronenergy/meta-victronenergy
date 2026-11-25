WWW_LOCALHOST = "/var/www/localhost"
WWW_ROOT = "/var/www/venus"
WWW_RCD = "${sysconfdir}/venus/www.d"

NGINX_USER = "www-data"

# The unix socket php-fpm listens on and nginx passes php requests to.
PHP_FPM_SOCKET = "/var/run/php-fpm.socket"

PHP_USER ?= "php-fpm"
PHP_GROUP ?= "php-fpm"
