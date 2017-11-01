#!/usr/bin/env bash

counter=1

while read feature ; do
    echo "public static final" $(echo $feature | tr '-' '_' | awk '{print toupper($0);}') "=" $counter
    counter=$((counter+1))
done
