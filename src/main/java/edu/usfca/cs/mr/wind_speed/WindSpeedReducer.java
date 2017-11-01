package edu.usfca.cs.mr.wind_speed;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WindSpeedReducer extends Reducer<IntWritable,IntWritable,IntWritable,LongWritable> {
    @Override
    protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        long count = 0;
        for (IntWritable value : values) {
            count += value.get();
        }
        context.write(key, new LongWritable(count));
    }
}
