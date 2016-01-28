# NOTE: this can be done a bit smarter since it is known which firmware is needed
# for fw in `find /lib/modules/ -name '*.ko' -exec modinfo -F firmware {} \;`; do echo -n "$fw "; if [ ! -f /lib/firmware/$fw ]; then echo "not shipped"; else echo ok; fi; done

PACKAGES =+ "${PN}-rt2800 ${PN}-rt73"
PR .= ".0"

#LICENSE_${PN}-rt2800 = "LICENCE.ralink-firmware.txt"
FILES_${PN}-rt2800 = " \
  /lib/firmware/rt2870.bin \
"

#LICENSE_${PN}-rt73 = "LICENCE.ralink-firmware.txt"
FILES_${PN}-rt73 = " \
  /lib/firmware/rt73.bin \
"
