package edu.usfca.cs.mr.snow_depth;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Reducer: Input to the reducer is the output from the mapper. It receives
 * geohash, list<snowDepth> pairs.  Emits <geohash, average> for geohashes
 * that have snow all year (i.e. for all records seen).
 */
public class SnowDepthReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {

    @Override
    protected void reduce(Text key, Iterable<FloatWritable> values, Context context)
            throws IOException, InterruptedException {
        double total = 0.0;
        int count = 0;
        boolean hasNonZeroValue = false;

        for (FloatWritable val : values) {
            count++;
            float f = val.get();
            total += f;
            if (Math.abs(f - 0.0001f) < 0.0001f) { // d < 1 mm considered zero
                hasNonZeroValue = true;
            }
        }

        double average = total / (double) count;
        if (!hasNonZeroValue && average > 0f) {
            context.write(new Text(key), new FloatWritable((float) average));
        }
    }

}
