package com.matrixCaculator;

import com.vectorCalculation.VectorD;

import java.util.ArrayList;
import java.util.Arrays;

public class MatrixD {

    // row and column
    private final int ro, co;

    // store with array
    private final double[][] ma;

    // store RREF
    // store solution
    private double[][] rref = null, sol = null;

    // store RREF pivots
    // store free variables
    private final ArrayList<Integer> piC = new ArrayList<>(), freeC = new ArrayList<>();

    // constructor
    /**create a matrix based on 2D array
     * @param matrixArray all entries in the matrix as 2D array
     */
    public MatrixD(double[][] matrixArray) {
        if (matrixArray == null) throw new NullPointerException("matrixArray cannot be null");
        ro = matrixArray.length;
        co = matrixArray[0].length;
        ma = new double[ro][co];
        for (int i = 0; i < ro; i++) {
            System.arraycopy(matrixArray[i], 0, ma[i], 0, co);
        }
    }

    /**construct a matrix of 1 column from a column vector
     * @param columnVector the column vector to transform
     */
    public MatrixD(VectorD columnVector) {
        ro = columnVector.dim();
        co = 1;
        ma = new double[ro][1];
        double[] ve = columnVector.toArray();
        for (int i = 0; i < ro; i++) ma[i][1] = ve[i];
    }

    // column
    /**
     * @return number of columns of this matrix
     */
    public int co(){
        return co;
    }

    // row
    /**
     * @return number of rows of this matrix
     */
    public int ro(){
        return ro;
    }

    // to array
    /**
     * @return a 2D array corresponding to this matrix
     */
    public double[][] toArray(){
        return ma;
    }

    // extract column vector
    /**generate a column vector of a specific column
     * @param columnIndex the index of column you want to extract as a column vector
     * @return the column vector of column columnIndex
     */
    public VectorD vec(int columnIndex){
        double[] result = new double[ro];
        for(int i = 0; i < ro; i++) {
            result[i] = ma[i][columnIndex];
        }
        return new VectorD(result);
    }

    // multiply by vector
    /** multiply the matrix by a vector with dimension equal to the columns of this matrix
     * @param that the vector to multiply
     * @return a new vector gained by multiplying the matrix and the vector
     */
    public VectorD time(VectorD that){
        if(co != that.dim()) throw new IllegalArgumentException("the number of columns of the matrix must equal to the number of dimensions of the vector");
        double[] result = new double[ro];
        double[] ve = that.toArray();
        for (int i = 0; i < ro; i++) {
            result[i] = 0;
            for(int j = 0; j < co; j++) {
                result[i] = result[i] + ma[i][j] * ve[j];
            }
        }
        return new VectorD(result);
    }

    // to string
    public String toString() {
        return Arrays.deepToString(ma);
    }

    // simplify the matrix to reduced echelon form
    /**generate the reduced (row) echelon form of this matrix
     * <p>a reduced echelon form has step-like, all 1s leading entries and is unique for each matrix
     * @return the reduced (row) echelon form of this matrix
     */
    public MatrixD RREF(){
        // check if rref is already calculated
        if (rref != null) return new MatrixD(rref);

        // copy ma as rref
        rref = new double[ro][co];
        for (int i = 0; i < ro; i++) System.arraycopy(ma[i], 0, rref[i], 0, co);

        // assign variables
        double[] tempArray = new double[co];
        double tempDec;

        // forward phase

        // find pivots
        // iC: the column being searched, from 0 to (co - 1)
        // topR: the highest row still considered, from 0 to (ro - 1)
        for (int iC = 0, topR = 0; iC < co; iC++) {
            // no enough rows so the rest become free variables
            if (topR >= ro) {
                // it is a free variable if not a constant
                if (iC < co - 1) freeC.add(iC);
            }

            // there are still rows to be searched
            else {
                // tR: current row scanning
                for (int tR = topR; tR < ro; tR++) {
                    // found nonzero entry, use it as a pivot
                    if (rref[tR][iC] != 0) {
                        // swap it to the top
                        System.arraycopy(rref[tR], 0, tempArray, 0, co);
                        System.arraycopy(tempArray, 0, rref[topR], 0, co);

                        // record it to the array list
                        piC.add(iC);

                        // make pivot 1
                        tempDec = rref[topR][iC];

                        for (int i = iC; i < co; i++) {
                            rref[topR][i] = rref[topR][i] / tempDec;
                        }

                        // check all entries below for nonzero entries and eliminate them
                        for (int checkR = topR + 1; checkR < ro; checkR++) {
                            // nonzero entries found
                            if (rref[checkR][iC] != 0) {
                                // subtract all of them to cancel the entry
                                tempDec = rref[checkR][iC];
                                for (int i = iC; i < co; i++) {
                                    rref[checkR][i] = rref[checkR][i] - rref[topR][i] * tempDec;
                                }
                            }
                        }

                        // now all is done, another top row eliminated
                        topR++;

                        // stop looking for entries in this column
                        break;
                    }

                    // it is the last row, no nonzero entry in this column
                    if (tR > ro - 2) {
                        // not the last column, it is a free variable
                        if (iC < co - 1) freeC.add(iC);
                    }
                }
            }
        }

        // backward phase

        // number of pivots
        int pivot = piC.size();

        // check all pivots from the bottom one
        // piNow: the pivot focused now, from (pivot - 1) to 1
        // there is no point checking for pivot 0 as nothing is above it
        // it is also the row index for sure
        for (int piNow = pivot - 1; piNow > 0; piNow--) {
            // check upwards one-by-one from the pivots and eliminate nonzero entries
            // checkR: the row being checked
            for (int checkR = piNow - 1; checkR >= 0; checkR--) {
                // found nonzero entry
                if (rref[checkR][piC.get(piNow)] != 0) {
                    tempDec = rref[checkR][piC.get(piNow)];

                    // eliminate the entry, from piNow's column to the rightmost
                    for(int i = piC.get(piNow); i < co; i++) {
                        rref[checkR][i] = rref[checkR][i] - rref[piNow][i] * tempDec;
                    }
                }
            }
        }
        return new MatrixD(rref);
    }

    // give out the free variables
    public Integer[] freeVar(){
        RREF();
        return freeC.toArray(new Integer[0]);
    }

    // solve the matrix
    /**generate all the solutions of the matrix
     * <p></p>if there is no solution, return null
     * <p>if there is 1 solution, return a vector array of that one solution
     * <p>if there are multiple solutions, return a vector array, where the first vector is one of the solution (assuming all free variables are 0) and the rest are parameters to multiply by each free variables
     * <p>for example: solution set := {freeVar()[0],..., freeVar()[freeVal().length - 1] ∈ R | sol()[0] + freeVar()[0] * sol()[1] + ... + freeVar()[freeVar().length - 1] * sol()[freeVal().length]}
     * <p></p>
     * @return all the solutions of the matrix, null if no solution
     */
    public VectorD[] sol(){
        // make sure RREF is called
        RREF();

        // no solution when the last pivot is in the last column
        // piC.get(piC.size() - 1: the column of the last pivot
        if (piC.get(piC.size() - 1) == co - 1) return null;

        // initialize the vector array to return
        VectorD[] result = new VectorD[freeVar().length + 1];

        // check if the one solution is already generated
        if (sol != null) {
            for (int i = 0; i < freeVar().length + 1; i++) result[i] = new VectorD(sol[i]);
            return result;
        }

        // when it has 1 solution
        if (freeVar().length == 0) {
            // initialize the array of 0s to store vectors to be multiplied by free variables
            sol = new double[freeVar().length + 1][co - 1];
            for (int i = 0; i < freeVar().length + 1; i++) for (int j = 0; j < co - 1; j++) sol[i][j] = 0;

            // proceed from the last pivot to the first
            // piNow: the pivot being considered, also the row it is in, the column it is in is piC.get(piNow)
            for (int piNow = piC.size() - 1; piNow >= 0; piNow--) {
                // assign the variable corresponding to the pivot's column new value from the last column (the constant)
                sol[0][piC.get(piNow)] = rref[piNow][co - 1];

                // check variables from the right to the pivot (piC.get(piNow) + 1) to the one but rightmost (co - 2)
                // subtract other variables from the one but rightmost to the one next to this pivot
                // iC: the column and the variable dealing with
                for (int iC = piC.get(piNow) + 1; iC < co - 1; iC++) {
                    sol[0][piC.get(piNow)] = sol[0][piC.get(piNow)] - rref[piNow][iC] * sol[0][iC];
                }
            }
            result[0] = new VectorD(sol[0]);

            return result;
        }

        // when it has infinite solutions

        // define temp variables
        double tempDec;
        int pC, tempInd;

        // initialize the array of 0s to store vectors to be multiplied by free variables
        // set all the free variables' own places to 1
        sol = new double[freeVar().length + 1][co - 1];

        // i = 0
        for (int j = 0; j < co - 1; j++) sol[0][j] = 0;
        for (int i = 1; i < freeVar().length + 1; i++) {
            for (int j = 0; j < freeVar()[i - 1]; j++) sol[i][j] = 0;
            sol[i][freeVar()[i - 1]] = 1;
            for (int j = freeVar()[i - 1] + 1; j < co - 1; j++) sol[i][j] = 0;
        }

        // proceed pivot rref[piNow][pC] from rightmost (piC.size() - 1) to 0
        // piNow: the pivot being considered, also the row it is in, the column it is in is piC.get(piNow)
        for (int piNow = piC.size() - 1; piNow >= 0; piNow--) {
            // store the column of the pivot as pC
            pC = piC.get(piNow);

            // assign the variable corresponding to the pivot's column new value from the last column (the constant)
            sol[0][pC] = rref[piNow][co - 1];

            // check variables rref[piNow][iC] from the right to the pivot (pC + 1) to the one but rightmost (co - 2)
            // subtract other variables from the one but rightmost to the one next to this pivot
            // iC: the column and the variable dealing with
            for (int iC = pC + 1; iC < co - 1; iC++) {
                // store the value of the element as tempDec
                tempDec = rref[piNow][iC];

                // store the index of the free variable as tempInd
                tempInd = Arrays.binarySearch(freeVar(), iC);

                // for free variables
                if (tempInd >= 0) {
                    // subtract the value from the corresponding place
                    sol[tempInd + 1][pC] = sol[tempInd + 1][pC] - tempDec;
                }

                // for base variables
                else {
                    // subtract the constant
                    sol[0][pC] = sol[0][pC] - sol[0][iC] * tempDec;

                    // proceed sol[jR][iC] from the right to the variable being checked (piNow + 1) to (co - 1)
                    for (int jR = piNow + 1; jR < co; jR++) {
                        // store the index of the free variable as tempInd
                        tempInd = Arrays.binarySearch(freeVar(), jR);

                        // subtract the free variables
                        if (tempInd > 0) sol[tempInd + 1][pC] = sol[tempInd + 1][pC] - sol[tempInd + 1][iC] * tempDec;
                    }
                }
            }
        }
        // translate the 2D array to vector array
        for (int i = 0; i < freeVar().length + 1; i++) result[i] = new VectorD(sol[i]);

        return result;
    }

    // test client
    public static void main(String[] args) {
        MatrixD a = new MatrixD(new double[][]{{2,3,4}, {-1,5,-3}, {6,-2,8}});
        VectorD vec = new VectorD(new double[]{1,1,1});
        MatrixD b = new MatrixD(new double[][]{{1,6,2,-5,-2,-4}, {0,0,2,-8,-1,3}, {0,0,0,0,1,7}});

        System.out.println(a);
        System.out.println(a.time(vec));
        System.out.println(a.vec(1));
        System.out.println(a.RREF());
        System.out.println(b.RREF());
        System.out.println(Arrays.toString(b.freeVar()));
        System.out.println(Arrays.toString(a.sol()));
        System.out.println(Arrays.toString(b.sol()));
    }
}
