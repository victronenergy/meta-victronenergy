#!/bin/bash

# This script is called by signalk-server, to know which address
# to use when announcing to Navico devices. Fixes Navico plotters
# from getting stuck/slow due to continuously relaoding the SignalK
# app.
#
# https://github.com/victronenergy/venus-private/issues/260
#
# below command is the same as used by the navico advertiser for
# Venus OS
ip -o -4 addr show scope link | awk -F '[ /]+' '{ print $4 }'
