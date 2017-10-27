#!/usr/bin/env bash

if [ "$#" -ne 1 ]; then
  echo "Usage: $0 application_id" >&2
  exit 1
fi

app=$1

ssh bass01 APP=$app 'bash -s' <<'ENDSSH'
yarn logs -applicationId $APP
ENDSSH
