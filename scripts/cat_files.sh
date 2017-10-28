#!/usr/bin/env bash

for i in $@
do
    $(dirname $0)/cat_file.sh $i 2>/dev/null
done
