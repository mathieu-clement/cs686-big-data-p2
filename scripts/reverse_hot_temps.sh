#!/usr/bin/env bash

while read -r key value; do
    geohash=$(echo $key | cut -d':' -f1)
    address="`$(dirname $0)/reverse_geohash.py $geohash`"
    echo -e $value "\t" $address
done



