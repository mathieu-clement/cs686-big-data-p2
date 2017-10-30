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

To answer this question, I  used feature #51 "snow_depth_surface", eliminated all geohashes that have one or more zero values (I consider zero, any measurement under 0.001", i.e. less than 1 mm).

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

### What is the driest month in the bay area? This should include a histogram with data from each month. ([humidity_bay_area](src/main/java/edu/usfca/cs/mr/humidity_bay_area/))

![Humidity in the Bay Area](/images/humidity_bay_area.svg)

| Month | Humidity |
| --- | ---: |
| 1 | 29 |
| 2 | 27 |
| 3 | 22 |
| 4 | 33 |
| 5 | 28 |
| 6 | 24 |
| 7 | 34 |
| 8 | 23 |
| 9 | 30 |
| 10 | 31 |
| 11 | 36 |
| 12 | 45 |


### After graduating from USF, you found a startup that aims to provide personalized travel itineraries using big data analysis. Given your own personal preferences, build a plan for a year of travel across 5 locations. Or, in other words: pick 5 regions. What is the best time of year to visit them based on the dataset? ([travel_year](src/main/java/edu/usfca/cs/mr/travel_year/))

During this year of travel, I would like to visit:
 * dxfy: Halifax, Nova Scotia, in memory of flight SWR-111 that disappeared on September 2, 1998 in the waters of the Atlantic Ocean after an onboard fire,
 * dk2y: Nassau, Bahamas (near Miami, FL) to cheer up,
 * dpxy: Niagara Falls (between Toronto and Rochester, NY) because it's romantic,
 * 9whp: Albuquerque, to see the sites where they filmed Breaking Bad,
 * 9xhv: Rocky Mountain National Park, Colorado, no justification needed.
 
 I will choose the following features to determine when is a good time to visit:
  * Temperature (temperature_surface): 18-27 °C would be ideal ;
  * Snow (snow_cover_surface): < 1 cm because that's easier for hiking and less road closures ;
  * Wind (u-component_of_wind_maximum_wind and v-component_of_wind_maximum_wind): 0-3 on the [Beaufort scale](http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.458.268&rep=rep1&type=pdf), i.e. less than 20 kph ;
  * Humidity (relative_humidity_zerodegc_isotherm): 30-50 % ;
  * Rain (categorical_rain_yes1_no0_surface): 0 ;
  * Freezing rain (categorical_freezing_rain_yes1_no0_surface): 0 ;
  * Visibility (visibility_surface): > 5000.
 
To calculate the [wind speed](http://colaweb.gmu.edu/dev/clim301/lectures/wind/wind-uv.html) from the u and v vectors ("components") of the wind, we apply the formula:

![ws=sqrt(u^2+v^2](https://latex.codecogs.com/gif.latex?\box{ws}&space;=&space;\sqrt{u^2&space;&plus;&space;v^2)
 
By the way, sometimes it is not very obvious what kind of values we get. Is the percentage given as 20 or 0.20? For this I used the following:

    cat nam_mini.tdv | awk '{print $30;}' | sort -n -S 80% | uniq
    
This is for field 30, supposing it contains numerical data.

**Implementation:**

  1. The Map function outputs <geohash: month-day> for every record that matches all the aforementioned criteria.
  2. The Reduce function filters all those records and only outputs the first of 3 consecutive days of those good conditions.

## Deliverable II

Responses go here.
