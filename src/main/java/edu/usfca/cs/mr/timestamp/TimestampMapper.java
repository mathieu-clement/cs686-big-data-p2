package edu.usfca.cs.mr.timestamp;

import edu.usfca.cs.mr.util.Observation;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static edu.usfca.cs.mr.util.Feature.TIMESTAMP;

/**
 * Mapper: Reads line by line, emit <"mm-dd", 1> pairs based on timestamp.
 */
public class TimestampMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd");

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        Observation observation = new Observation(value.toString(), TIMESTAMP);
        String timestamp = observation.getFeature(TIMESTAMP, String.class);
        Date date = new Date(Long.parseLong(timestamp));
        String dateStr = DATE_FORMAT.format(date);

        context.write(new Text(dateStr), new IntWritable(1));
    }
}
