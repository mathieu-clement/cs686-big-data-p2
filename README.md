# Project 2 - Spatiotemporal Analysis with MapReduce

This repository includes starter files and a sample directory structure. You are welcome to use it or come up with your own project structure.

Project Specification: https://www.cs.usfca.edu/~mmalensek/courses/cs686/projects/project-2.html

# Deliverables

The project specification defines several questions that you will answer with MapReduce jobs. You should edit this document (README.md) with your answers as you find them, including figures, references, etc. This will also serve as a way of tracking your progress through the milestones.

## Deliverable I

#### How many records are in the dataset? ([record_count](src/main/java/edu/usfca/cs/mr/record_count/))

The program outputs: `<records, 323,759,744>`

We can compare this to the number of records that we see in the statistics at the end of the job: 

    Map-Reduce Framework
        Map input records=323,759,744
        Map output records=323,759,744
        Combine input records=323,759,744
        
(commas added for readability)

To put this in context, the population of the United States happens to be very much the same: 323.1 million.


The mini dataset contains 525,584 records for 03/14/2015 and 525,584 records for 03/15/2015.
        
#### Are there any Geohashes that have snow depths greater than zero for the entire year? List them all. ([snow_depth](src/main/java/edu/usfca/cs/mr/snow_depth/))

To answer this question, I  used feature #51 "snow_depth_surface", eliminated all geohashes that have one or more zero values (I consider zero, any measurement under 0.001", i.e. less than 1 mm).

|Geohash|Address|Average snow depth [m]|
| --- | --- | ---: |
| abcde | Montreal, Quebec | 12.4 |

#### When and where was the hottest temperature observed in the dataset? Is it an anomaly? ([hottest_temperature](src/main/java/edu/usfca/cs/mr/hottest_temperature/))

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


These temperatures seem plausible. Similar temperatures can be seen from adjacent geohashes, so they don't seem to be anomalies. FYI Quintana Roo is the Mexican state in which Cancún is situated, Oaxaca is a Mexican city situated very far away from there, and then there is the very pedantic Veracruz de Ignacio de la Llave, or you know... Veracruz.
We can also see that the same date comes more multiple times. 

For this exercise I had some trouble. I tried to use my own WritableComparable, but wasn't able to run the job due to a lack of memory apparently. Then the reduce part hanged for a long time. And finally there was the issue of sorting the data. For that I downloaded the output files to the disk, and ran:

    LC_ALL=C sort -k2 -n -S 80% part* | grep -v 'E-' 
    
`LC_ALL=C` given for reference, will speed up alphanumeric sorts dramatically, avoids UTF-8 type comparisons. `-k2` sorts using the second column (here, the temperature). `-S 80%` permits sort to use 80 % of the system memory. The output is piped to grep to exclude (`-v`) results such as 5.04235E-4 because numerical sort doesn't work with scientific notation. Definitely look at the `--parallel` option on newer version of GNU sort (the bass machines have antique software so that wasn't an option).

In the process I wrote a little utility to reverse geocode a geohash to an address using the Python Geohash library and querying the Nominatim web service (used by OpenStreetMap).

#### Where are you most likely to be struck by lightning? Use a precision of 4 Geohash characters and provide the top 3 locations.

## Deliverable II

Responses go here.
