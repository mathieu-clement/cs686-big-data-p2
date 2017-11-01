package edu.usfca.cs.mr.lightning;

import edu.usfca.cs.mr.util.Observation;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

import static edu.usfca.cs.mr.util.Feature.GEOHASH;
import static edu.usfca.cs.mr.util.Feature.LIGHTNING_SURFACE;

public class LightningMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Observation observation = new Observation(value.toString(), GEOHASH, LIGHTNING_SURFACE);
        String geohash = observation.getGeohash()
        boolean lightning = observation.getFeature(LIGHTNING_SURFACE, Boolean.class);

        if (lightning) {
            context.write(new Text(geohash.substring(0, 4)), new IntWritable(1));
        }
    }
}
