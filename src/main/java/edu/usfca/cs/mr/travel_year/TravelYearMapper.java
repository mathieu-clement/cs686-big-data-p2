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

import static edu.usfca.cs.mr.util.Feature.*;

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

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("D");

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        Observation observation = new Observation(value.toString(),
                TIMESTAMP, GEOHASH, VISIBILITY_SURFACE, CATEGORICAL_FREEZING_RAIN_YES1_NO0_SURFACE,
                RELATIVE_HUMIDITY_ZERODEGC_ISOTHERM,

                CATEGORICAL_RAIN_YES1_NO0_SURFACE, V_COMPONENT_OF_WIND_MAXIMUM_WIND,
                TEMPERATURE_SURFACE, SNOW_DEPTH_SURFACE, U_COMPONENT_OF_WIND_MAXIMUM_WIND
        );

        String geohash = observation.getGeohash();
        String geohashPrefix = geohash.substring(0, 4);
        if (!PREFIXES.contains(geohashPrefix)) return; // Filter out irrelevant locations

        String dayInYear = DATE_FORMAT.format(new Date(Long.parseLong(
                observation.getFeature(TIMESTAMP, String.class)
        )));
        int visibility = (int) (float) observation.getFeature(VISIBILITY_SURFACE, Float.class);
        boolean rain = observation.getFeature(CATEGORICAL_RAIN_YES1_NO0_SURFACE, Boolean.class);
        int humidity = (int) (float) observation.getFeature(RELATIVE_HUMIDITY_ZERODEGC_ISOTHERM, Float.class);
        boolean freezingRain = observation.getFeature(CATEGORICAL_RAIN_YES1_NO0_SURFACE, Boolean.class);
        double vWind = observation.getFeature(V_COMPONENT_OF_WIND_MAXIMUM_WIND, Double.class);
        float temperature = observation.getFeature(TEMPERATURE_SURFACE, Float.class);;
        float snowCover = observation.getFeature(SNOW_DEPTH_SURFACE, Float.class);;
        double uWind = observation.getFeature(U_COMPONENT_OF_WIND_MAXIMUM_WIND, Double.class);
        double windSpeed = Math.sqrt(uWind * uWind + vWind * vWind); // http://colaweb.gmu.edu/dev/clim301/lectures/wind/wind-uv.html

        if (
                temperature >= toKelvin(18) && temperature < toKelvin(28)
                        && snowCover < 0.01 /* m */
//                        && windSpeed < 5.555 /* m/s */
                        && humidity >= 30 && humidity < 50
                        && !rain
                        && !freezingRain
                        && visibility >= 3000 /* m */
                ) {
            context.write(new Text(geohashPrefix), new Text(dayInYear));
        }
    }

    private float toKelvin(float celsius) {
        return celsius + 273.15f;
    }
}
