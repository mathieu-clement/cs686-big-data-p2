# Project 2 - Spatiotemporal Analysis with MapReduce

Project Specification: https://www.cs.usfca.edu/~mmalensek/courses/cs686/projects/project-2.html

# Deliverables

## Deliverable I

### How many records are in the dataset? ([record_count](src/main/java/edu/usfca/cs/mr/record_count/))

The program outputs: `<records, 323,759,744>`

We can compare this to the number of records that we see in the statistics at the end of the job: 

    Map-Reduce Framework
        Map input records=323,759,744
        Map output records=323,759,744
        Combine input records=323,759,744
        
(commas added for readability)

To put this in context, the population of the United States happens to be very much the same: 323.1 million.


The mini dataset contains 525,584 records for 03/14/2015 and 525,584 records for 03/15/2015.
        
### Are there any Geohashes that have snow depths greater than zero for the entire year? List them all. ([snow_depth](src/main/java/edu/usfca/cs/mr/snow_depth/))

To answer this question, I  used feature #51 "snow_depth_surface", eliminated all geohashes that have one or more zero values.

Here's the top 3:

|Geohash|Address|Average snow depth [m]|
| --- | --- | ---: |
| f2w7rv7q5hup | near Lac Fontaine, QC, Canada | 7.5816345 |
| c41v48pupf00 | near Devil's Thumb, Alaska / Canada | 2.0594404 |
| c41vtks3952p | a few miles from #2 | 1.7886817 |


### When and where was the hottest temperature observed in the dataset? Is it an anomaly? ([hottest_temperature](src/main/java/edu/usfca/cs/mr/hottest_temperature/))

Here is the top 10 (geohash kept to original length / precision):

| Geohash | Address | Month-Day | Temperature [°C] |
| --- | --- | --- | ---: |
| **d5dpds10m55b** | **Quintana Roo, México** | **08-22** | **58.24063** |
| d5dpds10m55b | Quintana Roo, México | 08-23 | 58.024323 |
| d5f0jqerq27b | Cancún, Quintana Roo, México | 08-22 | 57.99063 |
| d5f0jqerq27b | Cancún, Quintana Roo, México | 08-27 | 57.94571 |
| d5f04xyhucez | Quintana Roo, México | 08-22 | 57.86563 |
| d5f04xyhucez | Quintana Roo, México | 08-08 | 57.761627 |
| 9g7322m79vh0 | Oaxaca, México | 04-27 | 57.7045 |
| d5dpds10m55b | Quintana Roo, México | 08-08 | 57.636627 |
| 9g7eb0mjs2zb | Veracruz de Ignacio de la Llave, México | 04-27 | 57.5795 |
| 9g77v81phcu0 | Veracruz de Ignacio de la Llave, México | 04-27 | 57.5795 |

**Yes, it is an anomaly.**

These temperatures seem plausible at first. Similar temperatures can be seen from adjacent geohashes, so they don't seem to be anomalies. FYI Quintana Roo is the Mexican state in which Cancún is situated, Oaxaca is a Mexican city situated very far away from there, and then there is the very pedantic Veracruz de Ignacio de la Llave, or you know... Veracruz.
We can also see that the same date comes more multiple times. 

However, Weather Underground tells another story for the temperature on August 22, 2015 in Cancun, reporting only a maximum of 33 °C. Furthermore, at the time of writing, the hottest temperature (measured) on Earth has been recorded in Death Valley in 1913 at 56.7 °C (134 °F), so we can trivially conclude that any temperature in the NAM files higher than the record is erroneous. 

**Answering this question was not a sinecure...** I tried to use my own WritableComparable, but wasn't able to run the job due to a lack of memory apparently. Then the reduce part hanged for a long time. And finally there was the issue of sorting the data. For that I downloaded the output files to the disk, and ran:

    LC_ALL=C sort -k2 -n -S 80% part* | grep -v 'E-' 
    
`LC_ALL=C` given for reference, will speed up alphanumeric sorts dramatically, avoids UTF-8 type comparisons. `-k2` sorts using the second column (here, the temperature). `-S 80%` permits sort to use 80 % of the system memory. The output is piped to grep to exclude (`-v`) results such as 5.04235E-4 because numerical sort doesn't work with scientific notation. Definitely look at the `--parallel` option on newer version of GNU sort (the bass machines have antique software so that wasn't an option).

In the process I wrote a little utility to reverse geocode a geohash to an address using the Python Geohash library and querying the Nominatim web service (used by OpenStreetMap).

### Where are you most likely to be struck by lightning? Use a precision of 4 Geohash characters and provide the top 3 locations. ([lightning](src/main/java/edu/usfca/cs/mr/lightning/))

To solve this question, I looked at the feature "lightning_surface". Whenever the value is positive, the mapper output is <geohash[4:], 1>. The reducer then calculates the sum just like word count, and that tells us how many observations of lightning we have found for each 4 character geohash.

We have a tie, so this list has more than 3 locations.

![Lightning map](/images/lightning_map.png) 

https://drive.google.com/open?id=1R22MWqeK0pteabunmvPfcmzw_tU&usp=sharing

|Geohash|Address (center)|Lightning occurrences|
| --- | --- | ---: |
| dkcm | Atlantic Ocean, Bahamas | 33 |
| d54s | Atlantic Ocean, 200 km east of Belize | 33 |
| d54q | Atlantic Ocean, 150 km east of Belize | 33 |
| 9g3m | Mexico City | 33 |
| 9en4  | Pacific Ocean, 100 km from the coast of Colola, Michoacán, Mexico | 33 |
| 9ekt | Pacific Ocean, close to previous point | 33 |
| 9ehw | Pacific Ocean, close to previous point | 33 |
| 9eeu | Pacific Ocean, west of Guadalajara, Mexico | 33 |
| 9685 | Pacific Ocean, very far from the coast | 33 |
| d5k2 | Atlantic Ocean, west of Cayman Islands | 23 |
| dt3n | BERMUDA TRIANGLE!!! :ghost: | 22 |
| dprg | Allegheny National Forest, close to Pennsylvania Route 666 :japanese_goblin: | 22 |

Keep in mind: 4 character geohash defines a zone with a [precision](https://gis.stackexchange.com/a/115501) of +/- 20 km.

### What is the driest month in the bay area? This should include a histogram with data from each month. ([humidity_bay_area](src/main/java/edu/usfca/cs/mr/humidity_bay_area/))

On average, during the month of March 2015, the humidity was 22 %, making it the driest month of the year.

![Humidity in the Bay Area](/images/humidity_bay_area.svg)


### After graduating from USF, you found a startup that aims to provide personalized travel itineraries using big data analysis. Given your own personal preferences, build a plan for a year of travel across 5 locations. Or, in other words: pick 5 regions. What is the best time of year to visit them based on the dataset? ([travel_year](src/main/java/edu/usfca/cs/mr/travel_year/))

#### Locations

During this year of travel, I would like to visit:
 * dxfy: Halifax, Nova Scotia, in memory of flight SWR-111 that disappeared on September 2, 1998 in the waters of the Atlantic Ocean after an onboard fire,
 * dk2y: Nassau, Bahamas (near Miami, FL) to cheer up and enjoy some warmth while the continental US shivers,
 * dpxy: Niagara Falls (between Toronto and Rochester, NY) because it's romantic,
 * 9whp: Albuquerque, to see the sites where they filmed Breaking Bad,
 * 9xhv: Rocky Mountain National Park, Colorado, no justification needed.
 
 #### Features
 
 I will choose the following features to determine when is a good time to visit:
  * Temperature (temperature_surface): 18-27 °C would be ideal ;
  * Snow (snow_cover_surface): < 3 cm because that's easier for hiking and less road closures ;
  * Wind (u-component_of_wind_maximum_wind and v-component_of_wind_maximum_wind): 0-3 on the [Beaufort scale](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.458.268&rep=rep1&type=pdf), i.e. less than 20 kph -> finally decided to relax this criterion ;
  * Humidity (relative_humidity_zerodegc_isotherm): 30-50 % ;
  * Rain (categorical_rain_yes1_no0_surface): 0 ;
  * Freezing rain (categorical_freezing_rain_yes1_no0_surface): 0 ;
  * Visibility (visibility_surface): > 3000.

#### Wind speed

To calculate the [wind speed](http://colaweb.gmu.edu/dev/clim301/lectures/wind/wind-uv.html) from the u and v vectors ("components") of the wind, we apply the formula:

![ws=sqrt(u^2+v^2](https://latex.codecogs.com/gif.latex?\box{ws}&space;=&space;\sqrt{u^2&space;&plus;&space;v^2)

With this knowledge I plotted the distribution of the wind speed (considering "max wind"):

![wind speed distribution](/images/wind_speed_distribution.png)

and computed these values (unit is m/s):

  - Mean: 30
  - Standard deviation: 34
  - Median: 28
  - Quartile Q1: 18, Quartile Q3: 40, so 50 % of distribution between 18 and 40

#### Extracting a column from tdv files
 
By the way, sometimes it is not very obvious what kind of values we get. Is the percentage given as 20 or 0.20? For this I used the following:

    cat nam_mini.tdv | awk '{print $30;}' | sort -n -S 80% | uniq
    
This is for field 30, supposing it contains numerical data.

**Implementation:**

  1. The Map function outputs <geohash: dayInYear> for every record that matches all the aforementioned criteria.
  2. The Reduce function is basically an identity function.
  
| Destination | Dates | Comments |
| --- | --- | --- |
| Albuquerque | Last week of March, mid-May | Get there before it gets too hot.
| Nova Scotia | June - July - August | Only the summer months are bearable.
| Niagara Falls | Last week of August | Bring your umbrella.
| Rocky Mountain NP | Early September | "The Rockies have mild summers, cold winters and a lot of precipitation."
| Bahamas | Early November, mid-December, early January | Send selfies to your less fortunate friends.

[View the actual results](https://pastebin.com/ZE7JZskD)

![Travel year](/images/travel_year.png)
  
### Your travel startup is so successful that you move on to green energy; here, you want to help power companies plan out the locations of solar and wind farms across North America. Write a MapReduce job that locates the top 3 places for solar and wind farms, as well as a combination of both (solar + wind farm). You will report a total of 9 Geohashes as well as their relevant attributes (for example, cloud cover and wind speeds).

Criteria for choosing the location of wind farms:
  - wind speed (duh!) : see u and v component of wind and formula for wind speed above. A wind turbine have a cut-in power of 3.5 m/s, and produce their maximum energy at 10-15 m/s. At 25 m/s the turbine must be cut-off or it will break. We will therefore, in the reduce phase, consider locations where wind speed is >= 10 m/s and < 25 m/s about 75 % of the time (so we'll compute quartiles). [Source](http://www.level.org.nz/energy/renewable-electricity-generation/wind-turbine-systems/)
  - not in a region prone to icing (while ice-throwing turbines are supposedly not a danger according to the Internet, they will shut down when it's freezing, and thus not generating any electricity). For this, we want the temperature to be above 0 °C (freezing point) 90 % of the time.
  
Would be nice to have:
  - away from surrounding (tall) obstacles: we don't really have this data here, so we'll ignore it
  - not too far from civilization: would be nice, but no data, so ignored
  - not over water: while offshore installations certainly exist, we'll simplify the problem and assume this is unrealistic no matter how close to the coast. We'll filter manually due to programmatic difficulty. [onwater.io](https://onwater.io/) is promising but very unstable.

Criteria for choosing the location of solar farms:
  - mostly the same as for wind farms
  - ~~wind speed~~
  - cloud cover
  - closer to the equator is better, simply because the sun is up longer, and that's also where you need the most air conditioning, which works on electricity
  - no snow! (you can heat the panels... but why waste energy on that if you can avoid it)
  - south oriented is better, but... we don't have this data.

## Deliverable II

Responses go here.
