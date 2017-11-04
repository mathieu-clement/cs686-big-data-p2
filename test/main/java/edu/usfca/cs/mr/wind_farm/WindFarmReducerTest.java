package edu.usfca.cs.mr.wind_farm;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static edu.usfca.cs.mr.wind_farm.WindFarmReducer.percentile;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WindFarmReducerTest {
    @Test
    void testPercentile() {
        List<Float> list = Arrays.asList(0f, 1f, 2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f, 10f);

        assertEquals(5f, percentile(list, 50));
        assertEquals(6f, percentile(list, 60));
        assertEquals(7f, percentile(list, 70));
        assertEquals(8f, percentile(list, 80));
        assertEquals(9f, percentile(list, 90));
    }

}