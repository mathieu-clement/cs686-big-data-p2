package edu.usfca.cs.mr.climate_chart;

import edu.usfca.cs.mr.util.Observation;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import static edu.usfca.cs.mr.util.Feature.*;

public class ClimateChartMapper extends Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Observation obs = new Observation(value.toString(),
                TIMESTAMP, GEOHASH, TEMPERATURE_SURFACE, PRECIPITABLE_WATER_ENTIRE_ATMOSPHERE);

        String geohashPrefix = context.getConfiguration().get("geohash_prefix");
        String geohash = obs.getGeohash();
        if (!geohash.startsWith(geohashPrefix)) return;

        Date date = new Date(obs.getFeature(TIMESTAMP, Long.class));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;

        float temperature = obs.getFeature(TEMPERATURE_SURFACE, Float.class);
        float precipitations = obs.getFeature(PRECIPITABLE_WATER_ENTIRE_ATMOSPHERE, Float.class);

        context.write(new Text(geohashPrefix + ':' + month), new Text(
                "" + temperature + ':' + precipitations
        ));
    }
}
