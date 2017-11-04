#!/usr/bin/env bash

year=$1
month=$2
day=$3

base="https://nomads.ncdc.noaa.gov/data/namanl/${year}${month}/${year}${month}${day}"
curl $base/${year}${month}${day}.md5sum -o ${year}${month}${day}.md5sum

for grib in $(curl $base/ -o- | ./extract_link.py | xargs)
do
    echo $grib
    wget $base/$grib
done
