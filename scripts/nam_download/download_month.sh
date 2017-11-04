#!/usr/bin/env bash

year=2015
month=$1 # e.g. 01, 02, 03

for day in `seq -w 1 31`
do
    $(dirname $0)/download_day.sh $year $month $day
done
