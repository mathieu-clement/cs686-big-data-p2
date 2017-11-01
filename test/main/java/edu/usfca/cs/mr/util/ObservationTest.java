package edu.usfca.cs.mr.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObservationTest {
    @Test
    void getFeaturesSimple() {
        Object[] features = Observation.parseFeatures(
                "hello 7 world 3.141529 1.38064852 ignore",
                new int[]{1, 2, 4, 5},
                new Class[]{String.class, Integer.class, Float.class, Double.class}
        );

        assertTrue(features[0] instanceof String, "String.class");
        assertTrue(features[1] instanceof Integer, "Integer.class");
        assertTrue(features[2] instanceof Float, "Float.class");
        assertTrue(features[3] instanceof Double, "Double.class");

        assertEquals("hello", features[0]);
        assertEquals(7, features[1]);
        assertEquals(3.141529f, features[2]);
        assertEquals(1.38064852, features[3]);
    }

    @Test
    void getFeaturesRealTdvGeohashStr() {
        String tdv = "1426377600000\tdndf9tz5r8eb\t1954.7812\t0.0\t0.0\t-13.327637\t0.0\t24221.588\t3.0\t0.0\t101866.0\t0.0\t1.0\t0.0\t29.402878\t4.0337896\t100.0\t382.80603\t1.0\t10.0\t-1.4933472\t16.0\t0.0\t0.0\t-174.65234\t99495.0\t0.35999998\t0.5\t41.75\t0.0\t346.84128\t878.0\t4.0\t506.5\t0.15086599\t9.5\t0.42249998\t16.861176\t3280.0\t101887.0\t286.74802\t0.0\t199.82002\t140.0\t-9.532913\t0.1750159\t18126.736\t220.73187\t103.427795\t24182.238\t0.0\t8.191452\t0.0\t38.167664\t0.083749995\t15.957815\t3.5240784\t0.083749995";
        Object[] features = Observation.parseFeatures(
                tdv,
                new int[]{2},
                new Class[]{String.class}
        );
        String geohashStr = (String) features[0];
        assertEquals("dndf9tz5r8eb", geohashStr);
    }

    @Test
    void getFeaturesRealTdvGeohashSpatialRange() {
        String tdv = "1426377600000\tdndf9tz5r8eb\t1954.7812\t0.0\t0.0\t-13.327637\t0.0\t24221.588\t3.0\t0.0\t101866.0\t0.0\t1.0\t0.0\t29.402878\t4.0337896\t100.0\t382.80603\t1.0\t10.0\t-1.4933472\t16.0\t0.0\t0.0\t-174.65234\t99495.0\t0.35999998\t0.5\t41.75\t0.0\t346.84128\t878.0\t4.0\t506.5\t0.15086599\t9.5\t0.42249998\t16.861176\t3280.0\t101887.0\t286.74802\t0.0\t199.82002\t140.0\t-9.532913\t0.1750159\t18126.736\t220.73187\t103.427795\t24182.238\t0.0\t8.191452\t0.0\t38.167664\t0.083749995\t15.957815\t3.5240784\t0.083749995";
        Object[] features = Observation.parseFeatures(
                tdv,
                new int[]{2},
                new Class[]{SpatialRange.class}
        );
        assertEquals(Geohash.decodeHash("dndf9tz5r8eb"), features[0]);
    }

}