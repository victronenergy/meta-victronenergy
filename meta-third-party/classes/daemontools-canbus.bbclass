# note: a profile select the bitrate, hence serves are only started after
# the bitrate is set and hence down by default.
DAEMONTOOLS_SERVICE_SYMLINK = "0"
DAEMONTOOLS_DOWN = "1"
DAEMONTOOLS_LOG_DIR ?= "${DAEMONTOOLS_LOG_DIR_PREFIX}/${PN}.DEV"

inherit daemontools

do_per_canbus_service() {
	# make sure the service dir exists and is empty
	if [ -e "${D}${DAEMONTOOLS_SERVICES_DIR}" ]; then
		rm -rf "${D}${DAEMONTOOLS_SERVICES_DIR}"
	fi
	install -d ${D}${DAEMONTOOLS_SERVICES_DIR}

	# make a service per CAN bus interface
	for dev in ${VE_CAN_PORTS}
	do
		SERVICE="${D}${DAEMONTOOLS_SERVICES_DIR}/${PN}.$dev"
		cp -r ${D}${DAEMONTOOLS_SERVICE_DIR} $SERVICE

		# patch run files for CAN-bus device
		sed -i "s:DEV:$dev:" "$SERVICE/run"
		sed -i "s:DEV:$dev:" "$SERVICE/log/run"

		# FIXME: symlink the first service, as long as the profile selection
		# in the gui does not support multiple CAN-busses, so things don't break.
		if [ ! -e "${D}${DAEMONTOOLS_SERVICES_DIR}/${PN}" ]; then
			ln -sf "${DAEMONTOOLS_SERVICES_DIR}/${PN}.$dev" "${D}${DAEMONTOOLS_SERVICES_DIR}/${PN}"
		fi
	done
}

addtask per_canbus_service after do_install before do_package
