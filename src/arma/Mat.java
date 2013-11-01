/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 *******************************************************************************/

package arma;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Provides a real-valued dense matrix with double precision. Member functions as well as attributes are similar to the
 * Armadillo C++ Algebra Library (Armadillo) by Conrad Sanderson et al., based on DenseMatrix64F from Peter Abeles'
 * Efficient Java Matrix Library (EJML) Version 0.23 from 21.06.2013.
 * <p>
 * See also the <a href="https://github.com/SebastianNiemann/ArmadilloJava/blob/master/CONVERSION.md">syntax conversion
 * table</a>.
 * <p>
 * If not stated otherwise (marked as non-canonical in case), the provided interfaces is identical to Armadillo (e.g.
 * same ordering of arguments, accepted values, ...). However, this project is based on EJML to provide a pure Java
 * solution, which is why numeric results may slightly differ from the Armadillo C++ Algebra Library.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 * 
 * @see <a href="http://arma.sourceforge.net/">Armadillo C++ Algebra Library</a>
 * @see <a href="http://efficient-java-matrix-library.googlecode.com">Efficient Java Matrix Library</a>
 */
public class Mat extends AbstractMat {

  /**
   * Creates an empty matrix with zero elements.
   */
  public Mat() {
    this(new double[]{});
  }

  /**
   * Creates a column vector with the same number of elements as the provided one-dimensional array.
   * 
   * @param matrix The array
   */
  public Mat(double[] matrix) {
    if (matrix != null && matrix.length > 0) {
      set_size(matrix.length, 1);
      System.arraycopy(matrix, 0, _matrix, 0, matrix.length);
    } else {
      set_size(0, 0);
    }
  }

  /**
   * Creates a matrix with the same size as the provided two-dimensional array.
   * <p>
   * The array is assumed to have a structure like {@code array[rows][columns]}.
   * 
   * @param matrix The array
   * 
   * @throws IllegalArgumentException All rows must have the same length.
   */
  public Mat(double[][] matrix) throws IllegalArgumentException {
    if (matrix != null && matrix.length > 0 && matrix[0].length > 0) {
      int numberOfRows = matrix.length;
      int numberOfColumns = matrix[0].length;

      set_size(numberOfRows, numberOfColumns);
      for (int i = 0; i < n_rows; i++) {
        for (int j = 0; j < n_cols; j++) {
          if (matrix[i].length != n_cols) {
            throw new IllegalArgumentException("All rows must have the same length.");
          }

          _matrix[getElementIndex(i, j)] = matrix[i][j];
        }
      }
    } else {
      set_size(0, 0);
    }
  }

  /**
   * Creates a deep copying of a matrix.
   * 
   * @param matrix The matrix
   */
  public Mat(Mat matrix) {
    this(matrix._matrix);
  }

  /**
   * Creates a deep copying of a matrix.
   * 
   * @param abstractMat A matrix
   */
  protected Mat(AbstractMat abstractMat) {
    set_size(abstractMat.n_rows, abstractMat.n_cols);

    abstractMat.iteratorReset();
    for (int n = 0; n < n_elem; n++) {
      _matrix[n] = abstractMat._matrix[abstractMat.iteratorNext()];
    }
  }

  /**
   * Creates a uninitialised column vector with {@code numberOfElements} elements.
   * 
   * @param numberOfElements The number of elements
   */
  public Mat(int numberOfElements) {
    this(numberOfElements, 1, Fill.NONE);
  }

  /**
   * Creates an uninitialised matrix with {@code numberOfRows} rows and {@code numberOfColumns} columns.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   */
  public Mat(int numberOfRows, int numberOfColumns) {
    this(numberOfRows, numberOfColumns, Fill.NONE);
  }

  /**
   * Creates a column vector with {@code numberOfElements} elements that is filled according to {@code fillType}.
   * 
   * @param numberOfElements The number of elements
   * @param fillType The fill type
   */
  public Mat(int numberOfElements, Fill fillType) {
    this(numberOfElements, 1, fillType);
  }

  /**
   * Creates a matrix with {@code numberOfRows} rows and {@code numberOfColumns} columns that is filled according to
   * {@code fillType}.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * @param fillType The fill type
   * 
   * @throws IllegalArgumentException Does not support {@link Fill#RANDU} or {@link Fill#RANDN}. Use
   *           {@link #Mat(int, int, Fill, Random)} instead.
   */
  public Mat(int numberOfRows, int numberOfColumns, Fill fillType) throws IllegalArgumentException {
    switch (fillType) {
      case NONE:
      case ZEROS:
        zeros(numberOfRows, numberOfColumns);
        break;
      case ONES:
        ones(numberOfRows, numberOfColumns);
        break;
      case EYE:
        eye(numberOfRows, numberOfColumns);
        break;
      case RANDU:
      case RANDN:
        throw new IllegalArgumentException("Does not support Fill.RANDU or FILL.RANDN. Use Mat(int, int, Fill, Random) instead.");
    }
  }

  /**
   * Creates a column vector with {@code numberOfElements} elements that is filled according to {@code fillType}.
   * <p>
   * Works also with {@link Fill#RANDU} and {@link Fill#RANDN}.
   * 
   * @param numberOfElements The number of elements
   * @param fillType The fill type
   * @param rng The pseudorandom generator
   */
  public Mat(int numberOfElements, Fill fillType, Random rng) {
    this(numberOfElements, 1, fillType, rng);
  }

  /**
   * Creates a matrix with {@code numberOfRows} rows and {@code numberOfColumns} columns that is filled according to
   * {@code fillType}.
   * <p>
   * Works also with {@link Fill#RANDU} and {@link Fill#RANDN}.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * @param fillType The fill type
   * @param rng The pseudorandom generator
   */
  public Mat(int numberOfRows, int numberOfColumns, Fill fillType, Random rng) {
    switch (fillType) {
      case NONE:
      case ZEROS:
        zeros(numberOfRows, numberOfColumns);
        break;
      case ONES:
        ones(numberOfRows, numberOfColumns);
        break;
      case EYE:
        eye(numberOfRows, numberOfColumns);
        break;
      case RANDU:
        randu(numberOfRows, numberOfColumns, rng);
        break;
      case RANDN:
        randn(numberOfRows, numberOfColumns, rng);
        break;
    }
  }

  /**
   * Resizes the vector and sets all elements to 0.
   * <p>
   * <b>Non-canonical:</b> Remains a row/column vector if this is invoked for a row/column vector.
   * 
   * @param numberOfElements The number of elements
   */
  public void zeros(int numberOfElements) {
    isNonVectorDetection();

    set_size(numberOfElements);
    zeros();
  }

  /**
   * Resizes the matrix and sets all elements to 0.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   */
  public void zeros(int numberOfRows, int numberOfColumns) {
    set_size(numberOfRows, numberOfColumns);
    zeros();
  }

  /**
   * Resizes the vector and sets all elements to 1.
   * <p>
   * <b>Non-canonical:</b> Remains a row/column vector if this is invoked for a row/column vector.
   * 
   * @param numberOfElements The number of elements
   */
  public void ones(int numberOfElements) {
    isNonVectorDetection();

    if (is_colvec()) {
      set_size(numberOfElements, 1);
    } else {
      set_size(1, numberOfElements);
    }
    ones();
  }

  /**
   * Resizes the matrix and sets all elements to 1.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   */
  public void ones(int numberOfRows, int numberOfColumns) {
    set_size(numberOfRows, numberOfColumns);
    ones();
  }

  /**
   * Resizes the matrix and sets all elements along the main diagonal to 1 and the other elements to 0.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   */
  public void eye(int numberOfRows, int numberOfColumns) {
    zeros(numberOfRows, numberOfColumns);
    eye();
  }

  /**
   * Resizes the matrix and sets each element to a pseudorandom value drawn from the standard uniform distribution on
   * the left-closed and right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b>
   * <ul>
   * <li>Drawn from [0,1) instead of the closed interval [0,1].
   * <li>Remains a row/column vector if this is invoked for a row/column vector.
   * </ul>
   * 
   * @param numberOfElements The number of elements
   * @param rng The pseudorandom generator
   */
  public void randu(int numberOfElements, Random rng) {
    isNonVectorDetection();

    if (is_colvec()) {
      set_size(numberOfElements, 1);
    } else {
      set_size(1, numberOfElements);
    }

    randu(rng);
  }

  /**
   * Resizes the matrix and sets each element to a pseudorandom value drawn from the standard uniform distribution on
   * the left-closed and right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * @param rng The pseudorandom generator
   */
  public void randu(int numberOfRows, int numberOfColumns, Random rng) {
    set_size(numberOfRows, numberOfColumns);
    randu(rng);
  }

  /**
   * Resizes the matrix and sets each element to a pseudorandom value drawn from the standard normal distribution with
   * mean 0.0 and standard deviation 1.0.
   * <p>
   * <b>Non-canonical:</b> Remains a row/column vector if this is invoked for a row/column vector.
   * 
   * @param numberOfElements The number of elements
   * @param rng The pseudorandom generator
   */
  public void randn(int numberOfElements, Random rng) {
    isNonVectorDetection();

    if (is_colvec()) {
      set_size(numberOfElements, 1);
    } else {
      set_size(1, numberOfElements);
    }

    randn(rng);
  }

  /**
   * Resizes the matrix and sets each element to a pseudorandom value drawn from the standard normal distribution with
   * mean 0.0 and standard deviation 1.0.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   * @param rng The pseudorandom generator
   */
  public void randn(int numberOfRows, int numberOfColumns, Random rng) {
    set_size(numberOfRows, numberOfColumns);
    randn(rng);
  }

  /**
   * Expands the matrix by inserting a matrix at column position {@code j}.
   * 
   * @param j The column position
   * @param matrix The matrix
   */
  public void insert_cols(int j, Mat matrix) {
    isNonEqualNumberOfElementsDetection(matrix.n_rows, n_rows);
    isColumnOutOfBoundsDetection(j);

    Mat temp = new Mat(_matrix);
    set_size(n_rows, n_cols + matrix.n_cols);
    // Note: n_cols and n_rows were updated by .set_size()
    cols(0, j - 1, Op.EQUAL, temp.cols(0, j - 1));
    cols(j, matrix.n_cols - 1, Op.EQUAL, matrix);
    cols(matrix.n_cols, n_cols - 1, Op.EQUAL, temp.cols(j, n_cols - matrix.n_cols - 1));
  }

  /**
   * Expands the matrix by inserting a zero matrix from the {@code a}th to {@code b}th column.
   * 
   * @param a The first column position
   * @param b The last column position
   */
  public void insert_cols(int a, int b) {
    insert_cols(a, b, true);
  }

  /**
   * Expands the matrix by inserting a matrix from the {@code a}th to {@code b}th column.
   * <p>
   * The inserted matrix will be filled with 0 ({@code fillWithZeros} = true) or uninitialised otherwise.
   * 
   * @param a The first column position
   * @param b The last column position
   * @param fillWithZeros Whether to be filled with 0
   */
  public void insert_cols(int a, int b, boolean fillWithZeros) {
    isColumnRangeOutOfBoundsDetection(a, b);

    int spanLength = b - a + 1;
    Mat fillMatrix;
    if (fillWithZeros) {
      fillMatrix = new Mat(n_rows, spanLength, Fill.ZEROS);
    } else {
      fillMatrix = new Mat(n_rows, spanLength);
    }

    Mat temp = new Mat(_matrix);
    set_size(n_rows, n_cols + spanLength);
    // Note: n_cols and n_rows were updated by .set_size()
    cols(0, a - 1, Op.EQUAL, temp.cols(0, a - 1));
    cols(a, b, Op.EQUAL, fillMatrix);
    cols(b + 1, n_cols - 1, Op.EQUAL, temp.cols(a, n_cols - spanLength - 1));
  }

  /**
   * Expands the matrix by inserting a matrix at row position {@code j}.
   * 
   * @param i The row position
   * @param matrix The matrix
   */
  public void insert_rows(int i, Mat matrix) {
    isNonEqualNumberOfElementsDetection(matrix.n_cols, n_cols);
    isColumnOutOfBoundsDetection(i);

    Mat temp = new Mat(_matrix);
    set_size(n_rows + matrix.n_rows, n_cols);
    // Note: n_cols and n_rows were updated by .set_size()
    rows(0, i - 1, Op.EQUAL, temp.rows(0, i - 1));
    rows(i, matrix.n_rows - 1, Op.EQUAL, matrix);
    rows(matrix.n_rows, n_rows - 1, Op.EQUAL, temp.rows(i, n_rows - matrix.n_rows - 1));
  }

  /**
   * Expands the matrix by inserting a zero matrix from the {@code a}th to {@code b}th row.
   * 
   * @param a The first row position
   * @param b The last row position
   */
  public void insert_rows(int a, int b) {
    insert_rows(a, b, true);
  }

  /**
   * Expands the matrix by inserting a matrix from the {@code a}th to {@code b}th row.
   * <p>
   * The inserted matrix will be filled with 0 ({@code fillWithZeros} = true) or uninitialised otherwise.
   * 
   * @param a The first row position
   * @param b The last row position
   * @param fillWithZeros Whether to be filled with 0
   */
  public void insert_rows(int a, int b, boolean fillWithZeros) {
    isRowRangeOutOfBoundsDetection(a, b);

    int spanLength = b - a + 1;
    Mat fillMatrix;
    if (fillWithZeros) {
      fillMatrix = new Mat(spanLength, n_cols, Fill.ZEROS);
    } else {
      fillMatrix = new Mat(spanLength, n_cols);
    }

    Mat temp = new Mat(_matrix);
    set_size(n_rows + spanLength, n_cols);
    // Note: n_cols and n_rows were updated by .set_size()
    rows(0, a - 1, Op.EQUAL, temp.rows(0, a - 1));
    rows(a, b, Op.EQUAL, fillMatrix);
    rows(b + 1, n_rows - 1, Op.EQUAL, temp.rows(a, n_rows - spanLength - 1));
  }

  /**
   * Shrinks the matrix by removing the {@code j}th column.
   * 
   * @param j The column position
   */
  public void shed_col(int j) {
    shed_cols(j, j);
  }

  /**
   * Shrinks the matrix by removing the {@code a}th to {@code b}th columns.
   * 
   * @param a The first column position
   * @param b The last column position
   */
  public void shed_cols(int a, int b) {
    isColumnRangeOutOfBoundsDetection(a, b);

    int spanLength = b - a + 1;

    Mat temp = new Mat(_matrix);
    set_size(n_rows, n_cols - spanLength);
    // Note: n_cols and n_rows were updated by .set_size()
    cols(0, a - 1, Op.EQUAL, temp.cols(0, a - 1));
    cols(a, n_cols - 1, Op.EQUAL, temp.cols(b + 1, n_cols + spanLength - 1));
  }

  /**
   * Shrinks the matrix by removing the {@code j}th row.
   * 
   * @param i The row position
   */
  public void shed_row(int i) {
    shed_rows(i, i);
  }

  /**
   * Shrinks the matrix by removing the {@code a}th to {@code b}th rows.
   * 
   * @param a The first row position
   * @param b The last row position
   */
  public void shed_rows(int a, int b) {
    isRowRangeOutOfBoundsDetection(a, b);

    int spanLength = b - a + 1;

    Mat temp = new Mat(_matrix);
    set_size(n_rows - spanLength, n_cols);
    // Note: n_cols and n_rows were updated by .set_size()
    rows(0, a - 1, Op.EQUAL, temp.rows(0, a - 1));
    rows(a, n_rows - 1, Op.EQUAL, temp.rows(b + 1, n_rows + spanLength - 1));
  }

  /**
   * Sets the size of the vector to {@code numberOfElements} elements.
   * <p>
   * Neither guarantees to reuse the values of the elements nor their positions, except the number of elements is the
   * same as before.
   * <p>
   * In this case, the values of the elements are reused and only the size changes.
   * 
   * @param numberOfElements The number of elements
   */
  public void set_size(int numberOfElements) {
    isNonVectorDetection();
    isInvalidPositionDetection(numberOfElements);

    if (numberOfElements == n_elem) {
      return;
    } else if (is_colvec()) {
      set_size(numberOfElements, 1);
    } else {
      set_size(1, numberOfElements);
    }
  }

  /**
   * Sets the size of the matrix to {@link #n_rows} rows and {@link #n_cols} columns.
   * <p>
   * Neither guarantees to reuse the values of the elements nor their positions, except the number of elements is the
   * same as before.
   * <p>
   * In this case, the values of the elements are reused and only the size changes.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   */
  public void set_size(int numberOfRows, int numberOfColumns) {
    isInvalidPositionDetection(numberOfRows);
    isInvalidPositionDetection(numberOfColumns);

    if (n_rows != numberOfRows || n_cols != numberOfColumns) {
      _matrix = new double[numberOfRows * numberOfColumns];
    }
    updateAttributes(numberOfRows, numberOfColumns);
  }

  /**
   * Sets the size of the matrix to {@link #n_rows} rows and {@link #n_cols} columns.
   * <p>
   * Elements are accessed column-wise, while the matrix is also filled column-wise.
   * <p>
   * Guarantees to reuse the values of the elements but not their positions.
   * 
   * @param numberOfRows The new number of rows
   * @param numberOfColumns The new number of columns
   */
  public void reshape(int numberOfRows, int numberOfColumns) {
    reshape(numberOfRows, numberOfColumns, 0);
  }

  /**
   * Sets the size of the matrix to {@link #n_rows} rows and {@link #n_cols} columns.
   * <p>
   * Elements can be accessed either column-wise ({@code dimension} = 0) or row-wise ({@code dimension} = 1), while the
   * matrix is always filled column-wise.
   * <p>
   * Guarantees to reuse the values of the elements but not their positions.
   * 
   * @param numberOfRows The new number of rows
   * @param numberOfColumns The new number of columns
   * @param dimension The dimension
   */
  public void reshape(int numberOfRows, int numberOfColumns, int dimension) {
    isNonBinaryParameterDetection(dimension);

    Mat temp = new Mat(_matrix);
    set_size(numberOfColumns, numberOfRows);

    if (dimension == 0) {
      System.arraycopy(temp, 0, _matrix, 0, temp.n_elem);
    } else {
      for (int n = 0; n < temp.n_elem; n++) {
        _matrix[n] = temp._matrix[n];
      }
    }
  }

  /**
   * Sets the size of the vector to {@code numberOfElements} elements.
   * <p>
   * Guarantees to reuse the values of the elements and their positions.
   * 
   * @param numberOfElements The number of elements
   * 
   * @throws UnsupportedOperationException Must only be invoked for vectors.
   */
  public void resize(int numberOfElements) throws UnsupportedOperationException {
    isNonVectorDetection();

    if (is_colvec()) {
      resize(numberOfElements, 1);
    } else {
      resize(1, numberOfElements);
    }
  }

  /**
   * Sets the size of the matrix to {@link #n_rows} rows and {@link #n_cols} columns.
   * <p>
   * Guarantees to reuse the values of the elements and their positions.
   * 
   * @param numberOfRows The new number of rows
   * @param numberOfColumns The new number of columns
   */
  public void resize(int numberOfRows, int numberOfColumns) {
    Mat temp = new Mat(_matrix);
    set_size(numberOfColumns, numberOfRows);

    int srcColumnPointer = 0;
    int destColumnPointer = 0;
    int length = Math.min(temp.n_rows, numberOfRows);
    for (int j = 0; j < n_cols; j++) {
      System.arraycopy(temp, srcColumnPointer, _matrix, destColumnPointer, length);
      srcColumnPointer += temp.n_rows;
      destColumnPointer += numberOfRows;
    }
  }

  /**
   * Fills the matrix with data from a file of filetype {@code ascii} and returns true on succes.
   * <p>
   * Resets the matrix if an error occured.
   * 
   * @param filename The filename
   * @return Whether the process was succesfully.
   * 
   * @throws IOException An I/O error occured
   */
  public boolean load(String filename) throws IOException {
    return load(filename, "auto_detect");
  }

  /**
   * Fills the matrix with data from a file of filetype {@code filetype} and returns true on succes.
   * <p>
   * Resets the matrix if an error occured.
   * 
   * @param filename The filename
   * @param filetype The filetype
   * @return Whether the process was succesfully.
   * 
   * @throws IOException An I/O error occured
   */
  public boolean load(String filename, String filetype) throws IOException {
    return load(new FileInputStream(filename), filetype);
  }

  /**
   * Fills the matrix with data from a stream of filetype {@code ascii} and returns true on succes.
   * <p>
   * Resets the matrix if an error occured.
   * 
   * @param stream The stream
   * @return Whether the process was succesfully.
   * 
   * @throws IOException An I/O error occured
   */
  public boolean load(InputStream stream) throws IOException {
    return load(stream, "auto_detect");
  }

  /**
   * Fills the matrix with data from a stream of filetype {@code filetype} and returns true on succes.
   * <p>
   * Resets the matrix if an error occured.
   * 
   * @param stream The stream
   * @param filetype The filetype
   * @return Whether the process was succesfully.
   * 
   * @throws IllegalArgumentException Only ascii and auto_detect is supported, but was {@code filetype}.
   * @throws IOException An I/O error occured
   * @throws IllegalArgumentException All columns must have the same length.
   */
  public boolean load(InputStream stream, String filetype) throws IllegalArgumentException, IOException {
    if (!filetype.equals("ascii") && !filetype.equals("auto_detect")) {
      throw new IllegalArgumentException("Only ascii and auto_detect is supported, but was " + filetype + ".");
    }

    BufferedReader reader;
    try {
      reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
    } catch(UnsupportedEncodingException exception) {
      throw new AssertionError("UTF-8 is not supported. How could this happen?");
    }

    String line = reader.readLine();
    if (line == null) {
      reset();
      return false;
    }

    int numberOfColumns = line.trim().split("\\s+").length;
    List<double[]> matrix = new ArrayList<double[]>();
    do {
      String rowString[] = line.trim().split("\\s+");

      if (rowString.length != numberOfColumns) {
        reset();
        throw new IllegalArgumentException("All columns must have the same length.");
      }

      double[] rowDouble = new double[numberOfColumns];
      for (int j = 0; j < numberOfColumns; j++) {
        if (rowString[j].equals("Inf")) {
          rowDouble[j] = Double.POSITIVE_INFINITY;
        } else if (rowString[j].equals("-Inf")) {
          rowDouble[j] = Double.NEGATIVE_INFINITY;
        } else {
          rowDouble[j] = Double.valueOf(rowString[j]);
        }
      }
      matrix.add(rowDouble);

    } while ((line = reader.readLine()) != null);
    int numberOfRow = matrix.size();

    set_size(numberOfRow, numberOfColumns);
    for (int i = 0; i < numberOfRow; i++) {
      for (int j = 0; j < numberOfColumns; j++) {
        _matrix[getElementIndex(i, j)] = matrix.get(i)[j];
      }
    }
    reader.close();

    return true;
  }

  /**
   * Resets the matrix to an emtpy one.
   */
  public void clear() {
    reset();
  }

  /**
   * Resets the matrix to an emtpy one.
   */
  public void reset() {
    _matrix = new double[]{};
    updateAttributes(0, 0);
  }

  /**
   * Sets the size of the matrix to be the same as {@code matrix}.
   * <p>
   * Neither guarantees to reuse the values of the elements nor their positions.
   * 
   * @param matrix The matrix
   */
  public void copy_size(AbstractMat matrix) {
    set_size(matrix.n_rows, matrix.n_cols);
  }

  @Override
  protected int getElementIndex(int i, int j) {
    return i + j * n_rows;
  }

  @Override
  protected int getElementIndex(int n) {
    return n;
  }

  /**
   * Updates the attributes {@link #n_rows}, {@link #n_cols} and {@link #n_elem}.
   * 
   * @param numberOfRows The number of rows
   * @param numberOfColumns The number of columns
   */
  protected void updateAttributes(int numberOfRows, int numberOfColumns) {
    n_rows = numberOfRows;
    n_cols = numberOfColumns;
    n_elem = n_rows * n_cols;
  }
}