# build but not installed in the image
inherit packagegroup
LICENSE = "MIT"

DEPENDS += "\
	devmem2 \
	gdb \
	git \
	nodejs \
	openjdk-8 \
	packagegroup-core-buildessential \
	s6 \
	tcpdump \
	tinymembench \
	tmux \
	valgrind \
	vim \
"
