package com.testClient;

import com.matrixCaculator.MatrixBD;

public class solveMatrixTemplate {

    public static void main(String[] args) {
        MatrixBD prob = MatrixBD.StdReadMatrix();

        System.out.println("Your input: " + prob);
        System.out.println("Reduced echelon form: " + prob.RREF());

        prob.printFreeVar();

        prob.printSol();
    }
}
