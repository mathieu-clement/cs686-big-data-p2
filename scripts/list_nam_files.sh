#!/usr/bin/env bash

ssh bass01 'bash -s' <<'ENDSSH'
hdfs dfs -ls /tmp/cs686/nam | awk '{ print $8; }' | grep -v 'nam_mini' | xargs
ENDSSH
