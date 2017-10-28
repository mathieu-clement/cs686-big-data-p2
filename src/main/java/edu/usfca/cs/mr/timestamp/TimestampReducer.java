package edu.usfca.cs.mr.timestamp;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Reducer: Input to the reducer is the output from the mapper. It receives
 * <"mm-dd", list<count> pairs.  Sums up all the records for a specific date.
 * Emits <"mm-dd", total count>.
 */
public class TimestampReducer extends Reducer<Text, IntWritable, Text, LongWritable> {

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context)
    throws IOException, InterruptedException {
        long count = 0;
        for(IntWritable val : values){
            count += val.get();
        }
        context.write(key, new LongWritable(count));
    }

}
