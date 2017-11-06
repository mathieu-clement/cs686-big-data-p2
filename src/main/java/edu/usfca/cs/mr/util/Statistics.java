package edu.usfca.cs.mr.util;

import java.util.List;

public class Statistics {
    // Precondition: list is sorted
    public static float percentile(List<Float> list, float percentile) {
        int i = (int) (list.size() * percentile / 100f);

        if (i < 0) i = 0;
        else if (i >= list.size()) i = list.size() - 1;

        return list.get(i);
    }

    public static float average(List<Float> list) {
        int count = 0;
        float sum = 0f;
        for (Float f : list) {
            sum += f;
            count++;
        }
        return sum / count;
    }
}
