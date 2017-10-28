package edu.usfca.cs.mr.hottest_temperature;

import edu.usfca.cs.mr.util.Observation;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class HottestTemperatureMapper extends Mapper<LongWritable, Text, GeohashTimestampWritable, FloatWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Object[] features = Observation.getFeatures(
                value.toString(),
                new int[]{1, 2, 41},
                new Class[]{String.class, String.class, Float.class});
        String timestamp = (String) features[0];
        String geohash = (String) features[1];
        float temperature = (float) features[2];

        context.write(
                new GeohashTimestampWritable(geohash, timestamp),
                new FloatWritable(temperature));
    }
}
