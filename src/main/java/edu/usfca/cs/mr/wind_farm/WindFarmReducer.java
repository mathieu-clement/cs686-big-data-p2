package edu.usfca.cs.mr.wind_farm;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static edu.usfca.cs.mr.util.Observation.toCelsius;
import static edu.usfca.cs.mr.util.Statistics.percentile;

public class WindFarmReducer extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text geohash, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        List<Float> temperatures = new ArrayList<>();
        List<Float> windSpeeds = new ArrayList<>();
        List<Float> snowDepths = new ArrayList<>();
        for (Text t : values) {
            String[] parts = t.toString().split(":");
            temperatures.add(Float.parseFloat(parts[0]));
            windSpeeds.add(Float.parseFloat(parts[1]));
            snowDepths.add(Float.parseFloat(parts[2]));
        }

        Collections.sort(temperatures);
        Collections.sort(windSpeeds);
        Collections.sort(snowDepths);
        float temperatureAbove = percentile(temperatures, 10f); // 90 % of data is above
        temperatureAbove = toCelsius(temperatureAbove);
        float windSpeedAbove = percentile(windSpeeds, 25f);
        float windSpeedBelow = percentile(windSpeeds, 75f);
        float snowDepthBelow = percentile(snowDepths, 90f);
        if (temperatureAbove > 0f && windSpeedAbove > 17f && windSpeedBelow < 40f && snowDepthBelow < 0.01f) {
            context.write(geohash, new Text("" + temperatureAbove + ' ' + windSpeedAbove + ' ' + windSpeedBelow));
        }
    }
}
