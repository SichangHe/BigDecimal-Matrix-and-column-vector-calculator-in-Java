package com.vectorCalculation;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * this class provides a way to manipulate Euclidean vectors
 * <p>implementing the class BigDecimal, the result of this is very precise
 *
 * @author Steven He 何思畅
 */
public class VectorBD {
    // dimension
    private final int n;

    //container array
    private final BigDecimal[] ve;

    // for the big decimal calculation, we need a math context
    private final MathContext mc = MathContext.DECIMAL128;

    // constructor
    /**
     * @param vectorArray all elements in the vector as an array
     */
    public VectorBD(BigDecimal[] vectorArray) {
        if (vectorArray == null) throw new NullPointerException("the array you put in is null");
        n = vectorArray.length;
        ve = new BigDecimal[n];
        System.arraycopy(vectorArray, 0, ve, 0, n);
    }

    /**
     * @param vectorArray all elements in the vector as an array
     */
    public VectorBD(String[] vectorArray) {
        if (vectorArray == null) throw new NullPointerException("the array you put in is null");
        n = vectorArray.length;
        ve = new BigDecimal[n];
        for (int i = 0; i < n; i++) ve[i] = new BigDecimal(vectorArray[i]);
    }

    /**
     * @param vectorArray all elements in the vector as an array
     */
    public VectorBD(int[] vectorArray) {
        if (vectorArray == null) throw new NullPointerException("the array you put in is null");
        n = vectorArray.length;
        ve = new BigDecimal[n];
        for (int i = 0; i < n; i++) ve[i] = BigDecimal.valueOf(vectorArray[i]);
    }

    /**
     * @param vectorArray all elements in the vector as an array
     */
    public VectorBD(long[] vectorArray) {
        if (vectorArray == null) throw new NullPointerException("the array you put in is null");
        n = vectorArray.length;
        ve = new BigDecimal[n];
        for (int i = 0; i < n; i++) ve[i] = BigDecimal.valueOf(vectorArray[i]);
    }

    /**
     * @param vectorArray all elements in the vector as an array
     */
    public VectorBD(double[] vectorArray) {
        if (vectorArray == null) throw new NullPointerException("the array you put in is null");
        n = vectorArray.length;
        ve = new BigDecimal[n];
        for (int i = 0; i < n; i++) ve[i] = BigDecimal.valueOf(vectorArray[i]);
    }

    /**
     * construct a new vector of 0s with certain dimensions
     *
     * @param dimension the dimension of the vector
     */
    public VectorBD(int dimension) {
        if (dimension <= 0) throw new IllegalArgumentException("the dimension of the vector must be greater than 0");
        n = dimension;
        ve = new BigDecimal[n];
        for (int i = 0; i < n; i++) ve[i] = BigDecimal.ZERO;
    }

    // operations
    // add
    /**
     * sum up two vectors of the same dimension
     *
     * @param that the vector to be added
     * @return sum of the two vectors
     */
    public VectorBD add(VectorBD that) {
        if (that == null) throw new IllegalArgumentException("the vector added must not be null");
        if (this.n != that.n) throw new IllegalArgumentException("the dimensions of the two vectors must be the same");
        BigDecimal[] result = new BigDecimal[n];
        for (int i = 0; i < n; i++) result[i] = this.ve[i].add(that.ve[i]);
        return new VectorBD(result);
    }

    // minus
    /**
     * subtract this vector with that vector in the same dimension
     *
     * @param that the vector to be subtracted
     * @return subtraction of the two vectors
     */
    public VectorBD min(VectorBD that) {
        if (that == null) throw new IllegalArgumentException("the vector to subtract must not be null");
        if (this.n != that.n) throw new IllegalArgumentException("the dimensions of the two vectors must be the same");
        BigDecimal[] result = new BigDecimal[n];
        for (int i = 0; i < n; i++) result[i] = this.ve[i].subtract(that.ve[i]);
        return new VectorBD(result);
    }

    // dot product
    /**
     * return the dot product of two vectors of the same dimension
     *
     * @param that the vector to be dotted
     * @return dot product of the two vectors
     */
    public BigDecimal dot(VectorBD that) {
        if (that == null) throw new IllegalArgumentException("the vector added must not be null");
        if (this.n != that.n) throw new IllegalArgumentException("the dimensions of the two arrays must be the same");
        BigDecimal result = BigDecimal.ZERO;
        for (int i = 0; i < n; i++) result = result.add(this.ve[i].multiply(that.ve[i]));
        return result;
    }

    // times scalar
    /**
     * multiply a vector by a scalar
     *
     * @param that the scalar to be multiplied
     * @return multiplication of the vector and the scaler
     */
    public VectorBD time(BigDecimal that) {
        BigDecimal[] result = new BigDecimal[n];
        for (int i = 0; i < n; i++) result[i] = this.ve[i].multiply(that);
        return new VectorBD(result);
    }

    /**
     * multiply a vector by a scalar
     *
     * @param that the scalar to be multiplied
     * @return multiplication of the vector and the scaler
     */
    public VectorBD time(String that) {
        BigDecimal[] result = new BigDecimal[n];
        for (int i = 0; i < n; i++) result[i] = this.ve[i].multiply(new BigDecimal(that));
        return new VectorBD(result);
    }

    /**
     * multiply a vector by a scalar
     *
     * @param that the scalar to be multiplied
     * @return multiplication of the vector and the scaler
     */
    public VectorBD time(int that) {
        BigDecimal[] result = new BigDecimal[n];
        for (int i = 0; i < n; i++) result[i] = this.ve[i].multiply(BigDecimal.valueOf(that));
        return new VectorBD(result);
    }

    /**
     * multiply a vector by a scalar
     *
     * @param that the scalar to be multiplied
     * @return multiplication of the vector and the scaler
     */
    public VectorBD time(long that) {
        BigDecimal[] result = new BigDecimal[n];
        for (int i = 0; i < n; i++) result[i] = this.ve[i].multiply(BigDecimal.valueOf(that));
        return new VectorBD(result);
    }

    /**
     * multiply a vector by a scalar
     *
     * @param that the scalar to be multiplied
     * @return multiplication of the vector and the scaler
     */
    public VectorBD time(double that) {
        BigDecimal[] result = new BigDecimal[n];
        for (int i = 0; i < n; i++) result[i] = this.ve[i].multiply(BigDecimal.valueOf(that));
        return new VectorBD(result);
    }

    // magnitude
    /**
     * calculate the magnitude (or norm, or length) of this vector
     *
     * @return the magnitude of this vector
     */
    public BigDecimal mag() {
        BigDecimal result = BigDecimal.ZERO;
        for (int i = 0; i < n; i++) result = result.add(this.ve[i].multiply(this.ve[i]));
        result = result.sqrt(mc);
        return result;
    }

    // unit vector
    /**
     * calculate the corresponding unit vector of this vector
     *
     * @return the corresponding unit vector of this vector
     */
    public VectorBD unit() {
        BigDecimal[] result = new BigDecimal[n];
        BigDecimal magnitude = this.mag();
        for (int i = 0; i < n; i++) {
            result[i] = this.ve[i].divide(magnitude, mc);
        }
        return new VectorBD(result);
    }

    // cross product
    /**
     * calculate the cross product of two vectors of dimension 3
     *
     * @param that the vector to cross with
     * @return the cross product of two vectors
     */
    public VectorBD cros(VectorBD that) {
        if (this.n != 3 || that.n != 3)
            throw new IllegalArgumentException("both vectors must have a dimension of 3 to have a cross product");
        BigDecimal[] result = new BigDecimal[]{this.ve[1].multiply(that.ve[2]).subtract(this.ve[2].multiply(that.ve[1])), this.ve[2].multiply(that.ve[0]).subtract(this.ve[0].multiply(that.ve[2])), this.ve[0].multiply(that.ve[1]).subtract(this.ve[1].multiply(that.ve[0]))};
        return new VectorBD(result);
    }

    // extend
    /**
     * create a vector with more dimensions by filling up the new dimensions with 0s
     *
     * @param newDimension the dimension of the new vector
     * @return a new vector equivalent to the original one on those dimensions it has, but with more dimensions
     */
    public VectorBD ext(int newDimension) {
        if (n >= newDimension)
            throw new IllegalArgumentException("the new dimension must be more than the present dimension");
        BigDecimal[] result = new BigDecimal[newDimension];
        if (n >= 0) System.arraycopy(ve, 0, result, 0, n);
        for (int i = n; i < newDimension; i++) result[i] = BigDecimal.ZERO;
        return new VectorBD(result);
    }

    // shorten
    /**make a shorter version of this vector
     * @param newDimension the dimension of the new vector
     * @return a new vector equivalent to the original one in those dimensions remain, but shorter
     */
    public VectorBD shor(int newDimension) {
        if (newDimension <= 0) throw new IllegalArgumentException("the new dimension must be greater than 0");
        BigDecimal[] result = new BigDecimal[newDimension];
        System.arraycopy(ve, 0, result, 0, newDimension);
        return new VectorBD(result);
    }

    // dimension
    /**
     * @return the number of dimension of this vector
     */
    public int dim() {
        return n;
    }

    // to array
    /**
     * @return the array corresponding to the vector
     */
    public BigDecimal[] toArray() {
        return ve;
    }

    // to string
    public String toString() {
        StringBuilder result = new StringBuilder("⟨" + this.ve[0]);
        for (int i = 1; i < n; i++) result.append(", ").append(this.ve[i]);
        result.append("⟩");
        return result.toString();
    }

    // test client
    public static void main(String[] args) {
        VectorBD a = new VectorBD(new int[]{8, 4, 3});
        VectorBD b = new VectorBD(new BigDecimal[]{new BigDecimal("2"), BigDecimal.ZERO, BigDecimal.ONE});
        VectorBD c = new VectorBD(4);

        System.out.println(a + ", " + b);
        System.out.println(a.add(b) + ", " + a.min(b));
        System.out.println(a.dot(b));
        System.out.println(c);
        System.out.println(a.mag());
        System.out.println(a.unit());
        System.out.println(a.cros(b));
        System.out.println(b.ext(5));
        System.out.println(a.time(new BigDecimal("5")));

    }
}