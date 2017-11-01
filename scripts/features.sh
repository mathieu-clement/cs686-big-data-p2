#!/usr/bin/env bash

while read feature ; do
    echo $(echo $feature | tr '-' '_' | awk '{print toupper($0);}')", "
done
