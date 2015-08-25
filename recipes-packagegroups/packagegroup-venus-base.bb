DESCRIPTION = "additional system packages"

PR = "r13"

inherit packagegroup
LICENSE = "MIT"

PACKAGES = "\
         packagegroup-venus-base \
         "

# build but not installed in the image
DEPENDS += "\
	gdb \
	git \
	s6 \
	tcpdump \
	tmux \
	vim \
"

RDEPENDS_${PN} += "\
	bash \
	bsdiff \
	bzip2 \
	connman \
	cronie \
	curl \
	dbus \
	e2fsprogs \
	eglibc-utils \
	iproute2 \
	iw \
	ldd \
	less \
	linux-firmware-rt2800 \
	linux-firmware-rt73 \
	linux-firmware-rtl8192cu \
	localedef \
	miniupnpc \
	mtd-utils \
	mtd-utils-ubifs \
	nano \
	ncurses-tools \
	openssh \
	openssh-sftp-server \
	opkg \
	opkg-collateral \
	rsync \
	screen \
	simple-upnpd \
	strace \
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
	u-boot-env-tools \
	usbutils \
	venus-version \
	watchdog \
	zip \
"

RDEPENDS_${PN}_ccgx += "\
	application \
"
