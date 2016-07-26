DESCRIPTION = "additional system packages"

PR = "r18"

inherit packagegroup
LICENSE = "MIT"

# build but not installed in the image
DEPENDS += "\
	gdb \
	git \
	packagegroup-core-buildessential \
	s6 \
	tcpdump \
	tmux \
	vim \
"

RDEPENDS_${PN} += "\
	bash \
	bsdiff \
	bzip2 \
	ca-certificates \
	canutils \
	connman \
	connman-tools \
	cronie \
	curl \
	dbus \
	dbus-tools \
	e2fsprogs \
	eglibc-utils \
	gdbserver \
	iproute2 \
	iw \
	javascript-vnc-client \
	ldd \
	less \
	linux-firmware-rt2800 \
	linux-firmware-rt73 \
	linux-firmware-rtl8192cu \
	localedef \
	nano \
	ncurses-tools \
	openssh \
	openssh-sftp-server \
	opkg \
	opkg-collateral \
	rsync \
	screen \
	socat \
	strace \
	sysctl-conf \
	tzdata \
	tzdata-africa \
	tzdata-americas \
	tzdata-antarctica \
	tzdata-arctic \
	tzdata-asia \
	tzdata-atlantic \
	tzdata-australia \
	tzdata-europe \
	tzdata-pacific \
	usbutils \
	venus-version \
	watchdog \
	websockify-c \
	zip \
"
