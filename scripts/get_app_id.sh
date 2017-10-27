#!/usr/bin/env bash

ssh bass01 'bash -s' <<'ENDSSH'
yarn application -list  | awk '$6 == "mclement2" { print $1; }'
ENDSSH
