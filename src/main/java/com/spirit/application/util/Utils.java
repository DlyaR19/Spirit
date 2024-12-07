package com.spirit.application.util;

import java.util.Arrays;

public class Utils {

    private Utils() {
        throw new UnsupportedOperationException("Class cannot be instantiated as it is a utility class");
    }

    public static <T> T[] append(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }
}
