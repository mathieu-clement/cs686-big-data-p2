package edu.usfca.cs.mr.travel_year;

import com.google.common.collect.Sets;
import edu.usfca.cs.mr.util.Observation;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class TravelYearMapper extends Mapper<LongWritable, Text, Text, Text> {

    private static final Set<String> PREFIXES = Sets.newHashSet(
            "dxfy", // Halifax, Nova Scotia
            "dk2y", // Nassau, Bahamas
            "dpxy", // Niagara Falls
            "9whp", // Albuquerque
            "9xhv" // Rocky Mountain
    );
    static {
        for (String prefix : PREFIXES) {
            if (prefix.length() != 4) {
                System.err.println("TravelYearMapper: All prefixes should have the same length.");
                System.exit(1);
            }
        }
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd");

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String valueStr = value.toString();
        if (!PREFIXES.contains(valueStr.substring(0,4))) return; // Filter out irrelevant locations

        Object[] features = Observation.getFeatures(
                valueStr,
                new int[]{1, 2, 8, 10, 13,
                        30, 38, 41, 42, 54},
                new Class<?>[]{String.class, String.class, Integer.class, Boolean.class, Integer.class,
                Boolean.class, Double.class, Float.class, Float.class, Double.class}
        );

        String geohash = (String) features[0];
        String monthDay = DATE_FORMAT.format(new Date(Long.parseLong((String) features[1])));
        int visibility = (int) features[2];
        boolean rain = (boolean) features[3];
        int humidity = (int) features[4];
        boolean freezingRain = (boolean) features[5];
        double vWind = (double) features[6];
        float temperature = (float) features[7];
        float snowCover = (float) features[8];
        double uWind = (double) features[9];
        double windSpeed = Math.sqrt(uWind * uWind + vWind * vWind); // http://colaweb.gmu.edu/dev/clim301/lectures/wind/wind-uv.html

        if (temperature >= 18 && temperature < 28 /* deg C */ &&
                snowCover < 0.01 /* m */ &&
                windSpeed < 5.555 /* m/s */ &&
                humidity >= 30 && humidity < 50 &&
                !rain &&
                !freezingRain &&
                visibility >= 5000 /* m */) {
            context.write(new Text(geohash.substring(0, 4)), new Text(monthDay));
        }
    }
}
