package edu.usfca.cs.mr.travel_year;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;

import static edu.usfca.cs.mr.travel_year.TravelYearReducer.toMonthDay;

class TravelYearReducerTest {
    @Test
    void testToMonthDay() throws ParseException {
        Assertions.assertEquals("01-01", toMonthDay(1));
        Assertions.assertEquals("01-02", toMonthDay(2));
        Assertions.assertEquals("01-31", toMonthDay(31));
        Assertions.assertEquals("02-01", toMonthDay(32));
        Assertions.assertEquals("12-31", toMonthDay(365));
    }

}