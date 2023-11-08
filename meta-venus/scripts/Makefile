SUBDIRS := $(wildcard */)
TGZ_FILES := $(addprefix venus-data-, $(SUBDIRS:%/=%.tgz))

all: $(TGZ_FILES)

venus-data-%.tgz: %
	tar -czf $@ -C $< --transform 's,^./,rc/,' .

.PHONY: clean

clean:
	rm -f $(TGZ_FILES)
