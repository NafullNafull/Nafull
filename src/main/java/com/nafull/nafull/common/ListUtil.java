package com.nafull.nafull.common;

import java.util.ArrayList;
import java.util.List;

public class ListUtil {
    public static <T> List<T> merge(final List<T> list, final T e) {
        List<T> merged = new ArrayList<>(list);
        merged.add(e);
        return merged;
    }
}
