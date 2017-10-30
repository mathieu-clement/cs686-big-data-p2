package edu.usfca.cs.mr.travel_year;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class TravelYearReducer extends Reducer<Text, Text, Text, Text> {

    private static final Date BEGIN_2015 = new Date(1420070400000L);

    @Override
    protected void reduce(Text geohash, Iterable<Text> daysItr, Context context) throws IOException, InterruptedException {
        Set<Integer> days = new HashSet<>();
        for (Text d : daysItr) {
            days.add(Integer.parseInt(d.toString()));
        }

        for (Integer day : days) {
            if (days.contains(day + 1) && days.contains(day + 2)) {
                try {
                    String monthDay = toMonthDay(day);
                    context.write(geohash, new Text(monthDay));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static String toMonthDay(Integer day) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(BEGIN_2015);
        cal.add(Calendar.DATE, day);
        int month = cal.get(Calendar.MONTH) + 1;
        int dayInMonth = cal.get(Calendar.DAY_OF_MONTH);
        return leftPad(month) + '-' + leftPad(dayInMonth);
    }

    static String leftPad(int i) {
        if (i < 10) return "0" + i;
        return "" + i;
    }
}
