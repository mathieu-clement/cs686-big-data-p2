package edu.usfca.cs.mr.util;

import java.util.*;

public class Observation {

    private String[] features;
    private List<Integer> indices;

    public Observation(String tdv, Feature... features) {
        this.indices = toIndices(features);
        this.features = parseFeatures(tdv);
    }

    private List<Integer> toIndices(Feature[] features) {
        List<Integer> result = new ArrayList<>(features.length);
        for (Feature feature : features) {
            result.add(feature.getIndex());
        }
        return result;
    }

    private String[] parseFeatures(String tdv) {
        Collections.sort(indices);
        String[] result = new String[indices.size()];

        StringTokenizer itr = new StringTokenizer(tdv, "\t");
        int i = 1;
        int j = 0;
        while (itr.hasMoreTokens()) {
            String value = itr.nextToken();
            if (i == indices.get(j) /* atIndex() */) {
                result[j] = value;
                j++;
            }
            if (j == indices.size()) {
                break;
            }
            i++;
        }

        return result;
    }

    public <T> T getFeature(Feature feature, Class<T> clazz) {
        return (T) toObject(features[resolveIndex(feature)], clazz);
    }

    private int resolveIndex(Feature feature) {
        return this.indices.indexOf(feature.getIndex());
    }

    public String getGeohash() {
        return getFeature(Feature.GEOHASH, String.class);
    }

    private static Object toObject(String s, Class<?> c) {
        if (c == String.class) return s;
        if (c == Float.class) return Float.parseFloat(s);
        if (c == Double.class) return Double.parseDouble(s);
        if (c == Boolean.class) return "1".equals(s) || "1.0".equals(s);
        if (c == Integer.class) return Integer.parseInt(s);
        if (c == Long.class) return Long.parseLong(s);
        if (c == SpatialRange.class) return Geohash.decodeHash(s);
        throw new IllegalArgumentException("Class " + c.getName() + " is not supported.");
    }

    public static float toCelsius(float kelvin) {
        return kelvin - 273.15f;
    }

}
