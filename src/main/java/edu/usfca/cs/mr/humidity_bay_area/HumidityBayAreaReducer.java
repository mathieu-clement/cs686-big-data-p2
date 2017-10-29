package edu.usfca.cs.mr.humidity_bay_area;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class HumidityBayAreaReducer extends Reducer<IntWritable, FloatWritable, IntWritable, FloatWritable> {
    @Override
    protected void reduce(IntWritable month, Iterable<FloatWritable> values, Context context) throws IOException, InterruptedException {
        long count = 0;
        double total = 0.0;

        for (FloatWritable v : values) {
            float humidity = v.get();
            total += humidity;
            count++;
        }

        float average = (float) (total / (double) count);
        context.write(month, new FloatWritable(average));
    }
}
