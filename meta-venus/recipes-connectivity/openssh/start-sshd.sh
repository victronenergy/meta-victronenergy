#!/bin/sh

if [ ! -d /data/keys ]; then
	mkdir /data/keys
	chmod 700 /data/keys
fi

echo "*** Create keys if necessary..."
if [ ! -f /data/keys/ssh_host_rsa_key ]; then
	echo "  generating ssh RSA key..."
	ssh-keygen -q -f /data/keys/ssh_host_rsa_key -N '' -t rsa
fi
if [ ! -f /data/keys/ssh_host_ecdsa_key ]; then
	echo "  generating ssh ECDSA key..."
	ssh-keygen -q -f /data/keys/ssh_host_ecdsa_key -N '' -t ecdsa
fi
if [ ! -f /data/keys/ssh_host_dsa_key ]; then
	echo "  generating ssh DSA key..."
	ssh-keygen -q -f /data/keys/ssh_host_dsa_key -N '' -t dsa
fi

echo "*** Create the PrivSep empty dir if necessary..."
if [ ! -d /var/run/sshd ]; then
	mkdir /var/run/sshd
	chmod 0755 /var/run/sshd
fi

echo "*** Starting sshd..."
exec /usr/sbin/sshd -D -e -f /etc/ssh/sshd_config -h /data/keys/ssh_host_rsa_key -h /data/keys/ssh_host_ecdsa_key -h /data/keys/ssh_host_dsa_key
