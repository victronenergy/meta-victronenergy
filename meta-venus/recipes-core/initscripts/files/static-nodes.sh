#! /bin/sh

test "$1" = start || exit 1

kmod static-nodes -f tmpfiles |
while read type name mode uid gid age dev; do
    test -e $name && continue

    type=${type%!}

    case $type in
        d)
            mkdir -p -m $mode $name
            ;;
        c|b)
            mknod -m $mode $name $type ${dev%:*} ${dev#*:}
            ;;
    esac
done
