package edu.usfca.cs.mr.wind_farm;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WindFarmReducer extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text geohash, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        List<Float> temperatures = new ArrayList<>();
        List<Float> windSpeeds = new ArrayList<>();
        for (Text t : values) {
            String[] parts = t.toString().split(":");
            float temperature = Float.parseFloat(parts[0]);
            float windSpeed = Float.parseFloat(parts[1]);

            temperatures.add(temperature);
            windSpeeds.add(windSpeed);
        }

        Collections.sort(temperatures);
        Collections.sort(windSpeeds);
        float temperature10 = percentile(temperatures, 10); // 90 % of data is above
        float windSpeed25 = percentile(windSpeeds, 25); // 75 % of the data is above
        float windSpeed75 = percentile(windSpeeds, 75); // 75 % of the data is below
        if (toCelsius(temperature10) > 0f && windSpeed25 > 17 && windSpeed75 < 40) {
            context.write(geohash, new Text("" + temperature10 + ':' + windSpeed25 + ':' + windSpeed75));
        }
    }

    // precondition: list is sorted
    static float percentile(List<Float> list, int percentile) {
        int i = list.size() * percentile / 100;

        if (i < 0) i = 0;
        else if (i >= list.size()) i = list.size() - 1;

        return list.get(i);
    }

    private static float toCelsius(float kelvin) {
        return kelvin - 273.15f;
    }
}
