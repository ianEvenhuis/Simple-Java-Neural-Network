package nn;

import org.ianShowTechniek.Util.ToolBox;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Matrix implements Serializable{

    //static var's
    public static final String TANH = "tanh";
    public static final String DTANH = "dtanh";
    public static final String SIGMOID = "sigmoid";
    public static final String DSIGMOID = "dsigmoid";
    public static final String MUTATE = "mutate";
    public static double lowerMutate = -0.1;
    public static double higherMutate = 0.1;

    private double[][] matrix;
    private int rows;
    private int cols;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = new double[rows][cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.matrix[i][j] = 0;
            }
        }
    }

    //<editor-fold desc="Matrix self functions">
    // This fills the matrix with random values (gaussian distribution)
    public void randomize() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.matrix[i][j] = new Random().nextGaussian();
            }
        }
    }

    // Take the matrix and make it a 1 dimensional array
    public double[] toArray() {
        // Add all the values to the array
        double[] arr = new double[rows * cols];
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                arr[i * j] = (this.matrix[i][j]);
            }
        }
        return arr;
    }


    // This transposes a matrix
    // rows X cols --> cols X rows
    public Matrix transpose() {
        Matrix result = new Matrix(this.cols, this.rows);
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {
                result.matrix[i][j] = this.matrix[j][i];
            }
        }
        return result;
    }

    // This makes a copy of the matrix
    public Matrix copy() {
        Matrix result = new Matrix(this.rows, this.cols);
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {
                result.matrix[i][j] = this.matrix[i][j];
            }
        }
        return result;
    }

    // This adds another matrix or a single value
    public void add(Matrix other) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.matrix[i][j] += other.matrix[i][j];
            }
        }
    }

    // This adds another matrix or a single value
    public void add(double other) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.matrix[i][j] += other;
            }
        }
    }


    // This multiplies another matrix or a single value
    // This is different than the dot() function!
    public void multiply(Matrix other) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.matrix[i][j] *= other.matrix[i][j];
            }
        }
    }

    // This multiplies another matrix or a single value
    // This is different than the dot() function!
    public void multiply(double other) {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.matrix[i][j] *= other;
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="These are some static functions to operate on a matrix">
    // This is the trickiest one
    // Takes a function and applies it to all values in the matrix
    public static Matrix map(Matrix m, Activation formule) {
        Matrix result = new Matrix(m.rows, m.cols);
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {
                switch (formule){

                    case TAHN:
                        result.matrix[i][j] = tanh(m.matrix[i][j]);
                        break;
                    case DTAHN:
                        result.matrix[i][j] = dtanh(m.matrix[i][j]);
                        break;
                    case SIGMOID:
                        result.matrix[i][j] = sigmoid(m.matrix[i][j]);
                        break;
                    case DSIGMOID:
                        result.matrix[i][j] = dSigmoid(m.matrix[i][j]);
                        break;
                    case MUTATE:
                        result.matrix[i][j] = mutate(m.matrix[i][j]);
                        break;
                }
            }
        }
        return result;
    }

    // Subtracts one matrix from another
    public static Matrix subtract(Matrix a, Matrix b) {
        Matrix result = new Matrix(a.rows, a.cols);
        for (int i = 0; i < result.rows; i++) {
            for (int j = 0; j < result.cols; j++) {
                result.matrix[i][j] = a.matrix[i][j] - b.matrix[i][j];
            }
        }
        return result;
    }


    // Multiplies two matrices together
    public static Matrix dot(Matrix a, Matrix b) {
        // Won't work if columns of A don't equal columns of B
        if (a.cols != b.rows) {
            System.out.println("Incompatible matrix sizes!");
            return null;
        }
        // Make a new matrix
        Matrix result = new Matrix(a.rows, b.cols);
        for (int i = 0; i < a.rows; i++) {
            for (int j = 0; j < b.cols; j++) {
                // Sum all the rows of A times columns of B
                float sum = 0;
                for (int k = 0; k < a.cols; k++) {
                    sum += a.matrix[i][k] * b.matrix[k][j];
                }
                // New value
                result.matrix[i][j] = sum;
            }
        }
        return result;
    }


    // Turn a 1 dimensional array into a matrix
    public static Matrix fromArray(double[] array) {
        Matrix m = new Matrix(array.length, 1);
        for (int i = 0; i < array.length; i++) {
            m.matrix[i][0] = array[i];
        }
        return m;
    }
    //</editor-fold>

    //<editor-fold desc="Math Functions to modify data in a matrix">
    private static double sigmoid(double x) {
        return 1 / (1 + Math.pow(Math.E, -x));

    }

    // This is the Sigmoid derivative!
    private static double dSigmoid(double x) {
        return x * (1 - x);
    }

    private static double tanh(double x) {
        return Math.tanh(x);
    }

    private static double dtanh(double x) {
        return 1 / (Math.pow(Math.cosh(x), 2));
    }

    // This is how we adjust weights ever so slightly
    private static double mutate(double x) {
        if (ToolBox.random(1) < 0.1) {
            return x + random(lowerMutate, higherMutate);
        } else {
            return x;
        }
    }
    //</editor-fold>

    //<editor-fold desc="Getters and Setters">
    public static double random(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max + 1);
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public Matrix setMatrix(double[][] matrix) {
        this.matrix = matrix;
        return this;
    }

    public int getRows() {
        return rows;
    }

    public Matrix setRows(int rows) {
        this.rows = rows;
        return this;
    }

    public int getCols() {
        return cols;
    }

    public Matrix setCols(int cols) {
        this.cols = cols;
        return this;
    }

    public double getLowerMutate() {
        return lowerMutate;
    }

    public Matrix setLowerMutate(double lowerMutate) {
        this.lowerMutate = lowerMutate;
        return this;
    }

    public double getHigherMutate() {
        return higherMutate;
    }

    public Matrix setHigherMutate(double higherMutate) {
        this.higherMutate = higherMutate;
        return this;
    }
    //</editor-fold>
}
