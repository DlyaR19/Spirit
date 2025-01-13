package com.spirit.application.util;

import java.util.Arrays;

/**
 * Utility class providing helper methods.
 */
public class Utils {

    private Utils() {
        throw new UnsupportedOperationException("Class cannot be instantiated as it is a utility class");
    }

    /**
     * Appends an element to an array and returns the new array.
     * @param arr     the original array
     * @param element the element to append
     * @param <T>     the type of the array elements
     * @return the new array with the appended element
     */
    public static <T> T[] append(T[] arr, T element) {
        final int N = arr.length; // Speichert die ursprüngliche Array-Länge
        arr = Arrays.copyOf(arr, N + 1); // Erstellt Kopie mit einer zusätzlichen Position
        arr[N] = element; // Fügt neues Element am Ende ein
        return arr;
    }
}
