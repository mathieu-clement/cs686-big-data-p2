package edu.usfca.cs.mr.snow_depth;

import edu.usfca.cs.mr.util.Observation;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

import static edu.usfca.cs.mr.util.Feature.GEOHASH;
import static edu.usfca.cs.mr.util.Feature.SNOW_DEPTH_SURFACE;

/**
 * Mapper: Reads line by line, emit <geohash, snow_depth> pairs
 */
public class SnowDepthMapper extends Mapper<LongWritable, Text, Text, FloatWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        Observation observation = new Observation(value.toString(), GEOHASH, SNOW_DEPTH_SURFACE);

        String geohash = observation.getGeohash();
        float snowDepth = observation.getFeature(SNOW_DEPTH_SURFACE, Float.class); // in meters

        context.write(new Text(geohash), new FloatWritable(snowDepth));
    }
}
