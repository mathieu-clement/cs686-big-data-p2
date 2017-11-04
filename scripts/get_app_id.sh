#!/usr/bin/env bash

ssh bass01 'bash -s' <<'ENDSSH'
yarn application -list  | awk '$4 == "mclement2" { print $1, $8; }'
ENDSSH
