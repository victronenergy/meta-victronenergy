# The canvu500 needs etnaviv and sunxi needs lima. Both need kmsro.
PACKAGECONFIG:append = " etnaviv kmsro lima"

# raspberrypi4-64: BCM2711s GPU is split across two gallium drivers - vc4
# handles KMS/display, v3d handles 3D rendering - and like etnaviv/lima
# above, that render/display split needs kmsro to bridge them. Without
# this, mesa-megadriver ships no vc4_dri.so/v3d_dri.so at all and EGL
# silently falls back to software rendering (kms_swrast).
PACKAGECONFIG:append:raspberrypi4-64 = " vc4 v3d kmsro"
