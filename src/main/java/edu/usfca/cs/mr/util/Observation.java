package edu.usfca.cs.mr.util;

import java.util.*;

public class Observation {

    private String[] features;
    private List<Integer> indices;

    public Observation(String tdv, Feature... features) {
        this.indices = toIndices(features);
        this.features = parseFeatures(tdv, indices);
    }

    private static List<Integer> copyAndSort(List<Integer> unsorted) {
        List<Integer> sortedIndices = new ArrayList<>(unsorted);
        Collections.sort(sortedIndices);
        return sortedIndices;
    }

    private List<Integer> toIndices(Feature[] features) {
        List<Integer> result = new ArrayList<>(features.length);
        for (Feature feature : features) {
            result.add(feature.getIndex());
        }
        return result;
    }

    private static String[] parseFeatures(String tdv, List<Integer> indices) {
        indices = copyAndSort(indices);
        String[] result = new String[indices.size()];

        StringTokenizer itr = new StringTokenizer(tdv);
        int i = 1;
        int j = 0;
        while (itr.hasMoreTokens()) {
            String value = itr.nextToken();
            if (i == indices.get(i) /* atIndex() */) {
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

    /**
     * Returns specific features with correct Java type
     *
     * @param tdv                    Tab-separated values
     * @param indices                Indices of features, starting at index 1, sorted asc.
     * @param deserializationClasses one of: {@link String}, {@link Integer}, {@link Float}, {@link Double}, {@link Boolean}, {@link SpatialRange} (from Geohash)
     * @return asked features
     * @see <a href="https://www.cs.usfca.edu/~mmalensek/courses/cs686/projects/project-2-data.html">Data dictionary</a>
     * @deprecated
     */
    @Deprecated
    public static Object[] parseFeatures(String tdv, int[] indices, Class<?>[] deserializationClasses) {
        Object[] result = new Object[indices.length];

        StringTokenizer itr = new StringTokenizer(tdv);
        int i = 1;
        int j = 0;
        while (itr.hasMoreTokens()) {
            String value = itr.nextToken();
            if (i == indices[j]) {
                try {
                    result[j] = toObject(value, deserializationClasses[j]);
                    j++;
                } catch (NumberFormatException nfe) {
                    throw new RuntimeException("Couldn't parse '" + value + "' as " + deserializationClasses[j].getSimpleName() + " from column #" + i, nfe);
                }
            }
            if (j == indices.length) {
                break;
            }
            i++;
        }

        return result;
    }

    public static Object[] parseFeatures(String tdv, Feature[] features, Class<?>[] deserializationClasses) {
        int[] indices = new int[features.length];
        for (int i = 0; i < features.length; i++) {
            indices[i] = features[i].getIndex();
        }
        return parseFeatures(tdv, features, deserializationClasses);
    }

    private static Object toObject(String s, Class<?> c) {
        if (c == String.class) return s;
        if (c == Float.class) return Float.parseFloat(s);
        if (c == Double.class) return Double.parseDouble(s);
        if (c == Boolean.class) return "1".equals(s) || "1.0".equals(s);
        if (c == Integer.class) return Integer.parseInt(s);
        if (c == SpatialRange.class) return Geohash.decodeHash(s);
        throw new IllegalArgumentException("Class " + c.getName() + " is not supported.");
    }

}
