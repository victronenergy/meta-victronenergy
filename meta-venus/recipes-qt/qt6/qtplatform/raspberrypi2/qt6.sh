# The vc4 KMS driver is only there when dtoverlay=vc4-kms-v3d is set in
# config.txt. Since the boot partition isn't updated by swupdate, older
# installations still use the firmware framebuffer, so fall back to linuxfb.
if [ -e /dev/dri/by-path/platform-soc:gpu-card ]; then
    export QT_QPA_PLATFORM=eglfs
    export QT_QPA_EGLFS_INTEGRATION=eglfs_kms
    export QT_QPA_EGLFS_HIDECURSOR=0
    export QT_QPA_KMS_CONFIG=/etc/qt-kms.conf
    export XDG_RUNTIME_DIR=/var/volatile/tmp/runtime-root
else
    export QT_QPA_PLATFORM=linuxfb
fi
