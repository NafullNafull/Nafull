package com.nafull.nafull.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ListUtil {
    public static <T> List<T> merge(final List<T> list, final T e) {
        List<T> merged = new ArrayList<>(list);
        merged.add(e);
        return merged;
    }

    public static <T> T random(final List<T> list) {
        int badgeIdx = new Random().nextInt(list.size());
        return list.get(badgeIdx);
    }

    public static <T> T random(final T[] list) {
        int badgeIdx = new Random().nextInt(list.length);
        return list[badgeIdx];
    }
}
