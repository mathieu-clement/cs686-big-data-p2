package edu.usfca.cs.mr.solar_farm;

import edu.usfca.cs.mr.util.Geohash;
import edu.usfca.cs.mr.util.Observation;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

import static edu.usfca.cs.mr.util.Feature.*;

public class SolarFarmMapper extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Observation observation = new Observation(value.toString(),
                GEOHASH,
                TEMPERATURE_SURFACE,
                TOTAL_CLOUD_COVER_ENTIRE_ATMOSPHERE,
                SNOW_DEPTH_SURFACE);

        String geohash = observation.getGeohash().substring(0, 3);
        // degrees to the equator, the lower the better = bias towards the southern states
        int latMin = (int) Geohash.decodeHash(geohash).getLowerBoundForLatitude();

        float temperature = observation.getFeature(TEMPERATURE_SURFACE, Float.class);
        float cloudCover = observation.getFeature(TOTAL_CLOUD_COVER_ENTIRE_ATMOSPHERE, Float.class);
        float snowDepth = observation.getFeature(SNOW_DEPTH_SURFACE, Float.class);

        context.write(new Text(geohash),
                new Text("" + temperature + ':' + cloudCover + ':' + snowDepth));
    }
}
