inherit daemontools

DAEMONTOOLS_RUN = "x11vnc -rawfb /dev/fb0 -shared -forever -pipeinput UINPUT:abs=1 -rfbport 5901 -rfbportv6 5901"
