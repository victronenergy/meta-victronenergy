#include <stdio.h>
#include <sys/stat.h>
#include <sys/sysmacros.h>
#include <sys/vfs.h>
#include <ext2fs/ext2fs.h>
#include <linux/magic.h>

int main(int argc, char **argv)
{
	struct statfs stf;
	struct stat st;
	char buf[64];
	ext2_filsys fs;
	errcode_t e2err;
	time_t tm;
	int err;

	if (argc != 2) {
		fprintf(stderr, "usage: %s path\n", argv[0]);
		return 1;
	}

	err = statfs(argv[1], &stf);
	if (err) {
		perror(argv[1]);
		return 1;
	}

	switch (stf.f_type) {
	case EXT4_SUPER_MAGIC:
		break;

	default:
		printf("unknown\n");
		return 0;
	}

	err = stat(argv[1], &st);
	if (err) {
		perror(argv[1]);
		return 1;
	}

	snprintf(buf, sizeof(buf), "/dev/block/%d:%d",
		 major(st.st_dev), minor(st.st_dev));

	e2err = ext2fs_open(buf, 0, 0, 0, unix_io_manager, &fs);
	if (err) {
		fprintf(stderr, "ext2fs_open: %s\n", error_message(e2err));
		return 1;
	}

#ifdef ext2fs_get_tstamp
	tm = ext2fs_get_tstamp(fs->super, s_mtime);
#else
	tm = fs->super->s_mtime;
#endif

	if (tm || fs->super->s_last_mounted[0])
		printf("modified\n");
	else
		printf("clean\n");

	ext2fs_close(fs);

	return 0;
}
