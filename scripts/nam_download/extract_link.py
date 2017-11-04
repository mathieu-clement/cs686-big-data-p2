#!/usr/bin/env python

import sys
import re

pattern = re.compile('.*>(namanl.+?_0000_.+?\.grb)</a>.*')

for line in sys.stdin:
    m = pattern.match(line)
    if m:
        print(m.group(1))
