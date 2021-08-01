package com.testClient;

import com.matrixCaculator.MatrixBD;

import java.util.Arrays;

public class test {
    public static void main(String[] args) {
        int[][] in = new int[5][6];
        for (int i = 0; i < 5; i++) {
            in[i] = readArray.integerArray(6);
        }
        MatrixBD prob = new MatrixBD(in);

        System.out.println(prob);
        System.out.println(prob.RREF());
        System.out.println(Arrays.toString(prob.freeVar()));
        System.out.println(Arrays.toString(prob.sol()));


    }
}
