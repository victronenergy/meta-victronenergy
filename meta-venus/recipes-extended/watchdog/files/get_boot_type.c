#include <fcntl.h>
#include <stdlib.h>
#include <stdio.h>

#include <linux/watchdog.h>

int main(int argc, char **argv)
{
	int fd;
	int bootstatus;

	fd = open("/dev/watchdog", O_RDONLY);
	if (fd != -1 && ioctl(fd, WDIOC_GETBOOTSTATUS, &bootstatus) == 0) {
		printf("%d\n", bootstatus);
		exit(EXIT_SUCCESS);
	}

	printf("-1\n");
	exit(EXIT_FAILURE);
}

