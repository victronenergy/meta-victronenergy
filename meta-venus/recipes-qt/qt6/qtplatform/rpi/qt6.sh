export QT_QPA_PLATFORM=eglfs
export QT_QPA_EGLFS_INTEGRATION=eglfs_kms

if [ -e /sys/class/backlight/11-0045/max_brightness ]; then
	cat /sys/class/backlight/11-0045/max_brightness > /sys/class/backlight/11-0045/brightness
else
	export QT_QPA_EGLFS_KMS_CONFIG=/etc/qt-kms.conf
fi
