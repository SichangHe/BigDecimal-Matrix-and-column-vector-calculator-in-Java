package com.matrixCaculator;

import com.testClient.readArray;
import com.vectorCalculation.VectorBD;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class MatrixBD {

    // row and column
    private final int ro, co;

    // for the big decimal calculation, we need a math context
    private final MathContext mc = MathContext.DECIMAL128;

    // store with array
    private final BigDecimal[][] ma;

    // store RREF
    // store solution
    private BigDecimal[][] rref = null, sol = null;

    // store RREF pivots
    // store free variables
    private final ArrayList<Integer> piC = new ArrayList<>(), freeC = new ArrayList<>();

    // create scanner
    private static Scanner sc = new Scanner(System.in);

    // constructor
    /**create a matrix based on 2D array
     * @param matrixArray all entries in the matrix as 2D array
     */
    public MatrixBD(BigDecimal[][] matrixArray) {
        if (matrixArray == null) throw new NullPointerException("matrixArray cannot be null");
        ro = matrixArray.length;
        co = matrixArray[0].length;
        ma = new BigDecimal[ro][co];
        for (int i = 0; i < ro; i++) System.arraycopy(matrixArray[i], 0, ma[i], 0, co);
    }
    /**create a matrix based on 2D array
     * @param matrixArray all entries in the matrix as 2D array
     */
    public MatrixBD(String[][] matrixArray) {
        if (matrixArray == null) throw new NullPointerException("matrixArray cannot be null");
        ro = matrixArray.length;
        co = matrixArray[0].length;
        ma = new BigDecimal[ro][co];
        for (int i = 0; i < ro; i++) {
            for (int j = 0; j < co; j++) ma[i][j] = new BigDecimal(matrixArray[i][j]);
        }
    }
    /**create a matrix based on 2D array
     * @param matrixArray all entries in the matrix as 2D array
     */
    public MatrixBD(int[][] matrixArray) {
        if (matrixArray == null) throw new NullPointerException("matrixArray cannot be null");
        ro = matrixArray.length;
        co = matrixArray[0].length;
        ma = new BigDecimal[ro][co];
        for (int i = 0; i < ro; i++) {
            for (int j = 0; j < co; j++) ma[i][j] = BigDecimal.valueOf(matrixArray[i][j]);
        }
    }
    /**create a matrix based on 2D array
     * @param matrixArray all entries in the matrix as 2D array
     */
    public MatrixBD(long[][] matrixArray) {
        if (matrixArray == null) throw new NullPointerException("matrixArray cannot be null");
        ro = matrixArray.length;
        co = matrixArray[0].length;
        ma = new BigDecimal[ro][co];
        for (int i = 0; i < ro; i++) {
            for (int j = 0; j < co; j++) ma[i][j] = BigDecimal.valueOf(matrixArray[i][j]);
        }
    }
    /**create a matrix based on 2D array
     * @param matrixArray all entries in the matrix as 2D array
     */
    public MatrixBD(double[][] matrixArray) {
        if (matrixArray == null) throw new NullPointerException("matrixArray cannot be null");
        ro = matrixArray.length;
        co = matrixArray[0].length;
        ma = new BigDecimal[ro][co];
        for (int i = 0; i < ro; i++) {
            for (int j = 0; j < co; j++) ma[i][j] = BigDecimal.valueOf(matrixArray[i][j]);
        }
    }

    /**construct a matrix of 1 column from a column vector
     * @param columnVector the column vector to transform
     */
    public MatrixBD(VectorBD columnVector) {
        ro = columnVector.dim();
        co = 1;
        ma = new BigDecimal[ro][1];
        BigDecimal[] ve = columnVector.toArray();
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
    public BigDecimal[][] toArray(){
        return ma;
    }

    // extract column vector
    /**generate a column vector of a specific column
     * @param columnIndex the index of column you want to extract as a column vector
     * @return the column vector of column columnIndex
     */
    public VectorBD vec(int columnIndex){
        BigDecimal[] result = new BigDecimal[ro];
        for(int i = 0; i < ro; i++) {
            result[i] = ma[i][columnIndex];
        }
        return new VectorBD(result);
    }

    // multiply by vector
    /** multiply the matrix by a vector with dimension equal to the columns of this matrix
     * @param that the vector to multiply
     * @return a new vector gained by multiplying the matrix and the vector
     */
    public VectorBD time(VectorBD that){
        if(co != that.dim()) throw new IllegalArgumentException("the number of columns of the matrix must equal to the number of dimensions of the vector");
        BigDecimal[] result = new BigDecimal[ro];
        BigDecimal[] ve = that.toArray();
        for (int i = 0; i < ro; i++) {
            result[i] = BigDecimal.ZERO;
            for(int j = 0; j < co; j++) {
                result[i] = result[i].add(ma[i][j].multiply(ve[j]));
            }
        }
        return new VectorBD(result);
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
    public MatrixBD RREF(){
        // check if rref is already calculated
        if (rref != null) return new MatrixBD(rref);

        // copy ma as rref
        rref = new BigDecimal[ro][co];
        for (int i = 0; i < ro; i++) System.arraycopy(ma[i], 0, rref[i], 0, co);

        // assign variables
        BigDecimal[] tempArray = new BigDecimal[co];
        BigDecimal tempDec;

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
                    if (rref[tR][iC].compareTo(BigDecimal.ZERO) != 0) {
                        // swap it to the top
                        System.arraycopy(rref[tR], 0, tempArray, 0, co);
                        System.arraycopy(tempArray, 0, rref[topR], 0, co);

                        // record it to the array list
                        piC.add(iC);

                        // make pivot 1
                        tempDec = rref[topR][iC];

                        for (int i = iC; i < co; i++) {
                            rref[topR][i] = rref[topR][i].divide(tempDec, mc).stripTrailingZeros();
                        }

                        // check all entries below for nonzero entries and eliminate them
                        for (int checkR = topR + 1; checkR < ro; checkR++) {
                            // nonzero entries found
                            if (rref[checkR][iC].compareTo(BigDecimal.ZERO) != 0) {
                                // subtract all of them to cancel the entry
                                tempDec = rref[checkR][iC];
                                for (int i = iC; i < co; i++) {
                                    rref[checkR][i] = rref[checkR][i].subtract(rref[topR][i].multiply(tempDec).setScale(33, RoundingMode.HALF_UP)).stripTrailingZeros();
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
                if (rref[checkR][piC.get(piNow)].compareTo(BigDecimal.ZERO) != 0) {
                    tempDec = rref[checkR][piC.get(piNow)];

                    // eliminate the entry, from piNow's column to the rightmost
                    for(int i = piC.get(piNow); i < co; i++) {
                        rref[checkR][i] = rref[checkR][i].subtract(rref[piNow][i].multiply(tempDec)).stripTrailingZeros();
                    }
                }
            }
        }
        return new MatrixBD(rref);
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
    public VectorBD[] sol(){
        // make sure RREF is called
        RREF();

        // no solution when the last pivot is in the last column
        // piC.get(piC.size() - 1: the column of the last pivot
        if (piC.get(piC.size() - 1) == co - 1) return null;

        // initialize the vector array to return
        VectorBD[] result = new VectorBD[freeVar().length + 1];

        // check if the one solution is already generated
        if (sol != null) {
            for (int i = 0; i < freeVar().length + 1; i++) result[i] = new VectorBD(sol[i]);
            return result;
        }

        // when it has 1 solution
        if (freeVar().length == 0) {
            // initialize the array of 0s to store vectors to be multiplied by free variables
            sol = new BigDecimal[freeVar().length + 1][co - 1];
            for (int i = 0; i < freeVar().length + 1; i++) for (int j = 0; j < co - 1; j++) sol[i][j] = BigDecimal.ZERO;

            // proceed from the last pivot to the first
            // piNow: the pivot being considered, also the row it is in, the column it is in is piC.get(piNow)
            for (int piNow = piC.size() - 1; piNow >= 0; piNow--) {
                // assign the variable corresponding to the pivot's column new value from the last column (the constant)
                sol[0][piC.get(piNow)] = rref[piNow][co - 1];

                // check variables from the right to the pivot (piC.get(piNow) + 1) to the one but rightmost (co - 2)
                // subtract other variables from the one but rightmost to the one next to this pivot
                // iC: the column and the variable dealing with
                for (int iC = piC.get(piNow) + 1; iC < co - 1; iC++) {
                    sol[0][piC.get(piNow)] = sol[0][piC.get(piNow)].subtract(rref[piNow][iC].multiply(sol[0][iC]));
                }
            }
            result[0] = new VectorBD(sol[0]);

            return result;
        }

        // when it has infinite solutions

        // define temp variables
        BigDecimal tempDec;
        int pC, tempInd;

        // initialize the array of 0s to store vectors to be multiplied by free variables
        // set all the free variables' own places to 1
        sol = new BigDecimal[freeVar().length + 1][co - 1];

        // i = 0
        for (int j = 0; j < co - 1; j++) sol[0][j] = BigDecimal.ZERO;
        for (int i = 1; i < freeVar().length + 1; i++) {
            for (int j = 0; j < freeVar()[i - 1]; j++) sol[i][j] = BigDecimal.ZERO;
            sol[i][freeVar()[i - 1]] = BigDecimal.ONE;
            for (int j = freeVar()[i - 1] + 1; j < co - 1; j++) sol[i][j] = BigDecimal.ZERO;
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
                    sol[tempInd + 1][pC] = sol[tempInd + 1][pC].subtract(tempDec);
                }

                // for base variables
                else {
                    // subtract the constant
                    sol[0][pC] = sol[0][pC].subtract(sol[0][iC].multiply(tempDec));

                    // proceed sol[jR][iC] from the right to the variable being checked (piNow + 1) to (co - 1)
                    for (int jR = piNow + 1; jR < co; jR++) {
                        // store the index of the free variable as tempInd
                        tempInd = Arrays.binarySearch(freeVar(), jR);

                        // subtract the free variables
                        if (tempInd > 0) sol[tempInd + 1][pC] = sol[tempInd + 1][pC].subtract(sol[tempInd + 1][iC].multiply(tempDec));
                    }
                }
            }
        }
        // translate the 2D array to vector array
        for (int i = 0; i < freeVar().length + 1; i++) result[i] = new VectorBD(sol[i]);

        return result;
    }

    /**instruct the user to input a matrix through standard input
     * @return the matrix input from standard input
     */
    public static MatrixBD StdReadMatrix() {
        System.out.print("Number of rows: ");
        int a = sc.nextInt();

        System.out.print("Number of column: ");
        int b = sc.nextInt();

        String[][] in = new String[a][b];
        for (int i = 0; i < a; i++) {
            System.out.print("Row " + (i + 1) + ": ");
            in[i] = readArray.stringArray(b);
        }
        System.out.println();
        return new MatrixBD(in);
    }

    /**print the free variables to standard output
     */
    public void printFreeVar() {
        RREF();
        System.out.print("Free variables: ");
        for (Integer integer : freeC) {
            System.out.print("x" + (integer + 1) + ", ");
        }
        System.out.println();
    }

    /**print the solution to standard output
     */
    public void printSol() {
        System.out.print("Solution: ");
        if (sol != null) {
            System.out.print(new VectorBD(sol[0]));
            for(int i = 0; i < freeC.size(); i++) {
                System.out.print(" + " + "x" +(freeC.get(i) + 1) + " * " + new VectorBD(sol[i + 1]));
            }
        }
        System.out.println();
    }
    // test client
    public static void main(String[] args) {
        MatrixBD a = new MatrixBD(new int[][]{{2,3,4}, {-1,5,-3}, {6,-2,8}});
        VectorBD vec = new VectorBD(new int[]{1,1,1});
        MatrixBD b = new MatrixBD(new int[][]{{1,6,2,-5,-2,-4}, {0,0,2,-8,-1,3}, {0,0,0,0,1,7}});

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
