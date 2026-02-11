# The canvu500 needs etnaviv and sunxi needs lima. Both need kmsro.
PACKAGECONFIG:append = " etnaviv kmsro lima"

PACKAGECONFIG:remove:rpi = "wayland"
PACKAGECONFIG:remove:rpi = "etnaviv"
PACKAGECONFIG:remove:rpi = "lima"
PACKAGECONFIG:append:rpi = " gallium vc4 v3d"
