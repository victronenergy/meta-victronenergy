# build but not installed in the image
inherit packagegroup
LICENSE = "MIT"

DEPENDS += "\
	devmem2 \
	gdb \
	git \
	nodejs \
	openvpn \
	packagegroup-core-buildessential \
	python-pylint \
	s6 \
	tcpdump \
	tinymembench \
	tmux \
	valgrind \
	venus-socketcan-test \
	vim \
	x11vnc \
"
