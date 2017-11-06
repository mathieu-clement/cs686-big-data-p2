package edu.usfca.cs.mr.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ObservationTest {
    @Test
    void getFeaturesNewInterface() {
        Observation obs = new Observation("1426377600000\t9mk8dbv1zr8p\t2431.2812\t0.0\t0.0\t-5.5776367\t0.0\t24221.588\t0.0\t0.0\t101380.0\t52.875\t13.0\t0.0\t2.9028778\t4.2837896\t0.0\t395.55603\t0.0\t0.0\t-4.368347\t6.0\t0.0\t0.0\t-0.65234375\t101379.0\t0.0\t0.0\t0.0\t0.0\t311.84128\t568.5\t0.0\t-5000.0\t0.100865975\t-20.0\t0.0\t-37.638824\t3920.0\t101380.0\t291.24802\t0.0\t0.07002258\t0.0\t15.592087\t1.5900003E-5\t21126.736\t200.60687\t36.677795\t10982.238\t0.0\t-18.058548\t374.75\t-2.8323364\t0.0\t12.832815\t0.27407837\t0.0",
                Feature.GEOHASH, Feature.VISIBILITY_SURFACE, Feature.U_COMPONENT_OF_WIND_MAXIMUM_WIND, Feature.TIMESTAMP);

        assertEquals(-2.8323364, (double) obs.getFeature(Feature.U_COMPONENT_OF_WIND_MAXIMUM_WIND, Double.class));
        assertEquals(24221.588, (double) obs.getFeature(Feature.VISIBILITY_SURFACE, Double.class));
        assertEquals("9mk8dbv1zr8p", obs.getGeohash());
        assertEquals("1426377600000", obs.getFeature(Feature.TIMESTAMP, String.class));
    }
}