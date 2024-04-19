#include <crypt.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void usage(char const *name)
{
	printf("usage %s: unique-id password\n", name);
	printf("\n");
	printf("Generate exactly the same password hash as gui-v1.\n");
	printf("\n");
	printf("Since VRM assumes the salt is the 'VRM unique id' it must be used as\n");
	printf("salt when creating the hash or they won't match.\n");
	printf("\n");
	printf("unique-id: The unique id of the device. Must be 12 characters long.\n");
	printf("password: The password to be hashed.\n");
	printf("\n");
}

int main(int argc, char *argv[])
{
	char const *uniqueId;
	char const *passwd;
	char input[16];
	char salt[CRYPT_GENSALT_OUTPUT_SIZE];
	char *hash;

	if (argc < 2) {
		usage(argv[0]);
		exit(EXIT_FAILURE);
	}

	if (strcmp(argv[1], "-h") == 0) {
		usage(argv[0]);
		exit(0);
	}

	uniqueId = argv[1];
	if (strlen(uniqueId) != 12) {
		fprintf(stderr, "The uniqueId must be 12 characters long\n");
		exit(EXIT_FAILURE);
	}

	if (argc != 3) {
		usage(argv[0]);
		exit(EXIT_FAILURE);
	}
	passwd = argv[2];

	/* mind it, input is a buffer, not a string! */
	memcpy(&input[0], uniqueId, 12);
	memcpy(&input[12], uniqueId, 4);

	if (crypt_gensalt_rn("$2a$", 8, input, sizeof(input), salt, sizeof(salt)) == NULL) {
		printf("error!!!");
		exit(EXIT_FAILURE);
	}

	hash = crypt(passwd, salt);
	if (!hash) {
		fprintf(stderr, "crypt failed!\n");
		exit(EXIT_FAILURE);
	}

	if (!printf("%s\n", hash))
		exit(EXIT_FAILURE);

	return 0;
}
