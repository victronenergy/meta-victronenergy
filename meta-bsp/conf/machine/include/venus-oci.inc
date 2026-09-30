# Generic OCI container machine, no real kernel/bootloader - the container runtime supplies it.
MACHINEOVERRIDES =. "venus-oci:"

PREFERRED_PROVIDER_virtual/kernel ?= "linux-dummy"
PREFERRED_PROVIDER_kernel-base ?= "linux-dummy"
PREFERRED_PROVIDER_kernel-image ?= "linux-dummy"

MACHINE_FEATURES = "ext2"

# No Classic UI, VNC display or VE.Smart server in the OCI image
RDEPENDS:packagegroup-ve-addons:remove = "gui vesmart-server"
RDEPENDS:packagegroup-ve-console-apps:remove = "javascript-vnc-client"
START_GUI = ""
