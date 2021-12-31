#!/bin/sh

# note: this used to be an overlayfs, but modifying the lower layer of an
# overlayfs is undefined, and causes trouble when making the rootfs writable.
# The overlayfs cannot be unmounted since it is in use. So instead copy the
# stored files to a ramdisk, so deleting a file from the ramdisk and the storage
# is no longer undefined behaviour.
mkdir -p /run/overlays/service
cp -a /opt/victronenergy/service/* /run/overlays/service
mount --bind /run/overlays/service /service
