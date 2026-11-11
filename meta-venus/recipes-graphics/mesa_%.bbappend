# The canvu500 needs etnaviv and sunxi needs lima. Both need kmsro.
# The Raspberry Pi 2 / 3 / Zero 2 need vc4.
#
# NOTE: mesa is not MACHINE specific, so don't make this depend on the MACHINE.
# Whether the drivers end up in the image is determined by VENUS_GPU.
PACKAGECONFIG:append = " etnaviv kmsro lima vc4"
