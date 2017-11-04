package edu.usfca.cs.mr.wind_farm;

import edu.usfca.cs.mr.util.Observation;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

import static edu.usfca.cs.mr.util.Feature.*;

public class WindFarmMapper extends Mapper<LongWritable, Text, Text, Text> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Observation observation = new Observation(value.toString(),
                GEOHASH,
                TEMPERATURE_SURFACE,
                U_COMPONENT_OF_WIND_MAXIMUM_WIND, V_COMPONENT_OF_WIND_MAXIMUM_WIND);

        String geohash = observation.getGeohash().substring(0, 4);

        float temperature = observation.getFeature(TEMPERATURE_SURFACE, Float.class); // K -> deg C

        double u = observation.getFeature(U_COMPONENT_OF_WIND_MAXIMUM_WIND, Double.class);
        double v = observation.getFeature(U_COMPONENT_OF_WIND_MAXIMUM_WIND, Double.class);
        float windSpeed = (float) Math.sqrt(u * u + v * v);

        context.write(new Text(geohash),
                new Text("" + temperature + ':' + windSpeed));
    }
}
