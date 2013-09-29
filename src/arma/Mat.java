package arma;

import java.util.Random;

import org.ejml.data.DenseMatrix64F;
import org.ejml.ops.CommonOps;
import org.ejml.ops.RandomMatrices;

/**
 * Mat is a (dense) matrix with calls similar to the Armadillo C++ Algebra
 * Library by Conrad Sanderson et al., but is based on DenseMatrix64F from Peter
 * Abeles’ Efficient Java Matrix Library (EJML).
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 * @version 1.0
 * 
 * @see <a href="http://arma.sourceforge.net/">Armadillo C++ Algebra Library</a>
 * @see <a href="http://efficient-java-matrix-library.googlecode.com">Efficient
 *      Java Matrix Library</a>
 */
public class Mat {

	/**
	 * The internal data representation of the matrix.
	 */
	private DenseMatrix64F _matrix;

	/**
	 * The number of rows of the matrix.
	 */
	public int n_rows;

	/**
	 * The number of columns of the matrix.
	 */
	public int n_cols;

	/**
	 * The number of elements (same as {@link #n_rows} * {@link #n_cols}) of the
	 * matrix.
	 */
	public int n_elem;

	/**
	 * Creates a new matrix based on DenseMatrix64F and updates {@link #n_rows},
	 * {@link #n_cols} and {@link #n_elem}. Its called by all other
	 * constructors.
	 * 
	 * @param matrix The matrix to be copied.
	 */
	Mat(DenseMatrix64F matrix) {
		_matrix = new DenseMatrix64F(matrix);

		updateAttributes();
	}

	/**
	 * Creates a new matrix with {@link #n_elem} = 0.
	 */
	public Mat() {
		this(new DenseMatrix64F());
	}
	
	/**
	 * Creates a new matrix based on a 2d array.
	 * 
	 * @param matrix The 2d array with the structure double[rows][columns].
	 */
	public Mat(double[][] matrix) {
		this(new DenseMatrix64F(matrix));
	}

	/**
	 * Creates an new zero matrix with {@link #n_rows} = numberOfRows and {@link #n_cols} = numberOfColumns.
	 * 
	 * @param numberOfRows The number of rows of the matrix.
	 * @param numberOfColumns The number of columns of the matrix.
	 * 
	 * @see #zeros(int, int)
	 */
	public Mat(int numberOfRows, int numberOfColumns) {
		this(new DenseMatrix64F(numberOfRows, numberOfColumns));
	}
	
	/**
	 * Creates a new matrix by copying the provided matrix.
	 * 
	 * @param matrix The matrix to be copied.
	 */
	public Mat(Mat matrix) {
		this(new DenseMatrix64F(matrix.memptr()));
	}

	public double at(int index) {
		return _matrix.get(index);
	}

	public double at(int row, int column) {
		return _matrix.get(row, column);
	}

	public void at(int index, Op operation, double value) {
		_matrix.set(index, getResult(_matrix.get(index), operation, value));
	}

	public void at(int row, int column, Op operation, double value) {
		_matrix.set(row, column,
				getResult(_matrix.get(row, column), operation, value));
	}

	public Mat col(int column) {
		DenseMatrix64F result = new DenseMatrix64F(n_rows, 1);

		for (int i = 0; i < n_rows; i++) {
			result.set(i, _matrix.get(i, column));
		}

		return new Mat(result);
	}

	public void col(int column, Op operation, Mat columnVector) {
		for (int i = 0; i < n_rows; i++) {
			at(i, column, operation, columnVector.at(i));
		}
	}

	public Mat row(int row) {
		DenseMatrix64F result = new DenseMatrix64F(1, n_cols);

		for (int i = 0; i < n_cols; i++) {
			result.set(i, _matrix.get(row, i));
		}

		return new Mat(result);
	}

	public void row(int row, Op operation, Mat rowVector) {
		for (int i = 0; i < n_cols; i++) {
			at(row, i, operation, rowVector.at(i));
		}
	}

	public Mat diag() {
		DenseMatrix64F result = new DenseMatrix64F(1, Math.min(n_rows, n_cols));
		CommonOps.extractDiag(_matrix, result);
		return new Mat(result);
	}

	public void diag(Op operation, double value) {
		int length = Math.min(n_cols, n_rows);
		for (int i = 0; i < length; i++) {
			at(i, i, operation, value);
		}
	}

	public void diag(Op operation, Mat diagonal) {
		for (int i = 0; i < diagonal.n_elem; i++) {
			at(i, i, operation, diagonal.at(i));
		}
	}

	public void each_col(Op operation, Mat columnVector) {
		for (int i = 0; i < n_cols; i++) {
			col(i, operation, columnVector);
		}
	}

	public void each_row(Op operation, Mat rowVector) {
		for (int i = 0; i < n_rows; i++) {
			row(i, operation, rowVector);
		}
	}

	public Mat elem(Mat selection) {
		int length = 0;
		for (int i = 0; i < selection.n_elem; i++) {
			if (selection.at(i) > 0) {
				length++;
			}
		}

		DenseMatrix64F result = new DenseMatrix64F(length, 1);

		int index = 0;
		for (int i = 0; i < selection.n_elem; i++) {
			if (selection.at(i) > 0) {
				result.set(index, _matrix.get(i));
				index++;
			}
		}

		return new Mat(result);
	}

	public void elem(Mat selection, Op operation, Mat otherMatrix) {
		int index = 0;
		for (int i = 0; i < selection.n_elem; i++) {
			if (selection.at(i) > 0) {
				at(i, operation, otherMatrix.at(index));
				index++;
			}
		}
	}

	public void fill(double value) {
		CommonOps.fill(_matrix, value);
	}

	public DenseMatrix64F memptr() {
		return _matrix;
	}

	public void resize(int numberOfRows, int numberOfCols) {

	}

	public Mat t() {
		DenseMatrix64F result = new DenseMatrix64F(n_rows, n_cols);
		CommonOps.transpose(_matrix, result);
		return new Mat(result);
	}

	public Mat i() {
		DenseMatrix64F result = new DenseMatrix64F(n_rows, n_cols);
		CommonOps.invert(_matrix, result);
		return new Mat(result);
	}

	public void set_size(int numberOfRows, int numberOfCols) {
		if (n_rows != numberOfRows || n_cols != numberOfCols) {
			_matrix = new DenseMatrix64F(numberOfRows, numberOfCols);

			updateAttributes();
		}
	}

	@Override
	public String toString() {
		return _matrix.toString();
	}

	public static Mat zeros(int numberOfRows, int numberOfCols) {
		return new Mat(numberOfRows, numberOfCols);
	}

	public static Mat ones(int numberOfRows, int numberOfCols) {
		Mat matrix = Mat.zeros(numberOfRows, numberOfCols);
		matrix.fill(1);
		return matrix;
	}

	public static Mat eye(int numberOfRows, int numberOfCols) {
		return new Mat(CommonOps.identity(numberOfRows, numberOfCols));
	}

	public static Mat randn(int numberOfRows, int numberOfColumns, Random rng) {
		DenseMatrix64F result = new DenseMatrix64F(numberOfRows,
				numberOfColumns);

		int numberOfElements = numberOfRows * numberOfColumns;
		for (int i = 0; i < numberOfElements; i++) {
			result.set(i, rng.nextGaussian());
		}

		return new Mat(result);
	}

	public static Mat randu(int numberOfRows, int numberOfColumns, Random rng) {
		return new Mat(RandomMatrices.createRandom(numberOfRows,
				numberOfColumns, rng));
	}

	public Mat plus(double value) {
		DenseMatrix64F result = new DenseMatrix64F(n_rows, n_cols);
		CommonOps.add(_matrix, value, result);
		return new Mat(result);
	}

	public Mat plus(Mat otherMatrix) {
		DenseMatrix64F result = new DenseMatrix64F(otherMatrix.n_rows,
				otherMatrix.n_cols);
		CommonOps.add(_matrix, otherMatrix.memptr(), result);
		return new Mat(result);
	}

	public Mat minus(double value) {
		DenseMatrix64F result = new DenseMatrix64F(n_rows, n_cols);
		CommonOps.add(_matrix, -value, result);
		return new Mat(result);
	}

	public Mat minus(Mat otherMatrix) {
		DenseMatrix64F result = new DenseMatrix64F(otherMatrix.n_rows,
				otherMatrix.n_cols);
		CommonOps.sub(_matrix, otherMatrix.memptr(), result);
		return new Mat(result);
	}

	public Mat times(double value) {
		return elemTimes(value);
	}

	public Mat times(Mat otherMatrix) {
		DenseMatrix64F result = new DenseMatrix64F(otherMatrix.n_rows,
				otherMatrix.n_cols);
		CommonOps.mult(_matrix, otherMatrix.memptr(), result);
		return new Mat(result);
	}

	public Mat elemTimes(double value) {
		DenseMatrix64F result = new DenseMatrix64F(n_rows, n_cols);
		CommonOps.scale(value, _matrix, result);
		return new Mat(result);
	}

	public Mat elemTimes(Mat otherMatrix) {
		DenseMatrix64F result = new DenseMatrix64F(otherMatrix.n_rows,
				otherMatrix.n_cols);
		CommonOps.elementMult(_matrix, otherMatrix.memptr(), result);
		return new Mat(result);
	}

	public Mat divide(double value) {
		return elemDivide(value);
	}

	public Mat divide(Mat otherMatrix) {
		DenseMatrix64F result = new DenseMatrix64F(otherMatrix.n_rows,
				otherMatrix.n_cols);
		DenseMatrix64F inverse = new DenseMatrix64F(otherMatrix.n_rows,
				otherMatrix.n_cols);

		CommonOps.invert(otherMatrix.memptr(), inverse);
		CommonOps.mult(_matrix, inverse, result);

		return new Mat(result);
	}

	public Mat elemDivide(double value) {
		DenseMatrix64F result = new DenseMatrix64F(n_rows, n_cols);
		CommonOps.divide(value, _matrix, result);
		return new Mat(result);
	}

	public Mat elemDivide(Mat otherMatrix) {
		DenseMatrix64F result = new DenseMatrix64F(otherMatrix.n_rows,
				otherMatrix.n_cols);
		CommonOps.elementDiv(_matrix, otherMatrix.memptr(), result);
		return new Mat(result);
	}

	private static double getResult(double a, Op operation, double b) {
		double result = 0;

		switch (operation) {
			case EQUAL :
				result = b;
				break;
			case PLUS :
				result = a + b;
				break;
			case MINUS :
				result = a - b;
				break;
			case TIMES :
			case ELEMTIMES :
				result = a * b;
				break;
			case DIVIDE :
			case ELEMDIVIDE :
				result = a / b;
				break;
			default :
				throw new UnsupportedOperationException(
						"Only arithmetic operators and equallity are supported.");
		}

		return result;
	}

	private void updateAttributes() {
		n_rows = _matrix.numRows;
		n_cols = _matrix.numCols;
		n_elem = n_rows * n_cols;
	}
}