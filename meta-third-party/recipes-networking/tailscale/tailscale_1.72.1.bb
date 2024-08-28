# tailscale_1.0.5.bb
SUMMARY = "Tailscale client and daemon for Linux"
HOMEPAGE = "github.com/tailscale/tailscale"
SECTION = "net"

LICENSE = "CLOSED"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=d995c1c44529856a0f35a5ad43e51cc5"

SRC_URI = "git://github.com/tailscale/tailscale.git;nobranch=1;tag=v${PV};destsuffix=git/src/${GO_IMPORT}"
S = "${WORKDIR}/git"

GO_IMPORT = "tailscale.com"
GO_WORKDIR = "${GO_IMPORT}"
GO_INSTALL = "${GO_IMPORT}/cmd/tailscaled"

# from tailscale build script to deliver a small binary and combined
# tailscale and tailscaled binary
GOBUILDFLAGS:prepend="-tags=ts_omit_aws,ts_omit_bird,ts_omit_tap,ts_omit_kube,ts_omit_completion,ts_include_cli "
# from the tailscale build script "-w -s" is suggested, however can remove -s 
# as build environment when packaging will strip the binaries.
GO_EXTLDFLAGS += "-w"
# build executable instead of shared object
GO_LINKSHARED = ""
GOBUILDFLAGS:remove = "-buildmode=pie"

FILES_${PN} += "${GOBIN_FINAL}/*"
INSANE_SKIP_${PN} += "already-stripped"


inherit go go-mod daemontools

DAEMONTOOLS_RUN = "${sbindir}/tailscaled -no-logs-no-support -statedir /data/conf/tailscale"
DAEMONTOOLS_DOWN = "1"

do_compile[network] = "1"

do_install() {
    install -d ${D}/${bindir}
    install -d ${D}/${sbindir}
    install -m 755 ${B}/bin/linux_arm/tailscaled ${D}/${sbindir}/tailscaled
    ln -s -r ${D}/${sbindir}/tailscaled ${D}/${bindir}/tailscale
}

 


