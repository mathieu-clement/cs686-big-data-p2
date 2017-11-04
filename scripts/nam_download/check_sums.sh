#!/usr/bin/env bash

for f in *.grb
do
    md5file="$(echo $f | grep -o1 -E "2015([0-9][0-9])[0-9][0-9]").md5sum"
    expected="`grep $f $md5file | cut -d':' -f2`"
    actual="`gmd5sum $f`"
    if [[ "$expected" != "$actual" ]]; then
        echo $f ": Checksum doesn't match"
    fi
done
