package edu.usfca.cs.mr.climate_chart;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClimateChartReducer extends Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        List<Float> temperatures = new ArrayList<>();
        List<Float> precipitations = new ArrayList<>();

        /*
        String[] keyParts = key.toString().split(":");
        String geohash = keyParts[0];
        String month = keyParts[1];
        */

        for (Text value : values) {
            String[] parts = value.toString().split(":");
            temperatures.add(Float.parseFloat(parts[0]));
            precipitations.add(Float.parseFloat(parts[1]));
        }

        float totalTemp = 0f;
        int count = 0;
        float minTemp = Float.MAX_VALUE;
        float maxTemp = Float.MIN_VALUE;
        for (Float temperature : temperatures) {
            if (temperature < minTemp) minTemp = temperature;
            if (temperature > maxTemp) maxTemp = temperature;
            totalTemp += temperature;
            count++;
        }
        float avgTemp = totalTemp / count;

        float totalPrecipitations = 0f;
        for (Float precipitation : precipitations) {
            totalPrecipitations += precipitation;
        }

        context.write(key, new Text(String.format("%.1f %.1f %.1f %.1f",
                maxTemp, minTemp, totalPrecipitations, avgTemp)));
    }
}
