package edu.usfca.cs.mr.snow_depth;

import edu.usfca.cs.mr.util.Observation;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Mapper: Reads line by line, emit <geohash, snow_depth> pairs
 */
public class SnowDepthMapper extends Mapper<LongWritable, Text, Text, FloatWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {

        Object[] features = Observation.getFeatures(
                value.toString(),
                new int[]{2, 51},
                new Class[]{String.class, Float.class});
        String geohash = ((String) features[0]).substring(0, 4);
        float snowDepth = (float) features[1]; // in meters

        context.write(new Text(geohash), new FloatWritable(snowDepth));
    }
}
