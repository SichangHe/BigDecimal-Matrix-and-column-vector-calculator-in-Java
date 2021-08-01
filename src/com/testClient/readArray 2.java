package com.testClient;

import java.util.Scanner;

public class readArray {
    // create scanner
    private static Scanner sc = new Scanner(System.in);

    /**read an integer array of length {@code length} from standard input
     * @param length the length of the array we want to read
     * @return an integer array read from standard input
     */
    public static int[] integerArray(int length) {
        // initialize the array
        int[] result = new int[length];

        for (int i = 0; i < length; i++) {
            result[i] = sc.nextInt();
        }

        return result;
    }

    /**read an long array of length {@code length} from standard input
     * @param length the length of the array we want to read
     * @return an long array read from standard input
     */
    public static long[] longArray(int length) {
        // initialize the array
        long[] result = new long[length];

        for (int i = 0; i < length; i++) {
            result[i] = sc.nextLong();
        }

        return result;
    }

    /**read an double array of length {@code length} from standard input
     * @param length the length of the array we want to read
     * @return an double array read from standard input
     */
    public static double[] doubleArray(int length) {
        // initialize the array
        double[] result = new double[length];

        for (int i = 0; i < length; i++) {
            result[i] = sc.nextDouble();
        }

        return result;
    }

    /**read an string array of length {@code length} from standard input
     * @param length the length of the array we want to read
     * @return an string array read from standard input
     */
    public static String[] stringArray(int length) {
        // initialize the array
        String[] result = new String[length];

        for (int i = 0; i < length; i++) {
            result[i] = sc.next();
        }

        return result;
    }
}
