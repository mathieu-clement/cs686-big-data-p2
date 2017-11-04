package edu.usfca.cs.mr.wind_farm;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

        float temperature90 = percentile(temperatures, 90);
        float windSpeed75 = percentile(windSpeeds, 75);
//        if (temperature90 > 0f && windSpeed75 >= 18 && windSpeed75 <= 40) {
            context.write(geohash, new Text("" + temperature90 + ':' + windSpeed75));
//        }
    }

    private static float percentile(List<Float> list, int percentile) {
        List<Float> cumulated = new ArrayList<>();
        float previous = 0f;
        float total = 0f;

        for (float f : list) {
            cumulated.add(previous + f);
            total += f;
            previous = f;
        }

        float target = total * (percentile / 100f);
        int i;
        for (i = 0; i < cumulated.size(); i++) {
            float f = cumulated.get(i);
            if (f >= target) break;
        }

        return list.get(random.nextInt(list.size()));
    }

    private static Random random = new Random();
}
