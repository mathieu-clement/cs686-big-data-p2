#!/usr/bin/env bash

set -e

if [ "$#" -ne 2 ]; then
  echo "Usage: $0 job_name main_class" >&2
  exit 1
fi

job=$1
main_class=$2

echo "Transfer JAR to bass01..."
target=$(dirname $0)/../target
scp $target/project2-1.0.jar bass01:.

echo "HDFS: Delete folder for job $job..."
ssh bass01 JOB=$job 'bash -s' <<'ENDSSH'
hdfs dfs -rm -r -f -skipTrash /tmp/mclement2/$JOB
ENDSSH

#echo "HDFS: Create folder for job $job"
#ssh bass01 JOB=$job 'bash -s' <<'ENDSSH'
#hdfs dfs -mkdir /tmp/mclement2/$JOB
#ENDSSH

echo "Launch job..."
ssh bass01 JOB=$job MAIN_CLASS=$main_class 'bash -s' <<'ENDSSH'
yarn jar project2-1.0.jar $MAIN_CLASS /tmp/cs686/nam/nam_mini.tdv /tmp/mclement2/$JOB
ENDSSH
