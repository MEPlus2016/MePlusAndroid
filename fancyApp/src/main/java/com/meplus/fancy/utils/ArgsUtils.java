package com.meplus.fancy.utils;

import java.util.Comparator;
import java.util.TreeMap;

/**
 * Created by dandanba on 4/20/16.
 */
public class ArgsUtils {
    public static TreeMap<String, String> generateArags() {
        return new TreeMap<>((Comparator<String>) (lhs, rhs) -> {
            return lhs.toLowerCase().compareTo(rhs.toLowerCase());
        });
    }
}
