package edu.usfca.cs.mr.hottest_temperature;

import edu.usfca.cs.mr.util.Feature;
import edu.usfca.cs.mr.util.Observation;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static edu.usfca.cs.mr.util.Feature.*;

public class HottestTemperatureMapper extends Mapper<LongWritable, Text, Text, FloatWritable> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd");

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Object[] features = Observation.getFeatures(
                value.toString(),
                new Feature[]{TIMESTAMP, GEOHASH, TEMPERATURE_SURFACE},
                new Class<?>[]{String.class, String.class, Float.class});
        String timestamp = (String) features[0];
        Date date = new Date(Long.parseLong(timestamp));
        String dateStr = DATE_FORMAT.format(date);

        String geohash = (String) features[1];

        float temperature = (float) features[2]; // Kelvin
        temperature = temperature - 273.15f; // degrees Celsius

        context.write(
                new Text(geohash + ":" + dateStr),
                new FloatWritable(temperature));
    }
}
