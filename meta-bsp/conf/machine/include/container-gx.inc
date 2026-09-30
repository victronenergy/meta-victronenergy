# Generic Container GX machine, no real kernel/bootloader - the container runtime supplies it.
MACHINEOVERRIDES =. "container-gx:"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-dummy"
PREFERRED_PROVIDER_kernel-base ?= "linux-dummy"
PREFERRED_PROVIDER_kernel-image ?= "linux-dummy"

MACHINE_FEATURES = "ext2"

