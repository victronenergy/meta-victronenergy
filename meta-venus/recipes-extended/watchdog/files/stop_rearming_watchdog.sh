#!/bin/sh

# note: the only thing left todo is unmounting filesystems and reboot. Occasionally
# this seems to fail on a ccgx. Hence this script guards it by the hw watchdog.
# The watchdog process is kept alive (see S20sendsigs) so the watchdog device is still open
# and refreshed. This process is now stopped, so the file is still open, and no longer
# refreshed, iow it will trigger a watchdog reset if the kernel fails to reboot itself.
killall -STOP watchdog

