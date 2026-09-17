#!/bin/sh
# daemontools service, not an rcS.d script - dbus-daemon itself only comes
# up once svscan starts supervising /service, after rcS.d has already
# finished, so this can't be an rcS.d script without deadlocking boot.

until dbus-send --system --print-reply --dest=com.victronenergy.settings /Settings com.victronenergy.Settings.AddSetting \
                string:Gui string:RunningVersion variant:int32:2 string:"i" variant:int32:1 variant:int32:2 ; do
    sleep 1
done

dbus-send --system --print-reply --dest=com.victronenergy.settings /Settings/Gui/RunningVersion com.victronenergy.BusItem.SetValue variant:int32:2

# One-shot: sleep rather than exit, so daemontools doesn't respawn-loop
# this forever re-running an already-done SetValue.
exec sleep infinity
