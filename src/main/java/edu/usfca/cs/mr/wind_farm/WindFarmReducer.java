package edu.usfca.cs.mr.wind_farm;

import org.apache.hadoop.io.ArrayWritable;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WindFarmReducer extends Reducer<Text, ArrayWritable, Text, ArrayWritable> {
    @Override
    protected void reduce(Text geohash, Iterable<ArrayWritable> values, Context context) throws IOException, InterruptedException {
        List<Float> temperatures = new ArrayList<>();
        List<Float> windSpeeds = new ArrayList<>();
        for (ArrayWritable wr : values) {
            Writable[] array = wr.get();
            float temperature = ((FloatWritable) array[0]).get();
            float windSpeed = ((FloatWritable) array[1]).get();

            temperatures.add(temperature);
            windSpeeds.add(windSpeed);
        }

        float temperature90 = percentile(temperatures, 90);
        float windSpeed75 = percentile(windSpeeds, 75);
        if (temperature90 > 0f && windSpeed75 >= 18 && windSpeed75 <= 40) {
            context.write(geohash, new ArrayWritable(FloatWritable.class, new Writable[]{
                    new FloatWritable(temperature90),
                    new FloatWritable(windSpeed75)
            }));
        }
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
        for (i = 0; i < list.size(); i++) {
            float f = list.get(i);
            if (f >= target) break;
        }

        return list.get(i);
    }
}
