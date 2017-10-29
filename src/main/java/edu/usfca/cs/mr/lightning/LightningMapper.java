package edu.usfca.cs.mr.lightning;

import edu.usfca.cs.mr.util.Observation;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class LightningMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Object[] features = Observation.getFeatures(value.toString(),
                new int[]{2, 23},
                new Class<?>[]{String.class, Boolean.class});
        String geohash = (String) features[0];
        boolean lightning = (boolean) features[1];

        if (lightning) {
            context.write(new Text(geohash.substring(0, 4)), new IntWritable(1));
        }
    }
}
