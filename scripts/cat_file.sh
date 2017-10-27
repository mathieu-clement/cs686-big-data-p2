#!/usr/bin/env bash

if [ "$#" -ne 1 ]; then
  echo "Usage: $0 dfs_file" >&2
  exit 1
fi

dfs_file=$1

ssh bass01 DFS_FILE=$dfs_file 'bash -s' <<'ENDSSH'
hdfs dfs -cat $DFS_FILE
ENDSSH
