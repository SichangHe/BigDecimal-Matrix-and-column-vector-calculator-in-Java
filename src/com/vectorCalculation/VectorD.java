package com.vectorCalculation;

/**
 * this class provides a way to manipulate Euclidean vectors using double
 *
 * @author Steven He 何思畅
 */
public class VectorD {
    // dimension
    private final int n;

    //container array
    private final double[] ve;

    // constructor
    /**
     * @param vectorArray all elements in the vector as an array
     */
    public VectorD(double[] vectorArray) {
        if (vectorArray == null) throw new NullPointerException("the array you put in is null");
        n = vectorArray.length;
        ve = new double[n];
        System.arraycopy(vectorArray, 0, ve, 0, n);
    }

    /**
     * construct a new vector of 0s with certain dimensions
     *
     * @param dimension the dimension of the vector
     */
    public VectorD(int dimension) {
        if (dimension <= 0) throw new IllegalArgumentException("the dimension of the vector must be greater than 0");
        n = dimension;
        ve = new double[n];
    }

    // operations
    // add
    /**
     * sum up two vectors of the same dimension
     *
     * @param that the vector to be added
     * @return sum of the two vectors
     */
    public VectorD add(VectorD that) {
        if (that == null) throw new IllegalArgumentException("the vector added must not be null");
        if (this.n != that.n) throw new IllegalArgumentException("the dimensions of the two vectors must be the same");
        double[] result = new double[n];
        for (int i = 0; i < n; i++) result[i] = this.ve[i] + that.ve[i];
        return new VectorD(result);
    }

    // minus
    /**
     * subtract this vector with that vector in the same dimension
     *
     * @param that the vector to be subtracted
     * @return subtraction of the two vectors
     */
    public VectorD min(VectorD that) {
        if (that == null) throw new IllegalArgumentException("the vector to subtract must not be null");
        if (this.n != that.n) throw new IllegalArgumentException("the dimensions of the two vectors must be the same");
        double[] result = new double[n];
        for (int i = 0; i < n; i++) result[i] = this.ve[i] - that.ve[i];
        return new VectorD(result);
    }

    // dot product
    /**
     * return the dot product of two vectors of the same dimension
     *
     * @param that the vector to be dotted
     * @return dot product of the two vectors
     */
    public double dot(VectorD that) {
        if (that == null) throw new IllegalArgumentException("the vector added must not be null");
        if (this.n != that.n) throw new IllegalArgumentException("the dimensions of the two arrays must be the same");
        double result = 0;
        for (int i = 0; i < n; i++) result += this.ve[i] * that.ve[i];
        return result;
    }

    // times scalar
    /**
     * multiply a vector by a scalar
     *
     * @param that the scalar to be multiplied
     * @return multiplication of the vector and the scaler
     */
    public VectorD time(double that) {
        double[] result = new double[n];
        for (int i = 0; i < n; i++) result[i] = this.ve[i] * that;
        return new VectorD(result);
    }

    /**
     * multiply a vector by a scalar
     *
     * @param that the scalar to be multiplied
     * @return multiplication of the vector and the scaler
     */
    public VectorD time(int that) {
        double[] result = new double[n];
        for (int i = 0; i < n; i++) result[i] = this.ve[i] * that;
        return new VectorD(result);
    }

    /**
     * multiply a vector by a scalar
     *
     * @param that the scalar to be multiplied
     * @return multiplication of the vector and the scaler
     */
    public VectorD time(long that) {
        double[] result = new double[n];
        for (int i = 0; i < n; i++) result[i] = this.ve[i] * that;
        return new VectorD(result);
    }

    // magnitude
    /**
     * calculate the magnitude (or norm, or length) of this vector
     *
     * @return the magnitude of this vector
     */
    public double mag() {
        double result = 0;
        for (int i = 0; i < n; i++) result += this.ve[i] * this.ve[i];
        result = Math.sqrt(result);
        return result;
    }

    // unit vector
    /**
     * calculate the corresponding unit vector of this vector
     *
     * @return the corresponding unit vector of this vector
     */
    public VectorD unit() {
        double[] result = new double[n];
        double magnitude = this.mag();
        for (int i = 0; i < n; i++) {
            result[i] = this.ve[i] / magnitude;
        }
        return new VectorD(result);
    }

    // cross product
    /**
     * calculate the cross product of two vectors of dimension 3
     *
     * @param that the vector to cross with
     * @return the cross product of two vectors
     */
    public VectorD cros(VectorD that) {
        if (this.n != 3 || that.n != 3)
            throw new IllegalArgumentException("both vectors must have a dimension of 3 to have a cross product");
        double[] result = new double[]{this.ve[1] * that.ve[2] - (this.ve[2] * that.ve[1]), this.ve[2] * that.ve[0] - (this.ve[0] * (that.ve[2])), this.ve[0] * that.ve[1] - (this.ve[1] * that.ve[0])};
        return new VectorD(result);
    }

    // extend
    /**
     * create a vector with more dimensions by filling up the new dimensions with 0s
     *
     * @param newDimension the dimension of the new vector
     * @return a new vector equivalent to the original one on those dimensions it has, but with more dimensions
     */
    public VectorD ext(int newDimension) {
        if (n >= newDimension)
            throw new IllegalArgumentException("the new dimension must be more than the present dimension");
        double[] result = new double[newDimension];
        if (n >= 0) System.arraycopy(ve, 0, result, 0, n);
        for (int i = n; i < newDimension; i++) result[i] = 0;
        return new VectorD(result);
    }

    // shorten
    /**make a shorter version of this vector
     * @param newDimension the dimension of the new vector
     * @return a new vector equivalent to the original one in those dimensions remain, but shorter
     */
    public VectorD shor(int newDimension) {
        if (newDimension <= 0) throw new IllegalArgumentException("the new dimension must be greater than 0");
        double[] result = new double[newDimension];
        System.arraycopy(ve, 0, result, 0, newDimension);
        return new VectorD(result);
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
    public double[] toArray() {
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
        VectorD a = new VectorD(new double[]{8, 4, 3});
        VectorD b = new VectorD(new double[]{2,0,1});
        VectorD c = new VectorD(4);

        System.out.println(a + ", " + b);
        System.out.println(a.add(b) + ", " + a.min(b));
        System.out.println(a.dot(b));
        System.out.println(c);
        System.out.println(a.mag());
        System.out.println(a.unit());
        System.out.println(a.cros(b));
        System.out.println(b.ext(5));
        System.out.println(a.time(5));

    }
}