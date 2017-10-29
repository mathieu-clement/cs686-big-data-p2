# Project 2 - Spatiotemporal Analysis with MapReduce

This repository includes starter files and a sample directory structure. You are welcome to use it or come up with your own project structure.

Project Specification: https://www.cs.usfca.edu/~mmalensek/courses/cs686/projects/project-2.html

# Deliverables

The project specification defines several questions that you will answer with MapReduce jobs. You should edit this document (README.md) with your answers as you find them, including figures, references, etc. This will also serve as a way of tracking your progress through the milestones.

## Deliverable I

#### How many records are in the dataset? ([record_count](src/main/java/edu/usfca/cs/mr/record_count/))

The program outputs: `<records, 323759744>`

We can compare this to the number of records that we see in the statistics at the end of the job: 

    Map-Reduce Framework
        Map input records=323759744
        Map output records=323759744
        Combine input records=323759744
        
The mini dataset contains 525584 records for 03/14/2015 and 525584 records for 03/15/2015.
        
#### Are there any Geohashes that have snow depths greater than zero for the entire year? List them all. ([snow_depth](src/main/java/edu/usfca/cs/mr/snow_depth/))

To answer this question, I  used feature #51 "snow_depth_surface", eliminated all geohashes that have one or more zero values (I consider zero, any measurement under 0.001", i.e. less than 1 mm). The geohash has been reduced to a 3 character prefix (78 km [accuracy](https://gis.stackexchange.com/a/115501)).

|Geohash|Address|Average snow depth [m]|
| --- | --- | ---: |
| abcde | Montreal, Quebec | 12.4 |

#### When and where was the hottest temperature observed in the dataset? Is it an anomaly? ([hottest_temperature](src/main/java/edu/usfca/cs/mr/hottest_temperature/))

Here is the top 50 (geohash kept to original length / precision):

| Geohash | Address | Temperature [°C] |
| --- | --- | ---: |
| abcde | TODO | 123.45 |

For this exercise I had some trouble. I tried to use my own WritableComparable, but wasn't able to run the job due to a lack of memory apparently. In the process I wrote a little utility to reverse geocode a geohash to an address using the Python Geohash library and querying the Nominatim web service (used by OpenStreetMap).

These temperatures are plausible. Similar temperatures can be seen from adjacent geohashes, so they don't seem to be anomalies. 

## Deliverable II

Responses go here.
