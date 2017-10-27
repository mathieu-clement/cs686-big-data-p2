#!/usr/bin/env bash

if [ "$#" -ne 1 ]; then
  echo "Usage: $0 job_name" >&2
  exit 1
fi

job=$1

ssh bass01 JOB=$job 'bash -s' <<'ENDSSH'
hdfs dfs -ls /tmp/mclement2/$JOB  | awk '$5 != 0 { print $8; }' | sed '/^$/d'
ENDSSH
