package edu.usfca.cs.mr.wind_speed;

import edu.usfca.cs.mr.util.Observation;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

import static edu.usfca.cs.mr.util.Feature.*;

public class WindSpeedMapper extends Mapper<LongWritable, Text, IntWritable, IntWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Observation observation = new Observation(value.toString(), U_COMPONENT_OF_WIND_MAXIMUM_WIND, V_COMPONENT_OF_WIND_MAXIMUM_WIND);
        double u = observation.getFeature(U_COMPONENT_OF_WIND_MAXIMUM_WIND, Double.class);
        double v = observation.getFeature(V_COMPONENT_OF_WIND_MAXIMUM_WIND, Double.class);

        double speed = Math.sqrt(u * u + v * v);
    }
}
