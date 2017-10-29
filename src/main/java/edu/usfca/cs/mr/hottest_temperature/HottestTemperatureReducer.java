package edu.usfca.cs.mr.hottest_temperature;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class HottestTemperatureReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {
    @Override
    protected void reduce(Text key, Iterable<FloatWritable> values, Context context) throws IOException, InterruptedException {
        float max = Float.MIN_VALUE;
        for (FloatWritable value : values) {
            float f = value.get();
            if (f > max) max = f;
        }
        context.write(key, new FloatWritable(max));
    }
}
