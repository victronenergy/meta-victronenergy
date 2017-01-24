#include <fcntl.h>
#include <stdlib.h>
#include <stdio.h>

#include <linux/watchdog.h>

#ifdef MACH_beaglebone
static const int status_map[] = {
	[0]	= 3,
	[1]	= 4,
	[2]	= 5,
	[16]	= 17,
};
#else
#define status_map ((int *)0)
#endif

int main(int argc, char **argv)
{
	int fd;
	int bootstatus;

	fd = open("/dev/watchdog", O_RDONLY);
	if (fd != -1 && ioctl(fd, WDIOC_GETBOOTSTATUS, &bootstatus) == 0) {
		if (status_map)
			bootstatus = status_map[bootstatus];
		printf("%d\n", bootstatus);
		exit(EXIT_SUCCESS);
	}

	printf("-1\n");
	exit(EXIT_FAILURE);
}

