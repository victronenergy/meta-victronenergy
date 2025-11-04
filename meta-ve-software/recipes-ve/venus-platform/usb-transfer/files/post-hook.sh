#!/bin/sh

basedir=$(dirname $0)

# print script path
# Script path: /tmp/tmp.wmvGlg/rc/post-hook.sh
# echo "Script path: ${basedir}" >> /data/test.log

. ${basedir}/functions.sh

script="restore tank setups from USB drive"

echo "### ${script} starting"


# could also run on boot, check if service localsettings is up and running
svstatOutput=$(svstat /service/localsettings)

echo "Check if needed dbus service is available: ${svstatOutput}"
if ! echo "${svstatOutput}" | grep -q "pid"; then
    exit 1
fi


# dbus -y com.victronenergy.settings /Settings AddSetting System/UsbTransfer/Tank DisableAutoImport 0 i 0 1
# dbus -y com.victronenergy.settings /Settings RemoveSettings '%["System/UsbTransfer/Tank/DisableAutoImport"]'

# Check if DisableAutoImport is enabled (everything other than 0 is considered enabled)
if [ "$(dbus -y com.victronenergy.settings /Settings/System/UsbTransfer/Tank/DisableAutoImport GetValue)" != "0" ]; then
    dbus -y com.victronenergy.platform /Notifications/Inject SetValue "2	Tank Setups Import	Automatic import of tank setups is disabled in the settings."
    exit 0
fi



notify "Import tank setups..." --no-beep

/usr/bin/python ${basedir}/usb-transfer.py --import > /tmp/usb-transfer.log 2>&1

error_code=$?

# Check if the import was successful
if [ $error_code -ne 0 ]; then
    # Abort due to error, message is printed in the python script
    # Show error in GUIv2
    # dbus -y com.victronenergy.platform /Notifications/Inject SetValue "0	Tank Setups Import	Error during the import of the tank setups. Error code: ${error_code}"
    exit 1
fi


# Eject the USB drive
if [ "$(dbus -y com.victronenergy.logger /Storage/MountState GetValue)" = "1" ]; then
    dbus -y com.victronenergy.logger /Storage/MountState SetValue %2
    # Output: 2
fi


dbus -y com.victronenergy.platform /Notifications/Inject SetValue "2	Tank Setups Import	The tank setups were imported from the USB drive. You can remove the USB now."

# Play notification to let the user know the script is done
notify "Tank setups imported"

echo "### ${script} done"


exit 0
