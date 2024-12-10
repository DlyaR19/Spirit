package com.spirit.application.util;

import java.util.Arrays;

/**
 * Utility-Klasse für allgemeine Operationen.
 */

public class Utils {

    /**
     * Privater Konstruktor verhindert Instanziierung
     * Wirft Exception, da diese Klasse nur statische Methoden enthält
     */
    private Utils() {
        throw new UnsupportedOperationException("Class cannot be instantiated as it is a utility class");
    }

    /**
     * Generische Methode zum Anhängen eines Elements an ein Array
     * @param arr Das ursprüngliche Array
     * @param element Das anzuhängende Element
     * @param <T> Der Typ der Array-Elemente
     * @return Ein neues Array mit dem angehängten Element
     */
    public static <T> T[] append(T[] arr, T element) {
        final int N = arr.length; // Speichert die ursprüngliche Array-Länge
        arr = Arrays.copyOf(arr, N + 1); // Erstellt Kopie mit einer zusätzlichen Position
        arr[N] = element; // Fügt neues Element am Ende ein
        return arr;
    }
}
