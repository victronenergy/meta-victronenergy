#!/bin/sh

usage () {
	echo usage
	exit
}

if [ $# -lt 2 ]; then
	echo usage setting value [till_version]
	echo 
	echo forces a setting to another value
	echo
	echo typically used to change a wrong or no longer valid value
	echo during an update. params are:
	echo
	echo - setting: the last part of the setting path. mind it, this script
	echo will update all occurrences!
	echo
	echo - value: the value to set
	echo - version: the last version to do fix a setting for.

	exit
fi

setting=$1
value=$2

settings_file=/data/conf/settings.xml
version_file=/opt/color-control/localsettings/previous_version

if [ $# -eq 3 ]; then
	till_version=$3

	ver=`head -n1 ${version_file} | sed -e "s/^v\([0-9.]*\).*$/\1/g" -e "s/\./ /g" | awk '{print $1 * 100 + $2};'`

	if [ $ver -gt $till_version ]; then
		echo no change needed, $ver $till_version
		exit
	fi
fi

sed -i -e "s,\(<${setting}[^>]*>\)\(.*\)\(</${setting}>\),\1${value}\3,g" $settings_file

