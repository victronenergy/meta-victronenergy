#include <fcntl.h>
#include <stdlib.h>
#include <stdio.h>

#include <linux/watchdog.h>
#include <sys/ioctl.h>

/*
 * Note: the ioctrl WDIOC_GETBOOTSTATUS ought to return some of these flags
 * below. These flags are capabilities and status combined so not all them
 * makes sense to return as a status.
 *
 * WDIOF_OVERHEAT	Reset due to CPU overheat
 * WDIOF_FANFAULT	Fan failed
 * WDIOF_EXTERN1	External relay 1
 * WDIOF_EXTERN2	External relay 2
 * WDIOF_POWERUNDER	Power bad/power fault
 * WDIOF_CARDRESET	Card previously reset the CPU (WD-reset)
 * WDIOF_POWEROVER	Power over voltage
 * WDIOF_SETTIMEOUT	Set timeout (in seconds)
 * WDIOF_MAGICCLOSE	Supports magic close char
 * WDIOF_PRETIMEOUT	Pretimeout (in seconds), get/set
 * WDIOF_ALARMONLY	Watchdog triggers a management or
 *			other external alarm not a reboot
 * WDIOF_KEEPALIVEPING	Keep alive ping reply
 *
 * But it seldomly does and returns 0 most of the time. To figure
 * out if a watchdog reset occured, typically the power/reset manager
 * must be talked to, which knows many more reset causes. We typpically
 * patch linux to do get the watchdog reset status and abused this ioctl
 * to to report other reset reasons as well, and hence are _not_ in the
 * format described above, but kernel specific. The returned values are
 * translated here to a common value understood by VRM.
 */

enum vrm_boot_reason
{
	VRM_BOOT_REASON_UNKNOWN = 0, /* Old kernel, no boottype support etc */
	VRM_BOOT_COLD_BOOT_OR_REBOOT = 1,
	VRM_BOOT_UNREPRODUCABLE_RESET_ON_CCGX = 2,
	VRM_BOOT_RESET_BUTTON = 3,
	VRM_BOOT_COLD_BOOT = 4,
	VRM_BOOT_REBOOT_CMD = 5,
	VRM_BOOT_WATCHDOG_REBOOT = 17
};

#if defined(MACH_beaglebone)
static int get_vrm_boot_reason(int linux_flags)
{
	if (linux_flags & 0x20)
		return VRM_BOOT_RESET_BUTTON;

	if (linux_flags & 0x10)
		return VRM_BOOT_WATCHDOG_REBOOT;

	if (linux_flags & 0x02)
		return VRM_BOOT_REBOOT_CMD;

	if (linux_flags & 0x01)
		return VRM_BOOT_COLD_BOOT;

	return VRM_BOOT_REASON_UNKNOWN;
}
#elif defined(MACH_ccgx)
static int get_vrm_boot_reason(int linux_flags)
{
	if (linux_flags & 0x40)
		return VRM_BOOT_RESET_BUTTON;

	if (linux_flags & 0x30)
		return VRM_BOOT_WATCHDOG_REBOOT;

	if (linux_flags & 0x02)
		return VRM_BOOT_REBOOT_CMD;

	if (linux_flags & 0x01)
		return VRM_BOOT_COLD_BOOT_OR_REBOOT;

	return VRM_BOOT_REASON_UNKNOWN;
}
#else
static int get_vrm_boot_reason(int linux_flags)
{
	return (linux_flags & WDIOF_CARDRESET ? VRM_BOOT_WATCHDOG_REBOOT : VRM_BOOT_REASON_UNKNOWN);
}
#endif


int main(int argc, char **argv)
{
	int fd;
	int bootstatus;
	int vrmstatus;

	fd = open("/dev/watchdog", O_RDONLY);
	if (fd != -1 && ioctl(fd, WDIOC_GETBOOTSTATUS, &bootstatus) == 0) {
		vrmstatus = get_vrm_boot_reason(bootstatus);
		printf("%d\n", vrmstatus);
		exit(EXIT_SUCCESS);
	}

	printf("-1\n");
	exit(EXIT_FAILURE);
}

