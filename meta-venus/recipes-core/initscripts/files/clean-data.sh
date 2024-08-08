#! /bin/sh

MAX_SIZE=1048576

rm_if_large() {
    file=$1

    size=$(stat -c %s "$file")

    if [ $size -gt $MAX_SIZE ]; then
        truncate -s 0 "$file"
    fi
}

find /data -maxdepth 2 -type f -name current.log | while read file; do
    rm_if_large "$file"
done
