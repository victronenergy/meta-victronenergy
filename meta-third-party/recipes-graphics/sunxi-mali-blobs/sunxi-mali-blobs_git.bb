DESCRIPTION = "Mali OpenGL ES userspace libraries for Allwinner SoCs"
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://EULA%20for%20Mali%20400MP%20_AW.pdf;md5=495406406519c27072a3e6f1e825c0a8"

COMPATIBLE_MACHINE = "sunxi"

PROVIDES += "virtual/egl virtual/libgles1 virtual/libgles2"

inherit insane

SRC_URI = "\
	git://github.com/bootlin/mali-blobs.git;protocol=https \
	file://egl.pc \
	file://glesv1_cm.pc \
	file://glesv2.pc \
"
SRCREV = "418f55585e76f375792dbebb3e97532f0c1c556d"
S = "${WORKDIR}/git"

MALI_REV ?= "r8p1"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    sed -i -e "s:PREFIX:${prefix}:" -e "s:VERSION:${MALI_REV}:" ${WORKDIR}/*.pc

    install -d ${D}${libdir}/pkgconfig
    install -m 0644 ${WORKDIR}/*pc ${D}${libdir}/pkgconfig

    install -d ${D}${includedir}
    cp -a ${S}/include/fbdev/* ${D}${includedir}

    install -d ${D}${libdir}
    cp --no-dereference --preserve=mode,links ${S}/${MALI_REV}/${TARGET_ARCH}/fbdev/lib* ${D}${libdir}
}

FILES_${PN} = "${libdir}/lib*.so*"
FILES_${PN}-dev = "${includedir} ${libdir}/pkgconfig"

INSANE_SKIP_${PN} += "dev-so already-stripped"
