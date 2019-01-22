#include <errno.h>
#include <fcntl.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/statvfs.h>
#include <sys/vfs.h>
#include <unistd.h>

static void fatal(int ret, char const *str)
{
	fprintf(stderr, "%s", str);
	exit(ret);
}

static void fatal_errno(void)
{
	perror("");
	exit(errno);
}

static void check_readonly(char const *partition)
{
	struct statfs data_stat;

	if (statfs(partition, &data_stat) < 0)
		fatal_errno();

	if (data_stat.f_flags & ST_RDONLY) {
		printf("%s is read-only\n", partition);
		exit(0);
	}
}

static void usage(char const *name)
{
	printf("usage: %s mount\n", name);
	printf("\n");
	printf("Wait indefinitely for given partition to become read-only. The program will\n");
	printf("directly exit if the partition is already read-only.\n");
	printf("\n");
	printf("When the partition is read-only the exit code is zero. Non zero otherwise.\n");
	printf("\n");
	printf("example: %s /data\n", name);
}

int main(int argc, const char * argv[])
{
	int fd;
	fd_set fds;
	int rv;
	const char *partition;

	if (argc != 2) {
		usage(argv[0]);
		exit(0);
	}

	partition = argv[1];

	fd = open("/proc/mounts", O_RDONLY, 0);
	if (fd < 0)
		fatal(ENOENT, "Could not open /proc/mounts");

	check_readonly(partition);

	FD_ZERO(&fds);
	FD_SET(fd, &fds);

	while ((rv = select(fd + 1, NULL, NULL, &fds, NULL)) >= 0) {
		if (FD_ISSET(fd, &fds))
			check_readonly(partition);

		FD_ZERO(&fds);
		FD_SET(fd, &fds);
	}

	return 0;
}
