DESCRIPTION = "Boot partition image"
LICENSE = "MIT"

inherit deploy image-artifact-names nopackages

PACKAGE_ARCH = "${MACHINE_ARCH}"

BOOT_IMAGE_SIZE = "8192"
BOOT_IMAGE_SIZE:rpi = "90000"

do_deploy[depends] += "\
    dosfstools-native:do_populate_sysroot \
    mtools-native:do_populate_sysroot \
    virtual/bootloader:do_deploy \
    virtual/kernel:do_deploy \
"

do_configure[noexec] = "1"
do_compile[noexec] = "1"
do_install[noexec] = "1"

python do_deploy () {
    import os, subprocess

    bootimg = d.expand("${DEPLOYDIR}/${IMAGE_NAME}.vfat")
    cmd = d.expand(f"mkfs.vfat -S 512 -C {bootimg} ${{BOOT_IMAGE_SIZE}}")
    if not subprocess.run(cmd, shell=True, check=True):
        bb.fatal(f"creating {bootimg} failed")

    files = d.getVar("IMAGE_BOOT_FILES")
    for file in files.split():
        parts = file.split(";")
        destdir = os.path.dirname(file) if len(parts) != 2 else parts[1]
        file = parts[0]

        tgfile = os.path.basename(file)
        if tgfile == "*":
            tgfile = ""

        dest = os.path.join(destdir, tgfile)
        if dest != "":
            mmdcmd = f"mmd -i {bootimg} ::/{destdir}"
            subprocess.run(mmdcmd, shell=True)

        mcopy = d.expand(f"mcopy -i {bootimg} -s ${{DEPLOY_DIR_IMAGE}}/{file} ::/{dest}")
        subprocess.run(mcopy, shell=True, check=True)

    subprocess.run(f"gzip {bootimg}", shell=True, check=True)
    tg = d.expand("${DEPLOYDIR}/${IMAGE_LINK_NAME}.vfat.gz")
    subprocess.run(f"ln -sfr {bootimg}.gz {tg}", shell=True, check=True)
}

addtask do_deploy before do_build
