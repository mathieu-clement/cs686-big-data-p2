package edu.usfca.cs.mr.util;

import java.util.StringTokenizer;

public class Observation {
    /**
     * Returns specific features with correct Java type
     * @param tdv Tab-separated values
     * @param indices Indices of features, starting at index 1, sorted asc.
     * @param deserializationClasses one of: {@link String}, {@link Integer}, {@link Float}, {@link Double}, {@link SpatialRange} (from Geohash)
     * @return asked features
     * @see <a href="https://www.cs.usfca.edu/~mmalensek/courses/cs686/projects/project-2-data.html">Data dictionary</a>
     */
    public static Object[] getFeatures(String tdv, int[] indices, Class[] deserializationClasses) {
        Object[] result = new Object[indices.length];

        StringTokenizer itr = new StringTokenizer(tdv);
        int i = 1;
        int j = 0;
        while (itr.hasMoreTokens()) {
            String value = itr.nextToken();
            if (i == indices[j]) {
                result[j] = toObject(value, deserializationClasses[j]);
                j++;
            }
            if (j == indices.length) {
                break;
            }
            i++;
        }

        return result;
    }

    private static Object toObject(String s, Class c) {
        if (c == String.class) return s;
        if (c == Integer.class) return Integer.parseInt(s);
        if (c == Float.class) return Float.parseFloat(s);
        if (c == Double.class) return Double.parseDouble(s);
        if (c == SpatialRange.class) return Geohash.decodeHash(s);
        throw new IllegalArgumentException("Class " + c.getName() + " is not supported.");
    }

}
