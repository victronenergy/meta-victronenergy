#include <stdio.h>

#define TAM3517_sequence_number(info) \
	((info)->sequence_number % 0x1000000000000LL)
#define TAM3517_week_of_year(info) (((info)->sequence_number >> 48) % 0x100)
#define TAM3517_year(info) ((info)->sequence_number >> 56)
#define TAM3517_revision_fixed(info) ((info)->revision % 0x100)
#define TAM3517_revision_major(info) (((info)->revision >> 8) % 0x100)
#define TAM3517_revision_tn(info) ((info)->revision >> 16)

struct tam3517_module_info {
	char customer[48];
	char product[48];

	/*
	 * bit 0~47  : sequence number
	 * bit 48~55 : week of year, from 0.
	 * bit 56~63 : year
	 */
	unsigned long long sequence_number;

	/*
	 * bit 0~7   : revision fixed
	 * bit 8~15  : revision major
	 * bit 16~31 : TNxxx
	 */
	unsigned int revision;
	unsigned char eth_addr[4][8];
	unsigned char _rev[100];
};

int main(int argc, char *argv[])
{
	struct tam3517_module_info info;
	FILE *fh;

	/* opening file for reading */
	fh = fopen("/sys/bus/i2c/devices/1-0050/eeprom" , "rb");
	if (!fh) {
		perror("Error opening file");
		return -1;
	}

	if (fgets((char *) &info, sizeof(struct tam3517_module_info), fh) != NULL) {
	        printf("%u%02llu%02llu%012llu\n",
			TAM3517_revision_tn(&info),
			TAM3517_year(&info),
			TAM3517_week_of_year(&info),
			TAM3517_sequence_number(&info));
	}

	fclose(fh);

	return 0;
}
