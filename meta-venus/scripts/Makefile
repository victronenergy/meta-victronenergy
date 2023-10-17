SUBDIRS := $(wildcard [0-9]*/)
TGZ_FILES := $(addprefix venus-data-, $(SUBDIRS:%/=%.tgz))

all: $(TGZ_FILES)

venus-data-%.tgz: %
	tar -czf $@ --transform='s/$</rc/' --transform='s/rc-common/rc/' $</* rc-common/*

.PHONY: clean

clean:
	rm -f $(TGZ_FILES)
