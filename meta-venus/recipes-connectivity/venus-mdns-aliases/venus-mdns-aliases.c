#include <ctype.h>
#include <ifaddrs.h>
#include <net/if.h>
#include <signal.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include <avahi-client/client.h>
#include <avahi-client/publish.h>
#include <avahi-common/error.h>
#include <avahi-common/malloc.h>
#include <avahi-common/simple-watch.h>

#define CNAME_TTL       60
#define RECONNECT_DELAY  5
#define MAX_ALIASES      1

static AvahiSimplePoll *poll_obj;
static AvahiEntryGroup *group;

static char *aliases[MAX_ALIASES];
static int   n_aliases;

static volatile int running = 1;

/* Encode a DNS name into wire format (RFC 1035 §3.1). */
static int dns_encode(const char *name, uint8_t *buf, size_t cap)
{
	size_t pos = 0, namelen = strlen(name);
	const char *p = name;

	if (namelen && name[namelen - 1] == '.')
		namelen--;

	while (p < name + namelen) {
		const char *dot = memchr(p, '.', namelen - (size_t)(p - name));
		size_t lablen = dot ? (size_t)(dot - p) : namelen - (size_t)(p - name);

		if (pos + 1 + lablen + 1 > cap)
			return -1;
		buf[pos++] = (uint8_t)lablen;
		memcpy(buf + pos, p, lablen);
		pos += lablen;
		p   += lablen + (dot ? 1 : 0);
	}
	buf[pos++] = 0;
	return (int)pos;
}

static void publish(AvahiClient *c)
{
	struct ifaddrs *ifas, *ifa;
	char seen[16][IF_NAMESIZE];
	int n_seen = 0, n_added = 0;
	uint8_t rdata[256];
	const char *fqdn;
	int len, i, j;

	fqdn = avahi_client_get_host_name_fqdn(c);
	if (!fqdn)
		return;

	len = dns_encode(fqdn, rdata, sizeof(rdata));
	if (len < 0)
		return;

	if (!group) {
		group = avahi_entry_group_new(c, NULL, NULL);
		if (!group)
			return;
	}

	avahi_entry_group_reset(group);

	if (getifaddrs(&ifas) == 0) {
		memset(seen, 0, sizeof(seen));
		for (ifa = ifas; ifa; ifa = ifa->ifa_next) {
			int ifindex, already_seen = 0;

			if (!ifa->ifa_addr)
				continue;
			if (ifa->ifa_flags & IFF_LOOPBACK)
				continue;
			if (!(ifa->ifa_flags & IFF_UP))
				continue;
			if (strcmp(ifa->ifa_name, "ap0") == 0)
				continue;

			for (j = 0; j < n_seen; j++) {
				if (strcmp(seen[j], ifa->ifa_name) == 0) {
					already_seen = 1;
					break;
				}
			}
			if (already_seen)
				continue;
			if (n_seen < 16) {
				strncpy(seen[n_seen], ifa->ifa_name, IF_NAMESIZE - 1);
				n_seen++;
			}

			ifindex = (int)if_nametoindex(ifa->ifa_name);
			if (!ifindex)
				continue;

			for (i = 0; i < n_aliases; i++) {
				if (avahi_entry_group_add_record(group,
						ifindex, AVAHI_PROTO_UNSPEC, 0,
						aliases[i], 0x0001, 0x0005, CNAME_TTL,
						rdata, (size_t)len) >= 0)
					n_added++;
			}
		}
		freeifaddrs(ifas);
	}

	if (!n_added) {
		/* Fallback if no suitable interfaces found (e.g. only ap0 is up) */
		for (i = 0; i < n_aliases; i++)
			avahi_entry_group_add_record(group,
				AVAHI_IF_UNSPEC, AVAHI_PROTO_UNSPEC, 0,
				aliases[i], 0x0001, 0x0005, CNAME_TTL,
				rdata, (size_t)len);
	}

	avahi_entry_group_commit(group);
}

static void client_cb(AvahiClient *c, AvahiClientState state, void *ud)
{
	(void)ud;
	switch (state) {
	case AVAHI_CLIENT_S_RUNNING:
		publish(c);
		break;
	case AVAHI_CLIENT_FAILURE:
		avahi_simple_poll_quit(poll_obj);
		break;
	case AVAHI_CLIENT_S_COLLISION:
	case AVAHI_CLIENT_S_REGISTERING:
		if (group)
			avahi_entry_group_reset(group);
		break;
	default:
		break;
	}
}

static void sig_handler(int sig)
{
	(void)sig;
	running = 0;
	if (poll_obj)
		avahi_simple_poll_quit(poll_obj);
}

static void load_aliases(void)
{
	char buf[64];
	char alias[80];
	FILE *f;
	int i, len;

	f = fopen("/data/venus/serial-number", "r");
	if (!f)
		return;

	if (!fgets(buf, sizeof(buf), f))
		goto done;

	buf[strcspn(buf, "\n")] = '\0';
	len = (int)strlen(buf);

	/* Serials are "HQ" followed by alphanumeric characters only.
	 * Enforce this so unexpected file content cannot produce an invalid
	 * mDNS name or inject into the avahi XML service file. */
	if (len != 11)	/* HQ + 9 alphanumeric chars */
		goto done;
	if (tolower((unsigned char)buf[0]) != 'h' ||
	    tolower((unsigned char)buf[1]) != 'q')
		goto done;
	for (i = 2; i < len; i++) {
		if (!isalnum((unsigned char)buf[i]))
			goto done;
	}

	for (i = 0; i < len; i++)
		buf[i] = (char)tolower((unsigned char)buf[i]);
	snprintf(alias, sizeof(alias), "venus-%s.local", buf);
	aliases[n_aliases++] = strdup(alias);

done:
	fclose(f);
}

int main(void)
{
	signal(SIGTERM, sig_handler);
	signal(SIGINT,  sig_handler);

	load_aliases();
	if (!n_aliases)
		return 1;

	while (running) {
		AvahiClient *client;
		int err;

		poll_obj = avahi_simple_poll_new();
		if (!poll_obj)
			goto retry;

		client = avahi_client_new(avahi_simple_poll_get(poll_obj),
		                          0, client_cb, NULL, &err);
		if (!client) {
			avahi_simple_poll_free(poll_obj);
			poll_obj = NULL;
			goto retry;
		}

		avahi_simple_poll_loop(poll_obj);

		group = NULL;   /* freed by avahi_client_free */
		avahi_client_free(client);
		avahi_simple_poll_free(poll_obj);
		poll_obj = NULL;

	retry:
		if (running)
			sleep(RECONNECT_DELAY);
	}

	return 0;
}
