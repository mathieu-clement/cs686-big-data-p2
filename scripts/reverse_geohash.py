#!/usr/bin/env python

import sys
sys.path.insert(0, "~/.local/lib/python2.6/site-packages/")

import codecs
sys.stdout = codecs.getwriter('utf8')(sys.stdout)
sys.stderr = codecs.getwriter('utf8')(sys.stderr)

import Geohash
import requests

lat, lng = Geohash.decode(sys.argv[1])
lat = float(lat)
lng = float(lng)
#print '%f, %f' % (lat, lng)
url = 'http://nominatim.openstreetmap.org/reverse?format=json&addressdetails=1&lat=%f&lon=%f' % (lat, lng)
r = requests.get(url)
json = r.json()
addr = json['address']

city = None
if 'city' in addr:
    city = addr['city']
state = addr['state']
country = addr['country']# // country_code ... .upper()

if not city:
    print '%s, %s' % (state, country)
else:
    print '%s, %s, %s' % (city, state, country)
