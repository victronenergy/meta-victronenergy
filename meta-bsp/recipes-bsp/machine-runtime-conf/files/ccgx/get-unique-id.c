#include <stdio.h>
#include <fcntl.h>
#include <sys/mman.h>

#define MAP_SIZE 4096UL
#define MAP_MASK (MAP_SIZE - 1)

/*
 * The mac-address of the ccgx is used to identify the device.
 * It is available only after the network interface is brough up,
 * so read it directly, so there is no dependency on network state.
 */
int main(int argc, char **argv)
{
	int fd;
	void *map_base;
	unsigned *virt_addr;
	off_t target = 0x48002380;

	fd = open("/dev/mem", O_RDONLY);
	if (fd == -1) {
		printf("failed to open /dev/mem\n");
		return 1;
	}

	map_base = mmap(0, MAP_SIZE, PROT_READ, MAP_SHARED, fd, target & ~MAP_MASK);
	if (map_base == (void *) -1) {
		printf("failed to map\n");
		return 1;
	}

	virt_addr = map_base + (target & MAP_MASK);
	printf("%06x%06x\n", virt_addr[1], virt_addr[0]);

	return 0;
}
