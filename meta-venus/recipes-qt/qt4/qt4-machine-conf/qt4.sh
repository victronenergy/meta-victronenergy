if [ -e /dev/input/touchscreen0 ]; then
    export QWS_MOUSE_PROTO=linuxinput:/dev/input/touchscreen0
fi
