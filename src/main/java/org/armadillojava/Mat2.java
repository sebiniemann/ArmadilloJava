/*******************************************************************************
 * Copyright 2013 Sebastian Niemann <niemann@sra.uni-hannover.de> and contributors.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 *******************************************************************************/

package org.armadillojava;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.netlib.lapack.Dgetrf;
import org.netlib.lapack.Dgetri;
import org.netlib.util.intW;

/**
 * Provides a real-valued dense matrix with interfaces similar to the Armadillo C++ Algebra Library (Armadillo) by
 * Conrad Sanderson et al..
 * <p>
 * If not stated otherwise (marked as non-canonical), the provided interfaces are identical to Armadillo (e.g. same
 * ordering of arguments, accepted values, ...). However, numeric results may slightly differ from the Armadillo C++
 * Algebra Library.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 * 
 * @see <a href="http://arma.sourceforge.net/">Armadillo C++ Algebra Library</a>
 */
public class Mat2 extends AbstractMat2 {

  /**
   * Creates an empty matrix with zero elements.
   */
  public Mat() {
      set_size(0, 0);
  }

  /**
   * Creates a column vector with the same number of elements as the provided one-dimensional array.
   * 
   * @param matrix The array
   */
  public Mat(double[] matrix) {
    if (matrix.length > 0) {
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
    if (matrix.length > 0 && matrix[0].length > 0) {
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
    copy_size(matrix);
    System.arraycopy(matrix._matrix, 0, _matrix, 0, matrix.n_elem);
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
   * Returns the inverse matrix.
   * <p>
   * Fails if the provided matrix is not invertible.
   * 
   * @return The inverse
   */
  public Mat i() throws UnsupportedOperationException {
    isNotSquareDetection();

    intW errorCode = new intW(0);
    int[] pivotIndices = new int[n_rows];
    
    Dgetrf.dgetrf(n_rows, n_cols, _matrix, 0, n_rows, pivotIndices, 0, errorCode);
    
    if(errorCode.val == 0) {
      double[] tempMatrix = new double[n_rows];
      
      Dgetri.dgetri(n_rows, _matrix, 0, n_rows, pivotIndices, 0, tempMatrix, 0, n_rows, errorCode);
    }
    
    if(errorCode.val == 0) {
      Mat inverse = new Mat(_matrix);
      inverse.set_size(n_rows, n_cols);
      return inverse;
    } else {
      throw new UnsupportedOperationException("The matrix is not invertible.");
    }
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

    if (numberOfElements != n_elem) {
      if (is_colvec()) {
        set_size(numberOfElements, 1);
      } else {
        set_size(1, numberOfElements);
      }
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

    Mat temp = new Mat(this);
    set_size(numberOfRows, numberOfColumns);

    if (dimension == 0) {
      System.arraycopy(temp._matrix, 0, _matrix, 0, Math.min(temp.n_elem, n_elem));
    } else {
      int copiedNumberOfElements = Math.min(temp.n_elem, n_elem);

      int n = 0;
      for (int i = 0; i < temp.n_rows && n < copiedNumberOfElements; i++) {
        for (int j = 0; j < temp.n_cols && n < copiedNumberOfElements; j++) {
          _matrix[n++] = temp._matrix[temp.getElementIndex(i, j)];
        }
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
    Mat temp = new Mat(this);
    set_size(numberOfRows, numberOfColumns);

    int srcColumnPointer = 0;
    int destColumnPointer = 0;
    int length = Math.min(temp.n_rows, n_rows);

    int copiedNumberOfColumns = Math.min(temp.n_cols, n_cols);
    for (int j = 0; j < copiedNumberOfColumns; j++) {
      System.arraycopy(temp._matrix, srcColumnPointer, _matrix, destColumnPointer, length);
      srcColumnPointer += temp.n_rows;
      destColumnPointer += n_rows;
    }
    
  }

  /**
   * Fills the matrix with data from a file of filetype {@code ascii} and returns true on success.
   * <p>
   * Resets the matrix if an error occurred.
   * 
   * @param filename The filename
   * @return Whether the process was successfully.
   * 
   * @throws IOException An I/O error occurred
   */
  public boolean load(String filename) throws IOException {
    return load(filename, "auto_detect");
  }

  /**
   * Fills the matrix with data from a file of filetype {@code filetype} and returns true on success.
   * <p>
   * Resets the matrix if an error occurred.
   * 
   * @param filename The filename
   * @param filetype The filetype
   * @return Whether the process was successfully.
   * 
   * @throws IOException An I/O error occurred
   */
  public boolean load(String filename, String filetype) throws IOException {
    return load(new FileInputStream(filename), filetype);
  }

  /**
   * Fills the matrix with data from a stream of filetype {@code ascii} and returns true on success.
   * <p>
   * Resets the matrix if an error occurred.
   * 
   * @param stream The stream
   * @return Whether the process was successfully.
   * 
   * @throws IOException An I/O error occurred
   */
  public boolean load(InputStream stream) throws IOException {
    return load(stream, "auto_detect");
  }

  /**
   * Fills the matrix with data from a stream of filetype {@code filetype} and returns true on success.
   * <p>
   * Resets the matrix if an error occurred.
   * 
   * @param stream The stream
   * @param filetype The filetype
   * @return Whether the process was successfully.
   * 
   * @throws IllegalArgumentException Only ascii and auto_detect is supported, but was {@code filetype}.
   * @throws IOException An I/O error occurred
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
    List<double[]> matrix = new ArrayList<>();
    do {
      String rowString[] = line.trim().split("\\s+");

      if (rowString.length != numberOfColumns) {
        reset();
        throw new IllegalArgumentException("All columns must have the same length.");
      }

      double[] rowDouble = new double[numberOfColumns];
      for (int j = 0; j < numberOfColumns; j++) {
          switch (rowString[j]) {
              case "Inf":
              case "inf":
                  rowDouble[j] = Double.POSITIVE_INFINITY;
                  break;
              case "-Inf":
              case "-inf":
                  rowDouble[j] = Double.NEGATIVE_INFINITY;
                  break;
              case "NaN":
              case "nan":
                  rowDouble[j] = Double.NaN;
                  break;
              default:
                  rowDouble[j] = Double.valueOf(rowString[j]);
                  break;
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
    _matrix = null;
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

/**
 * Provides a real-valued dense matrix with interfaces similar to the Armadillo C++ Algebra Library (Armadillo) by
 * Conrad Sanderson et al..
 * <p>
 * If not stated otherwise (marked as non-canonical), the provided interfaces are identical to Armadillo (e.g. same
 * ordering of arguments, accepted values, ...). However, numeric results may slightly differ from the Armadillo C++
 * Algebra Library.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 * 
 * @see <a href="https://github.com/SebastianNiemann/ArmadilloJava/blob/master/CONVERSION.md">syntax conversion
 *      table</a>
 */
abstract class AbstractMat2 implements Iterable<Double> {

  /**
   * The original matrix, used for sub-views
   */
  AbstractMat        _originalMatrix;

  /**
   * The internal data representation
   */
  protected double[] _data;

  /**
   * The number of rows
   */
  public int         n_rows;

  /**
   * The number of columns
   */
  public int         n_cols;

  /**
   * The number of elements (same as {@link #n_rows} * {@link #n_cols}) .
   */
  public int         n_elem;

  /**
   * The current iterator position
   */
  protected int      _iterator;

  /**
   * Converts a (row, column)-position into a <em>one-dimensional<em> position.
   * <p>
   * <b>Note:</b> Column-major-ordering is assumed: {@code at(i, j) = at(i + j * n_rows)}.
   * 
   * @param i The row position
   * @param j The column position
   */
  abstract protected int getPosition(int i, int j);

  /**
   * Converts a one-dimensional position relative to this (sub)view into a one-dimensional position based on the
   * original matrix.
   * <p>
   * Used to convert positions between a sub-view and its original matrix.
   * <p>
   * <b>Note:</b> Column-major-ordering is assumed: {@code at(i, j) = at(i + j * n_rows)}.
   * 
   * @param n The one-dimensional position relative to this (sub)view
   */
  abstract protected int getPosition(int n);

  /**
   * Returns the value of the element at the {@code i}th row and {@code j}th column.
   * 
   * @param i The row position
   * @param j The column position
   */
  abstract public double at(int i, int j);

  /**
   * Performs a unary right-hand side in-place operation on the element at the {@code i}th row and {@code j}th column.
   * 
   * @param i The row position
   * @param j The column position
   * @param operator The unary operator
   */
  public void at(int i, int j, Op operator) {
    at(getPosition(i, j), operator);
  }

  /**
   * Performs a binary right-hand side in-place operation on the element at the {@code i}th row and {@code j}th column.
   * 
   * @param i The row position
   * @param j The column position
   * @param operator The binary operator
   * @param operand The right-hand side operand
   */
  public void at(int i, int j, Op operator, double operand) {
    at(getPosition(i, j), operator, operand);
  }

  /**
   * Returns the value of the {@code n}th element.
   * <p>
   * <b>Note:</b> Column-major-ordering is assumed: {@code at(i, j) = at(i + j * n_rows)}.
   * 
   * @param n The one-dimensional position
   */
  abstract public double at(int n);

  /**
   * Performs a unary right-hand side in-place operation on the {@code n}th element.
   * <p>
   * <b>Note:</b> Column-major-ordering is assumed: {@code at(i, j) = at(i + j * n_rows)}.
   * 
   * @param n The one-dimensional position
   * @param operator The unary operator
   */
  public void at(int n, Op operator) {
    inplace(n, operator);
  }

  /**
   * Performs a binary right-hand side in-place operation on the {@code n}th element.
   * <p>
   * <b>Note:</b> Column-major-ordering is assumed: {@code at(i, j) = at(i + j * n_rows)}.
   * 
   * @param n The one-dimensional position
   * @param operator The binary operator
   * @param operand The right-hand side operand
   */
  public void at(int n, Op operator, double operand) {
    inplace(n, operator, operand);
  }

  /**
   * Returns a shallow copy of the {@code j}th column.
   * 
   * @param j The column position
   */
  protected SubView colInternal(int j) {
    return new SubView(this, 0, n_rows - 1, j, j);
  }

  /**
   * Returns a deep copy of the {@code j}th column.
   * 
   * @param j The column index
   * @return The submatrix
   */
  public Mat col(int j) {
    return new Mat(colInternal(j));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code j}th column.
   * 
   * @param j The column index
   * @param operator The operator
   */
  public void col(int j, Op operator) {
    colInternal(j).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code j}th column.
   * 
   * @param j The column index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void col(int j, Op operator, AbstractMat operand) {
    colInternal(j).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code j}th column.
   * 
   * @param j The column index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void col(int j, Op operator, double operand) {
    colInternal(j).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code j}th column and {@code a}th to {@code b}th row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param j The column index
   * @return The submatrix
   */
  protected SubView colInternal(int a, int b, int j) {
    return new SubView(this, a, b, j, j);
  }

  /**
   * Returns a deep copy of all elements in the {@code j}th column and {@code a}th to {@code b}th row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param j The column index
   * @return The submatrix
   */
  public Mat col(int a, int b, int j) {
    return new Mat(colInternal(a, b, j));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code j}th column and
   * {@code a}th to {@code b}th row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param j The column index
   * @param operator The operator
   */
  public void col(int a, int b, int j, Op operator) {
    colInternal(a, b, j).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code j}th column and
   * {@code a}th to {@code b}th row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param j The column index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void col(int a, int b, int j, Op operator, AbstractMat operand) {
    colInternal(a, b, j).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code j}th column and
   * {@code a}th to {@code b}th row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param j The column index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void col(int a, int b, int j, Op operator, double operand) {
    colInternal(a, b, j).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of the {@code i}th row.
   * 
   * @param i The row index
   * @return The submatrix
   */
  protected SubView rowInternal(int i) {
    return new SubView(this, i, i, 0, n_cols - 1);
  }

  /**
   * Returns a deep copy of the {@code i}th row.
   * 
   * @param i The row index
   * @return The submatrix
   */
  public Mat row(int i) {
    return new Mat(rowInternal(i));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code i}th row.
   * 
   * @param i The row index
   * @param operator The operator
   */
  public void row(int i, Op operator) {
    rowInternal(i).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code i}th row.
   * 
   * @param i The row index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void row(int i, Op operator, AbstractMat operand) {
    rowInternal(i).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code i}th row.
   * 
   * @param i The row index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void row(int i, Op operator, double operand) {
    rowInternal(i).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code i}th row and {@code a}th to {@code b}th column.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param i The column index
   * @return The submatrix
   */
  protected SubView rowInternal(int a, int b, int i) {
    return new SubView(this, i, i, a, b);
  }

  /**
   * Returns a deep copy of all elements in the {@code i}th row and {@code a}th to {@code b}th column.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param i The column index
   * @return The submatrix
   */
  public Mat row(int a, int b, int i) {
    return new Mat(rowInternal(a, b, i));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code i}th row and
   * {@code a}th to {@code b}th column.
   * 
   * @param a The first column index
   * @param b The last column index
   * @param i The row index
   * @param operator The operator
   */
  public void row(int a, int b, int i, Op operator) {
    rowInternal(a, b, i).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code i}th row and
   * {@code a}th to {@code b}th column.
   * 
   * @param a The first column index
   * @param b The last column index
   * @param i The row index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void row(int a, int b, int i, Op operator, AbstractMat operand) {
    rowInternal(a, b, i).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code i}th row and
   * {@code a}th to {@code b}th column.
   * 
   * @param a The first column index
   * @param b The last column index
   * @param i The row index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void row(int a, int b, int i, Op operator, double operand) {
    rowInternal(a, b, i).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code a}th to {@code b}th column.
   * 
   * @param a The first column index
   * @param b The last column index
   * @return The submatrix
   */
  protected SubView colsInternal(int a, int b) {
    return new SubView(this, 0, n_rows - 1, a, b);
  }

  /**
   * Returns a deep copy of all elements in the {@code a}th to {@code b}th column.
   * 
   * @param a The first column index
   * @param b The last column index
   * @return The submatrix
   */
  public Mat cols(int a, int b) {
    return new Mat(colsInternal(a, b));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * column.
   * 
   * @param a The first column index
   * @param b The last column index
   * @param operator The operator
   */
  public void cols(int a, int b, Op operator) {
    colsInternal(a, b).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * column.
   * 
   * @param a The first column index
   * @param b The last column index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void cols(int a, int b, Op operator, AbstractMat operand) {
    colsInternal(a, b).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * column.
   * 
   * @param a The first column index
   * @param b The last column index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void cols(int a, int b, Op operator, double operand) {
    colsInternal(a, b).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code a}th to {@code b}th row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @return The submatrix
   */
  protected SubView rowsInternal(int a, int b) {
    return new SubView(this, a, b, 0, n_cols - 1);
  }

  /**
   * Returns a deep copy of all elements in the {@code a}th to {@code b}th row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @return The submatrix
   */
  public Mat rows(int a, int b) {
    return new Mat(rowsInternal(a, b));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param operator The operator
   */
  public void rows(int a, int b, Op operator) {
    rowsInternal(a, b).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void rows(int a, int b, Op operator, AbstractMat operand) {
    rowsInternal(a, b).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * row.
   * 
   * @param a The first row index
   * @param b The last row index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void rows(int a, int b, Op operator, double operand) {
    rowsInternal(a, b).inplace(operator, operand);
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   */
  public void submat(Op operator) {
    inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(Op operator, AbstractMat operand) {
    inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(Op operator, double operand) {
    inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code ai}th to {@code bi}th row and {@code aj}th to {@code bj}th
   * column.
   * 
   * @param ai The first row index
   * @param bi The last row index
   * @param aj The first column index
   * @param bj The last column index
   * @return The submatrix
   */
  protected SubView submatInternal(int ai, int bi, int aj, int bj) {
    return new SubView(this, ai, bi, aj, bj);
  }

  /**
   * Returns a deep copy of all elements in the {@code ai}th to {@code bi}th row and {@code aj}th to {@code bj}th
   * column.
   * 
   * @param ai The first row index
   * @param bi The last row index
   * @param aj The first column index
   * @param bj The last column index
   * @return The submatrix
   */
  public Mat submat(int ai, int bi, int aj, int bj) {
    return new Mat(submatInternal(ai, bi, aj, bj));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code ai}th to {@code bi}
   * th
   * row and {@code aj}th to {@code bj}th column.
   * 
   * @param ai The first row index
   * @param bi The last row index
   * @param aj The first column index
   * @param bj The last column index
   * @param operator The operator
   */
  public void submat(int ai, int bi, int aj, int bj, Op operator) {
    submatInternal(ai, bi, aj, bj).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code ai}th to {@code bi}
   * th row and {@code aj}th to {@code bj}th column.
   * 
   * @param ai The first row index
   * @param bi The last row index
   * @param aj The first column index
   * @param bj The last column index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(int ai, int bi, int aj, int bj, Op operator, AbstractMat operand) {
    submatInternal(ai, bi, aj, bj).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code ai}th to {@code bi}
   * th row and {@code aj}th to {@code bj}th column.
   * 
   * @param ai The first row index
   * @param bi The last row index
   * @param aj The first column index
   * @param bj The last column index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(int ai, int bi, int aj, int bj, Op operator, double operand) {
    submatInternal(ai, bi, aj, bj).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements in the {@code a}th to {@code b}th position.
   * 
   * @param a The first element index
   * @param b The last element index
   * @return The submatrix
   */
  protected SubView subvecInternal(int a, int b) {
    return new SubView(this, a, b);
  }

  /**
   * Returns a deep copy of all elements in the {@code a}th to {@code b}th position.
   * 
   * @param a The first element index
   * @param b The last element index
   * @return The submatrix
   */
  public Mat subvec(int a, int b) {
    return new Mat(subvecInternal(a, b));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * position.
   * 
   * @param a The first element index
   * @param b The last element index
   * @param operator The operator
   */
  public void subvec(int a, int b, Op operator) {
    subvecInternal(a, b).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * position.
   * 
   * @param a The first element index
   * @param b The last element index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void subvec(int a, int b, Op operator, AbstractMat operand) {
    subvecInternal(a, b).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in the {@code a}th to {@code b}th
   * position.
   * 
   * @param a The first element index
   * @param b The last element index
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void subvec(int a, int b, Op operator, double operand) {
    subvecInternal(a, b).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements specified in the selection as a column vector.
   * 
   * @param selection The element indices
   * @return The elements
   */
  protected SelectView elemInternal(AbstractMat selection) {
    return new SelectView(this, selection);
  }

  /**
   * Returns a deep copy of all elements specified in the selection as a column vector.
   * 
   * @param selection The element indices
   * @return The elements
   */
  public Mat elem(AbstractMat selection) {
    return new Mat(elemInternal(selection));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The element indices
   * @param operator The operator
   */
  public void elem(AbstractMat selection, Op operator) {
    elemInternal(selection).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The element indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void elem(AbstractMat selection, Op operator, AbstractMat operand) {
    elemInternal(selection).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The element indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void elem(AbstractMat selection, Op operator, double operand) {
    elemInternal(selection).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all columns specified in the selection.
   * 
   * @param selection The column indices
   * @return The elements
   */
  protected SelectView colsInternal(AbstractMat selection) {
    return new SelectView(this, null, selection);
  }

  /**
   * Returns a deep copy of all columns specified in the selection.
   * 
   * @param selection The column indices
   * @return The elements
   */
  public Mat cols(AbstractMat selection) {
    return new Mat(colsInternal(selection));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The column indices
   * @param operator The operator
   */
  public void cols(AbstractMat selection, Op operator) {
    colsInternal(selection).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The column indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void cols(AbstractMat selection, Op operator, AbstractMat operand) {
    colsInternal(selection).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The column indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void cols(AbstractMat selection, Op operator, double operand) {
    colsInternal(selection).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all rows specified in the selection.
   * 
   * @param selection The row indices
   * @return The elements
   */
  protected SelectView rowsInternal(AbstractMat selection) {
    return new SelectView(this, selection, null);
  }

  /**
   * Returns a deep copy of all rows specified in the selection.
   * 
   * @param selection The row indices
   * @return The elements
   */
  public Mat rows(AbstractMat selection) {
    return new Mat(rowsInternal(selection));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The row indices
   * @param operator The operator
   */
  public void rows(AbstractMat selection, Op operator) {
    rows(selection).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The row indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void rows(AbstractMat selection, Op operator, AbstractMat operand) {
    rows(selection).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param selection The row indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void rows(AbstractMat selection, Op operator, double operand) {
    rows(selection).inplace(operator, operand);
  }

  /**
   * Returns a shallow copy of all elements specified in the selection.
   * 
   * @param rowSelection The row indices
   * @param columnSelection The column indices
   * @return The elements
   */
  protected SelectView submatInternal(AbstractMat rowSelection, AbstractMat columnSelection) {
    return new SelectView(this, rowSelection, columnSelection);
  }

  /**
   * Returns a deep copy of all elements specified in the selection.
   * 
   * @param rowSelection The row indices
   * @param columnSelection The column indices
   * @return The elements
   */
  public Mat submat(AbstractMat rowSelection, AbstractMat columnSelection) {
    return new Mat(submatInternal(rowSelection, columnSelection));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param rowSelection The row indices
   * @param columnSelection The column indices
   * @param operator The operator
   */
  public void submat(AbstractMat rowSelection, AbstractMat columnSelection, Op operator) {
    submatInternal(rowSelection, columnSelection).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param rowSelection The row indices
   * @param columnSelection The column indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(AbstractMat rowSelection, AbstractMat columnSelection, Op operator, AbstractMat operand) {
    submatInternal(rowSelection, columnSelection).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements specified in the selection.
   * 
   * @param rowSelection The row indices
   * @param columnSelection The column indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void submat(AbstractMat rowSelection, AbstractMat columnSelection, Op operator, double operand) {
    submatInternal(rowSelection, columnSelection).inplace(operator, operand);
  }

  /**
   * Returns a deep copy of all elements on the main diagonal of the matrix as a column vector.
   * 
   * @return The diagonal
   */
  public Mat diag() {
    return diag(0);
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements on the main diagonal.
   * 
   * @param operator The operator
   */
  public void diag(Op operator) {
    diag(0, operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements on the main diagonal.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void diag(Op operator, AbstractMat operand) {
    diag(0, operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements on the main diagonal.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void diag(Op operator, double operand) {
    diag(0, operator, operand);
  }

  /**
   * Returns a shallow copy of all elements on the {@code k}th diagonal of the matrix as a column vector.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, the main diagonal is returned
   * <li>For {@code k} > 0, the {@code k}th super-diagonal is returned.
   * <li>For {@code k} < 0, the {@code k}th sub-diagonal is returned.
   * </ul>
   * 
   * @param k The diagonal position
   * @return The diagonal
   */
  protected DiagView diagInternal(int k) {
    return new DiagView(this, k);
  }

  /**
   * Returns a deep copy of all elements on the {@code k}th diagonal of the matrix as a column vector.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, the main diagonal is returned
   * <li>For {@code k} > 0, the {@code k}th super-diagonal is returned.
   * <li>For {@code k} < 0, the {@code k}th sub-diagonal is returned.
   * </ul>
   * 
   * @param k The diagonal position
   * @return The diagonal
   */
  public Mat diag(int k) {
    return new Mat(diagInternal(k));
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements on the {@code k}th diagonal.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, the main diagonal is returned
   * <li>For {@code k} > 0, the {@code k}th super-diagonal is returned.
   * <li>For {@code k} < 0, the {@code k}th sub-diagonal is returned.
   * </ul>
   * 
   * @param k The diagonal position
   * @param operator The operator
   */
  public void diag(int k, Op operator) {
    diagInternal(k).inplace(operator);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements on the {@code k}th diagonal.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, the main diagonal is returned
   * <li>For {@code k} > 0, the {@code k}th super-diagonal is returned.
   * <li>For {@code k} < 0, the {@code k}th sub-diagonal is returned.
   * </ul>
   * 
   * @param k The diagonal position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void diag(int k, Op operator, AbstractMat operand) {
    diagInternal(k).inplace(operator, operand);
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements on the {@code k}th diagonal.
   * <p>
   * <ul>
   * <li>For {@code k} = 0, the main diagonal is returned
   * <li>For {@code k} > 0, the {@code k}th super-diagonal is returned.
   * <li>For {@code k} < 0, the {@code k}th sub-diagonal is returned.
   * </ul>
   * 
   * @param k The diagonal position
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void diag(int k, Op operator, double operand) {
    diagInternal(k).inplace(operator, operand);
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in each column.
   * 
   * @param operator The operator
   */
  public void each_col(Op operator) {
    for (int j = 0; j < n_cols; j++) {
      col(j, operator);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in each column.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_col(Op operator, AbstractMat operand) {
    for (int j = 0; j < n_cols; j++) {
      col(j, operator, operand);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in each column.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_col(Op operator, double operand) {
    for (int j = 0; j < n_cols; j++) {
      col(j, operator, operand);
    }
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on each column specified in the selection.
   * 
   * @param selection The column indices
   * @param operator The operator
   */
  public void each_col(AbstractMat selection, Op operator) {
    selection.isNonVectorDetection();
    isInvalidColumnSelectionDetection(selection);

    selection.iteratorReset();
    while (selection.iteratorHasNext()) {
      col((int) selection._matrix[selection.iteratorNext()], operator);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on each column specified in the selection.
   * 
   * @param selection The column indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_col(AbstractMat selection, Op operator, AbstractMat operand) {
    selection.isNonVectorDetection();
    isInvalidColumnSelectionDetection(selection);

    selection.iteratorReset();
    while (selection.iteratorHasNext()) {
      col((int) selection._matrix[selection.iteratorNext()], operator, operand);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on each column specified in the selection.
   * 
   * @param selection The column indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_col(AbstractMat selection, Op operator, double operand) {
    selection.isNonVectorDetection();
    isInvalidColumnSelectionDetection(selection);

    selection.iteratorReset();
    while (selection.iteratorHasNext()) {
      col((int) selection._matrix[selection.iteratorNext()], operator, operand);
    }
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on all elements in each row.
   * 
   * @param operator The operator
   */
  public void each_row(Op operator) {
    for (int i = 0; i < n_cols; i++) {
      row(i, operator);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in each row.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_row(Op operator, Mat operand) {
    for (int i = 0; i < n_rows; i++) {
      row(i, operator, operand);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on all elements in each row.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_row(Op operator, double operand) {
    for (int i = 0; i < n_rows; i++) {
      row(i, operator, operand);
    }
  }

  /**
   * Performs an element-wise unary right-hand side inplace operation on each row specified in the selection.
   * 
   * @param selection The row indices
   * @param operator The operator
   */
  public void each_row(AbstractMat selection, Op operator) {
    selection.isNonVectorDetection();
    isInvalidRowSelectionDetection(selection);

    selection.iteratorReset();
    while (selection.iteratorHasNext()) {
      row((int) selection._matrix[selection.iteratorNext()], operator);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on each row specified in the selection.
   * 
   * @param selection The row indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_row(AbstractMat selection, Op operator, AbstractMat operand) {
    selection.isNonVectorDetection();
    isInvalidRowSelectionDetection(selection);

    selection.iteratorReset();
    while (selection.iteratorHasNext()) {
      row((int) selection._matrix[selection.iteratorNext()], operator, operand);
    }
  }

  /**
   * Performs an element-wise binary right-hand side inplace operation on each row specified in the selection.
   * 
   * @param selection The row indices
   * @param operator The operator
   * @param operand The right-hand side operand
   */
  public void each_row(AbstractMat selection, Op operator, double operand) {
    selection.isNonVectorDetection();
    isInvalidRowSelectionDetection(selection);

    selection.iteratorReset();
    while (selection.iteratorHasNext()) {
      row((int) selection._matrix[selection.iteratorNext()], operator, operand);
    }
  }

  /**
   * Creates a matrix being the addition of this matrix with a right-hand side addend
   * 
   * @param operand The addend
   * @return The matrix
   */
  public Mat plus(AbstractMat operand) {
    Mat result = new Mat(this);
    result.inplace(Op.PLUS, operand);
    return result;
  }

  /**
   * Creates a matrix being the addition of this matrix with a right-hand side addend
   * 
   * @param operand The addend
   * @return The matrix
   */
  public Mat plus(double operand) {
    Mat result = new Mat(this);
    result.inplace(Op.PLUS, operand);
    return result;
  }

  /**
   * Creates a matrix being the subtration of this matrix with a right-hand side subtrahend
   * 
   * @param operand The subtrahend
   * @return The matrix
   */
  public Mat minus(AbstractMat operand) {
    Mat result = new Mat(this);
    result.inplace(Op.MINUS, operand);
    return result;
  }

  /**
   * Creates a matrix being the subtration of this matrix with a right-hand side subtrahend
   * 
   * @param operand The subtrahend
   * @return The matrix
   */
  public Mat minus(double operand) {
    Mat result = new Mat(this);
    result.inplace(Op.MINUS, operand);
    return result;
  }

  /**
   * Creates a matrix being the multiplication of this matrix with a right-hand side multiplier
   * 
   * @param operand The multiplier
   * @return The matrix
   */
  public Mat times(AbstractMat operand) {
    isNonEqualNumberOfElementsDetection(n_cols, operand.n_rows);

    Mat result = new Mat(n_rows, operand.n_cols);

    int n = 0;
    for (int j = 0; j < operand.n_cols; j++) {
      for (int i = 0; i < n_rows; i++) {
        result._matrix[n++] = Arma.dot(rowInternal(i), operand.colInternal(j));
      }
    }

    return result;
  }

  /**
   * Creates a matrix being the multiplication of this matrix with a right-hand side multiplier
   * 
   * @param operand The multiplier
   * @return The matrix
   */
  public Mat times(double operand) {
    Mat result = new Mat(this);
    result.inplace(Op.TIMES, operand);
    return result;
  }

  /**
   * Creates a matrix being the element-wise multiplication of this matrix with a right-hand side multiplier
   * 
   * @param operand The multiplier
   * @return The matrix
   */
  public Mat elemTimes(AbstractMat operand) {
    Mat result = new Mat(this);
    result.inplace(Op.ELEMTIMES, operand);
    return result;
  }

  /**
   * Creates a matrix being the element-wise multiplication of this matrix with a right-hand side multiplier
   * 
   * @param operand The multiplier
   * @return The matrix
   */
  public Mat elemTimes(double operand) {
    Mat result = new Mat(this);
    result.inplace(Op.ELEMTIMES, operand);
    return result;
  }

  /**
   * Creates a matrix being the element-wise division of this matrix with a right-hand side divisor
   * 
   * @param operand The multiplier
   * @return The matrix
   */
  public Mat elemDivide(AbstractMat operand) {
    Mat result = new Mat(this);
    result.inplace(Op.ELEMDIVIDE, operand);
    return result;
  }

  /**
   * Creates a matrix being the element-wise division of this matrix with a right-hand side divisor
   * 
   * @param operand The multiplier
   * @return The matrix
   */
  public Mat elemDivide(double operand) {
    Mat result = new Mat(this);
    result.inplace(Op.ELEMDIVIDE, operand);
    return result;
  }

  /**
   * Creates a matrix being the element-wise negation of this matrix
   * 
   * @return The matrix
   */
  public Mat negate() {
    Mat result = new Mat(n_rows, n_cols);

    iteratorReset();
    for (int n = 0; n < n_elem; n++) {
      result._matrix[n] = -_matrix[iteratorNext()];
    }

    return result;
  }

  /**
   * Returns a reference to the internal data representation of the matrix.
   * 
   * @return The reference
   */
  public double[] memptr() {
    return _matrix;
  }

  /**
   * Sets all elements to 0.
   */
  public void zeros() {
    fill(0);
  }

  /**
   * Sets all elements to 1.
   */
  public void ones() {
    fill(1);
  }

  /**
   * Sets all elements along the main diagonal to 1 and the other elements to 0.
   */
  public void eye() {
    int length = Math.min(n_rows, n_cols);
    for (int i = 0; i < length; i++) {
      _matrix[getPosition(i, i)] = 1;
    }
  }

  /**
   * Sets each element to a pseudorandom value drawn from the standard uniform distribution on the left-closed and
   * right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   * 
   * @param rng The pseudorandom generator
   */
  public void randu(Random rng) {
    iteratorReset();
    while (iteratorHasNext()) {
      _matrix[iteratorNext()] = rng.nextDouble();
    }
  }

  /**
   * Sets each element to a pseudorandom value drawn from the standard normal distribution with mean 0.0 and standard
   * deviation 1.0.
   * 
   * @param rng The pseudorandom generator
   */
  public void randn(Random rng) {
    iteratorReset();
    while (iteratorHasNext()) {
      _matrix[iteratorNext()] = rng.nextGaussian();
    }
  }

  /**
   * Replaces the value of each element with the provided one.
   * 
   * @param value The value
   */
  public void fill(double value) {
    iteratorReset();
    while (iteratorHasNext()) {
      _matrix[iteratorNext()] = value;
    }
  }

  /**
   * Swaps the elements of this matrix with another one.
   * 
   * @param matrix The matrix
   */
  public void swap(AbstractMat matrix) {
    isNonEqualNumberOfElementsDetection(n_rows, matrix.n_rows);
    isNonEqualNumberOfElementsDetection(n_cols, matrix.n_cols);

    submat(Op.EQUAL, matrix);
  }

  /**
   * Swaps the {@code j1}th column with the {@code j2}th.
   * 
   * @param j1 The first column
   * @param j2 The second column
   */
  public void swap_cols(int j1, int j2) {
    isColumnOutOfBoundsDetection(j1);
    isColumnOutOfBoundsDetection(j2);

    Mat temp = col(j2);
    col(j2, Op.EQUAL, colInternal(j1));
    col(j1, Op.EQUAL, temp);
  }

  /**
   * Swaps the {@code i1}th row with the {@code i2}th.
   * 
   * @param i1 The first row
   * @param i2 The second row
   */
  public void swap_rows(int i1, int i2) {
    isRowOutOfBoundsDetection(i1);
    isRowOutOfBoundsDetection(i2);

    Mat temp = row(i2);
    row(i2, Op.EQUAL, rowInternal(i1));
    row(i1, Op.EQUAL, temp);
  }

  /**
   * Returns true if the matrix is empty and false otherwise.
   * 
   * @return Whether the matrix is empty
   */
  public boolean is_empty() {
    return (n_elem == 0);
  }

  /**
   * Returns true if the matrix contains only finite values and false otherwise.
   * 
   * @return Whether the matrix is finite
   */
  public boolean is_finite() {

    iteratorReset();
    while (iteratorHasNext()) {
      double element = _matrix[iteratorNext()];
      if (Double.isInfinite(element) || Double.isNaN(element)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns true if the matrix is square and false otherwise.
   * 
   * @return Whether the matrix is sqaure
   */
  public boolean is_square() {
    return (n_rows == n_cols);
  }

  /**
   * Returns true if the matrix is a vector and false otherwise.
   * 
   * @return Whether the matrix is vector
   */
  public boolean is_vec() {
    return is_colvec() || is_rowvec();
  }

  /**
   * Returns true if the matrix is a column vector and false otherwise.
   * 
   * @return Whether the matrix is column vector
   */
  public boolean is_colvec() {
    return (n_cols == 1);
  }

  /**
   * Returns true if the matrix is a row vector and false otherwise.
   * 
   * @return Whether the matrix is row vector
   */
  public boolean is_rowvec() {
    return (n_rows == 1);
  }

  /**
   * Returns true if the element index is within the boundary
   * 
   * @param n The element index
   * @return Whether the element index is within the boundary
   */
  public boolean in_range(int n) {
    return (n > -1 && n < n_elem);
  }

  /**
   * Returns true if the span is within the boundary
   * 
   * @param span The span
   * @return Whether the span is within the boundary
   */
  public boolean in_range(Span span) {
    return (span.isEntireRange() || (span.getFirst() > -1 && span.getLast() < n_elem));
  }

  /**
   * Returns true if the row index and column index are within the boundary
   * 
   * @param i The row index
   * @param j The column index
   * @return Whether the row index and column index are within the boundary
   */
  public boolean in_range(int i, int j) {
    return (i > -1 && j > -1 && i < n_rows && j < n_cols);
  }

  /**
   * Returns true if the row span and column span are within the boundary
   * 
   * @param spanI The row span
   * @param spanJ The column span
   * @return Whether the row span and column span are within the boundary
   */
  public boolean in_range(Span spanI, Span spanJ) {
    return ((spanI.isEntireRange() || (spanI.getFirst() > -1 && spanI.getLast() < n_rows)) && (spanJ.isEntireRange() || (spanJ.getFirst() > -1 && spanJ.getLast() < n_cols)));
  }

  /**
   * Returns the smallest value of all elements.
   * 
   * @return The minimum
   */
  public double min() {
    isEmptyDetection();

    iteratorReset();
    double minimum = _matrix[iteratorNext()];
    while (iteratorHasNext()) {
      minimum = Math.min(minimum, _matrix[iteratorNext()]);
    }

    return minimum;
  }

  /**
   * Returns the smallest value of all elements and stores its element index in {@code n}.
   * <p>
   * Note: The position must be of the mutable type int[].
   * 
   * @param n The element index
   * @return The minimum
   */
  public double min(int[] n) {
    isEmptyDetection();

    iteratorReset();
    double minimum = _matrix[iteratorNext()];
    while (iteratorHasNext()) {
      int nn = iteratorNext();
      double element = _matrix[nn];

      if (element < minimum) {
        minimum = element;
        n[0] = nn;
      }
    }

    return minimum;
  }

  /**
   * Returns the smallest value of all elements and stores its row index in {@code j} and column index in {@code j}.
   * <p>
   * Note: The positions must be of the mutable type int[].
   * 
   * @param i The row index
   * @param j The column index
   * @return The minimum
   */
  public double min(int[] i, int[] j) {
    isEmptyDetection();

    iteratorReset();
    double minimum = _matrix[iteratorNext()];
    while (iteratorHasNext()) {
      int n = iteratorNext();
      double element = _matrix[n];

      if (element < minimum) {
        minimum = element;
        j[0] = n / n_rows;
        i[0] = n - (j[0] * n_rows);
      }
    }

    return minimum;
  }

  /**
   * Returns the largest value of all elements.
   * 
   * @return The maximum
   */
  public double max() {
    isEmptyDetection();

    iteratorReset();
    double maximum = _matrix[iteratorNext()];
    while (iteratorHasNext()) {
      maximum = Math.max(maximum, _matrix[iteratorNext()]);
    }

    return maximum;
  }

  /**
   * Returns the largest value of all elements and stores its element index in {@code n}.
   * <p>
   * Note: The position must be of the mutable type int[].
   * 
   * @param n The element index
   * @return The maximum
   */
  public double max(int[] n) {
    isEmptyDetection();

    iteratorReset();
    double maximum = _matrix[iteratorNext()];
    while (iteratorHasNext()) {
      int nn = iteratorNext();
      double element = _matrix[nn];

      if (element > maximum) {
        maximum = element;
        n[0] = nn;
      }
    }

    return maximum;
  }

  /**
   * Returns the largest value of all elements and stores its row index in {@code j} and column index in {@code j} .
   * <p>
   * Note: The positions must be of the mutable type int[].
   * 
   * @param i The row index
   * @param j The column index
   * @return The maximum
   */
  public double max(int[] i, int[] j) {
    isEmptyDetection();

    iteratorReset();
    double maximum = _matrix[iteratorNext()];
    while (iteratorHasNext()) {
      int n = iteratorNext();
      double element = _matrix[n];

      if (element > maximum) {
        maximum = element;
        j[0] = n / n_rows;
        i[0] = n - (j[0] * n_rows);
      }
    }

    return maximum;
  }

  /**
   * Prints the matrix to System.out.
   */
  public void print() {
    print(System.out);
  }

  /**
   * Prints the matrix to System.out, with an additional header.
   * 
   * @param header The header
   */
  public void print(String header) {
    print(System.out, header);
  }

  /**
   * Prints the matrix to a Stream.
   * 
   * @param stream The stream
   */
  public void print(OutputStream stream) {
    print(stream, "");
  }

  /**
   * Prints the matrix to a Stream, with an additional header.
   * 
   * @param stream The stream
   * @param header The header
   */
  public void print(OutputStream stream, String header) {

    PrintWriter writer;
    try {
      writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(stream, "UTF-8")));
    } catch(UnsupportedEncodingException exception) {
      throw new AssertionError("UTF-8 is not supported. How could this happen?");
    }

    if (!header.isEmpty()) {
      writer.println(header);
    }
    writer.println(this);
  }

  /**
   * Prints the matrix to System.out.
   */
  public void raw_print() {
    print();
  }

  /**
   * Prints the matrix to System.out, with an additional header.
   * 
   * @param header The header
   */
  public void raw_print(String header) {
    print(header);
  }

  /**
   * Prints the matrix to a Stream.
   * 
   * @param stream The stream
   */
  public void raw_print(OutputStream stream) {
    print(stream);
  }

  /**
   * Prints the matrix to a Stream, with an additional header.
   * 
   * @param stream The stream
   * @param header The header
   */
  public void raw_print(OutputStream stream, String header) {
    print(stream, header);
  }

  /**
   * Stores the element into a file with filetype {@code filetype} and returns true on success.
   * 
   * @param filename The filename
   * @return Whether the process was succesfully.
   * 
   * @throws FileNotFoundException File not found.
   */
  public boolean save(String filename) throws FileNotFoundException {
    return save(new FileOutputStream(filename, false));
  }

  /**
   * Stores the element into a stream with filetype {@code filetype} and returns true on success.
   * 
   * @param stream The stream
   * @return Whether the process was succesfully.
   */
  public boolean save(OutputStream stream) {
    PrintWriter writer;
    try {
      writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(stream, "UTF-8")));
    } catch(UnsupportedEncodingException exception) {
      throw new AssertionError("UTF-8 is not supported. How could this happen?");
    }

    for (int i = 0; i < n_rows; i++)
    {
      writer.print(" ");

      for (int j = 0; j < n_cols; j++) {
        double value = _matrix[getPosition(i, j)];
        if (Double.isInfinite(value)) {
          String sign = "";
          if (value < 0) {
            sign = "-";
          }
          writer.format("%30s", sign + "Inf");
        } else {
          writer.format(Locale.ENGLISH, "%1$ 30.16e", value);
        }
      }

      writer.println();
    }

    writer.close();

    return true;
  }

  /**
   * Returns true if the matrix is empty and false otherwise.
   * 
   * @return Whether the matrix is empty
   */
  public boolean empty() {
    return is_empty();
  }

  /**
   * Returns the number of elements.
   * 
   * @return The number of elements
   */
  public int size() {
    return n_elem;
  }

  /**
   * Returns the transpose of the matrix.
   * 
   * @return The transpose
   */
  public Mat t() {
    Mat transpose = new Mat(n_cols, n_rows);
    int n = 0;
    for (int i = 0; i < n_rows; i++) {
      for (int j = 0; j < n_cols; j++) {
        transpose._matrix[n++] = _matrix[getPosition(i, j)];
      }
    }

    return transpose;
  }

  /**
   * Resets the internal position counter
   */
  protected void iteratorReset() {
    _iterator = 0;
  }

  /**
   * Returns true if iterator position has yet to reach the last position.
   */
  protected final boolean iteratorHasNext() {
    return (_iterator < n_elem);
  }

  /**
   * Returns the next iterator position.
   */
  protected int iteratorNext() {
    return _iterator++;
  }

  /**
   * Performs a element-wise unary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   * 
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  protected void inplace(Op operator) throws IllegalArgumentException, UnsupportedOperationException {
    iteratorReset();
    while (iteratorHasNext()) {
      inplace(iteratorNext(), operator);
    }
  }

  /**
   * Performs a element-wise unary right-hand side inplace operation on the {@code n}th element.
   * 
   * @param n The position
   * @param operator The operator
   * 
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only unary arithmetic operators are supported.
   */
  protected void inplace(int n, Op operator) throws IllegalArgumentException, UnsupportedOperationException {
    double operand = _matrix[n];

    if (Double.isNaN(operand)) {
      throw new IllegalArgumentException("NaN is not a valid operand.");
    }

    double result;
    switch (operator) {
      case NEGATE:
        result = -_matrix[n];
        break;
      case INCREMENT:
        result = _matrix[n]++;
        break;
      case DECREMENT:
        result = _matrix[n]--;
        break;
      default:
        throw new UnsupportedOperationException("Only unary arithmetic operators are supported.");
    }

    isUnderflowOverflowDetection(operand, operator, result);
  }

  /**
   * Performs a element-wise binary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  protected void inplace(Op operator, AbstractMat operand) throws IllegalArgumentException, UnsupportedOperationException {
    isNonEqualNumberOfElementsDetection(n_elem, operand.n_elem);

    iteratorReset();
    operand.iteratorReset();
    while (iteratorHasNext()) {
      inplace(iteratorNext(), operator, operand._matrix[operand.iteratorNext()]);
    }
  }

  /**
   * Performs a element-wise binary right-hand side inplace operation on all elements.
   * 
   * @param operator The operator
   * @param operand The right-hand side operand
   * 
   * @throws IllegalArgumentException NaN is not a valid operand.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  protected void inplace(Op operator, double operand) throws IllegalArgumentException, UnsupportedOperationException {
    iteratorReset();
    while (iteratorHasNext()) {
      inplace(iteratorNext(), operator, operand);
    }
  }

  /**
   * Performs a element-wise binary right-hand side inplace operation on the {@code n}th element.
   * 
   * @param n The position
   * @param operator The operator
   * @param rightHandSideOperand The right-hand side operand
   * 
   * @throws IllegalArgumentException NaN is not a valid operand, expect the operator equals Op.EQUAL.
   * @throws ArithmeticException Division by zero.
   * @throws ArithmeticException Division by infinity.
   * @throws UnsupportedOperationException Only binary arithmetic operators and equality are supported.
   */
  protected void inplace(int n, Op operator, double rightHandSideOperand) throws IllegalArgumentException, UnsupportedOperationException {
    double leftHandSideOperand = _matrix[n];

    if (!operator.equals(Op.EQUAL) && (Double.isNaN(leftHandSideOperand) || Double.isNaN(rightHandSideOperand))) {
      throw new IllegalArgumentException("NaN is not a valid operand, expect the operator equals Op.EQUAL.");
    }

    double result;
    switch (operator) {
      case EQUAL:
        result = _matrix[n] = rightHandSideOperand;
        break;
      case PLUS:
        result = _matrix[n] += rightHandSideOperand;
        break;
      case MINUS:
        result = _matrix[n] -= rightHandSideOperand;
        break;
      case TIMES:
      case ELEMTIMES:
        result = _matrix[n] *= rightHandSideOperand;
        break;
      case ELEMDIVIDE:
        if (rightHandSideOperand == 0) {
          throw new ArithmeticException("Division by zero.");
        }

        if (Double.isInfinite(rightHandSideOperand)) {
          throw new ArithmeticException("Division by infinity.");
        }

        result = _matrix[n] /= rightHandSideOperand;
        break;
      default:
        throw new UnsupportedOperationException("Only binary arithmetic operators and equality are supported.");
    }

    isUnderflowOverflowDetection(leftHandSideOperand, operator, rightHandSideOperand, result);
  }

  /**
   * Detects if the result of an unary operation was affected by an overflow and throws an exception if so.
   * <p>
   * Note: The supported unary operators cannot result in an underflow.
   * 
   * @param a The left-hand side operand
   * @param operator The operator
   * @param result The calculated result
   * 
   * @throws ArithmeticException Overflow detected. {@code a operator} resulted in {@code result}.
   */
  protected static void isUnderflowOverflowDetection(double a, Op operator, double result) throws ArithmeticException {
    if (!Double.isInfinite(a) && Double.isInfinite(result)) {
      throw new ArithmeticException("Overflow detected. " + a + " " + operator + " resulted in " + result + ".");
    }
  }

  /**
   * Detects if the result of an binary operation was affected by an underflow or overflow and throws an exception if
   * so.
   * <p>
   * The overflow detection is only active if the operator is one of {@link Op#TIMES}, {@link Op#ELEMTIMES} or
   * {@link Op#ELEMDIVIDE}.
   * 
   * @param a The left-hand side operand
   * @param operator The operator
   * @param b The right-hand side operand
   * @param result The calculated result
   * 
   * @throws ArithmeticException Underflow detected. {@code a operator b} resulted in {@code result}.
   * @throws ArithmeticException Overflow detected. {@code a operator b} resulted in {@code result}.
   */
  protected static void isUnderflowOverflowDetection(double a, Op operator, double b, double result) throws ArithmeticException {
    if (operator.equals(Op.TIMES) || operator.equals(Op.ELEMTIMES) || operator.equals(Op.ELEMDIVIDE)) {
      if (a != 0 && b != 0 && result == 0) {
        throw new ArithmeticException("Underflow detected. " + a + " " + operator + " " + b + " resulted in " + result + ".");
      }
    }

    if (!Double.isInfinite(a) && !Double.isInfinite(b) && Double.isInfinite(result)) {
      throw new ArithmeticException("Overflow detected. " + a + " " + operator + " " + b + " resulted in " + result + ".");
    }
  }

  /**
   * Detects if the matrix is not a vector and throws an exception if so.
   * 
   * @param first The first amount
   * @param second The second amount
   * 
   * @throws IllegalArgumentException The numbers of elements must match, but were {@code first} and {@code second}.
   */
  protected static void isNonEqualNumberOfElementsDetection(int first, int second) throws IllegalArgumentException {
    if (first != second) {
      throw new IllegalArgumentException("The numbers of elements must match, but were " + first + " and " + second + ".");
    }
  }

  /**
   * Detects if the matrix is empty and throws an exception if so.
   * 
   * @throws UnsupportedOperationException The matrix must have at least one element.
   */
  protected void isEmptyDetection() throws UnsupportedOperationException {
    if (n_elem < 1) {
      throw new UnsupportedOperationException("The matrix must have at least one element.");
    }
  }

  /**
   * Detects if the matrix is ill-conditioned and throws an exception if so.
   * 
   * @throws UnsupportedOperationException The matrix is ill-conditioned: {@code conditionNumber}.
   */
  protected void isIllConditionedDectetion() {
    // double conditionNumber = NormOps.normP(AbstractMat.convertMatToEJMLMat(this), 2);
    //
    // if (conditionNumber >= 1 / Math.max(n_cols, n_rows) * Datum.eps) {
    // throw new ArithmeticException("The matrix is ill-conditioned: " + conditionNumber);
    // }
  }

  /**
   * Detects if the matrix is not a vector and throws an exception if so.
   * 
   * @throws UnsupportedOperationException The matrix must be a vector, but was a ({@link #n_rows}, {@link #n_cols}
   *           )-matrix.
   */
  protected void isNonVectorDetection() throws UnsupportedOperationException {
    if (!is_vec()) {
      throw new UnsupportedOperationException("The matrix must be a vector, but was a (" + n_rows + ", " + n_cols + ")-matrix.");
    }
  }

  /**
   * Detects if the matrix is a vector and throws an exception if so.
   * 
   * @throws UnsupportedOperationException The matrix must be a non-vector, but was a ({@link #n_rows}, {@link #n_cols}
   *           )-vector.
   */
  protected void isVectorDetection() throws UnsupportedOperationException {
    if (n_rows > 1 && n_cols > 1 && is_vec()) {
      throw new UnsupportedOperationException("The matrix must be a non-vector, but was a (" + n_rows + ", " + n_cols + ")-vector.");
    }
  }

  /**
   * Detects if the position is negative or one of NaN or +/-infinity and throws an exception if so.
   * 
   * @param n The position
   * 
   * @throws IllegalArgumentException The position must be non-negative.
   * @throws IllegalArgumentException NaN and +/-infinity are not valid positions value.
   */
  protected static void isInvalidPositionDetection(int n) throws IllegalArgumentException {
    if (n < 0) {
      throw new IllegalArgumentException("The position must be non-negative.");
    }

    if (Double.isInfinite(n) || Double.isNaN(n)) {
      throw new IllegalArgumentException("NaN and +/-infinity are not valid positions value.");
    }
  }

  /**
   * Detects if any position is invalid or the first position is not less than equal the last one and throws an
   * exception if so.
   * 
   * @param a The first position
   * @param b The last position
   * 
   * @throws IllegalArgumentException The first position needs to be less than equal the last, but were {@code a} and
   *           {@code b}.
   */
  protected static void isInvalidRangeDetection(int a, int b) throws IllegalArgumentException {
    if (a > b) {
      throw new IllegalArgumentException("The first position needs to be less than equal the last, but were " + a + " and " + b + ".");
    }

    isInvalidPositionDetection(a);
    isInvalidPositionDetection(b);
  }

  /**
   * Detects if the element index is invalid or out of bound and throws an exception if so.
   * 
   * @param n The element index
   * 
   * @throws ArrayIndexOutOfBoundsException The element index range is out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}) but the element index range was ({@code n}, {@code n}).
   */
  protected void isElementOutOfBoundsDetection(int n) throws ArrayIndexOutOfBoundsException {
    isElementRangeOutOfBoundsDetection(n, n);
  }

  /**
   * Detects if the element index range is invalid or out of bound and throws an exception if so.
   * 
   * @param a The first element index
   * @param b The last element index
   * 
   * @throws ArrayIndexOutOfBoundsException The element index range is out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}) but the element index range was ({@code a}, {@code b}).
   */
  protected void isElementRangeOutOfBoundsDetection(int a, int b) throws ArrayIndexOutOfBoundsException {
    isInvalidRangeDetection(a, b);

    if (a < 0 || b > n_elem) {
      throw new ArrayIndexOutOfBoundsException("The element index range is out of bound. The matrix is of size (" + n_rows + ", " + n_cols + ") but the element index range was (" + a + ", " + b + ")");
    }
  }

  /**
   * Detects if a column index is invalid or out of bound and throws an exception if so.
   * 
   * @param j The column index
   * 
   * @throws ArrayIndexOutOfBoundsException The column index range is out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}) but the column index range was ({@code j}, {@code j}).
   */
  protected void isColumnOutOfBoundsDetection(int j) throws ArrayIndexOutOfBoundsException {
    isColumnRangeOutOfBoundsDetection(j, j);
  }

  /**
   * Detects if a column index range is invalid or out of bound and throws an exception if so.
   * 
   * @param a The first row index
   * @param b The last row index
   * 
   * @throws ArrayIndexOutOfBoundsException The column index range is out of bound. The matrix is of size (
   *           {@link #n_rows}, {@link #n_cols}) but the column index range was ({@code a}, {@code b}).
   */
  protected void isColumnRangeOutOfBoundsDetection(int a, int b) throws ArrayIndexOutOfBoundsException {
    isInvalidRangeDetection(a, b);

    if (a < 0 || b >= n_cols) {
      throw new ArrayIndexOutOfBoundsException("The column index range is out of bound. The matrix is of size (" + n_rows + ", " + n_cols + ") but the column index range was (" + a + ", " + b + ")");
    }
  }

  /**
   * Detects if a row index is invalid or out of bound and throws an exception if so.
   * 
   * @param i The row index
   * 
   * @throws ArrayIndexOutOfBoundsException The row index range is out of bound. The matrix is of size ( {@link #n_rows}
   *           , {@link #n_cols}) but the row index range was ({@code i}, {@code i}).
   */
  protected void isRowOutOfBoundsDetection(int i) throws ArrayIndexOutOfBoundsException {
    isRowRangeOutOfBoundsDetection(i, i);
  }

  /**
   * Detects if a row index range is out of bound and throws an exception if so.
   * 
   * @param a The first row index
   * @param b The last row index
   * 
   * @throws ArrayIndexOutOfBoundsException The row index range is out of bound. The matrix is of size ( {@link #n_rows}
   *           , {@link #n_cols}) but the row index range was ({@code a}, {@code b}).
   */
  protected void isRowRangeOutOfBoundsDetection(int a, int b) throws ArrayIndexOutOfBoundsException {
    isInvalidRangeDetection(a, b);

    if (a < 0 || b >= n_rows) {
      throw new ArrayIndexOutOfBoundsException("The row index range is out of bound. The matrix is of size (" + n_rows + ", " + n_cols + ") but the row index range was (" + a + ", " + b + ")");
    }
  }

  /**
   * Detects if any element index is invalid, out of bound or not an integer and throws an exception if so.
   * 
   * @param selection The column indices
   */
  protected void isInvalidElementSelectionDetection(AbstractMat selection) {
    for (int n = 0; n < selection.n_elem; n++) {
      double index = selection.at(n);

      isNonIntergerPositionDetection(index);
      isElementOutOfBoundsDetection((int) index);
    }
  }

  /**
   * Detects if any column index is invalid, out of bound or not an integer and throws an exception if so.
   * 
   * @param selection The column indices
   */
  protected void isInvalidColumnSelectionDetection(AbstractMat selection) {
    for (int n = 0; n < selection.n_elem; n++) {
      double index = selection.at(n);

      isNonIntergerPositionDetection(index);
      isColumnOutOfBoundsDetection((int) index);
    }
  }

  /**
   * Detects if any row index is invalid, out of bound or not an integer and throws an exception if so.
   * 
   * @param selection The row indices
   */
  protected void isInvalidRowSelectionDetection(AbstractMat selection) {
    for (int n = 0; n < selection.n_elem; n++) {
      double index = selection.at(n);

      isNonIntergerPositionDetection(index);
      isRowOutOfBoundsDetection((int) index);
    }
  }

  /**
   * Detects if a position is not an integer and throws an exception if so.
   * 
   * @param position The position
   * 
   * @throws IllegalArgumentException The position must be an integer, but was not: {@code rowPositionDouble}.
   */
  protected static void isNonIntergerPositionDetection(double position) throws IllegalArgumentException {
    int positionInt = (int) position;

    // Will also fail if the value is negative, which is also not allowed.
    if (position != positionInt) {
      throw new IllegalArgumentException("Each position must be an integer, but at least one was not: " + position + ".");
    }
  }

  /**
   * Detects if the matrix is not square and throws an exception if so.
   * 
   * @throws UnsupportedOperationException The matrix must be square, but was a ({@link #n_rows}, {@link #n_cols}
   *           )-matrix.
   */
  protected void isNotSquareDetection() throws UnsupportedOperationException {
    if (!is_square()) {
      throw new IllegalArgumentException("The matrix must be square, but was a (" + n_rows + ", " + n_cols + ")-matrix.");
    }
  }

  /**
   * Detects if the matrix is not symmetric and throws an exception if so.
   * 
   * @throws UnsupportedOperationException The matrix must be symmetric.
   */
  protected void isNotSymmetricDetection() {
    int n = 0;
    for (int j = 0; j < n_cols; j++) {
      for (int i = 0; i < n_rows; i++) {
        if (i != j && at(n) != at(j, i)) {
          throw new IllegalArgumentException("The matrix must be symmetric.");
        }
        n++;
      }
    }
  }

  /**
   * Detects if a parameter value is neither 0 nor 1 and throws an exception if so.
   * 
   * @param value The parameter value
   * 
   * @throws IllegalArgumentException The parameter value must be either 0 or 1, but was {@code value}.
   */
  protected static void isNonBinaryParameterDetection(int value) throws IllegalArgumentException {
    if (value < 0 || value > 1) {
      throw new IllegalArgumentException("The parameter value must be either 0 or 1, but was " + value + ".");
    }
  }

  @Override
  public String toString() {
    String output = "(" + n_rows + ", " + n_cols + ")-matrix: [\n";
    for (int i = 0; i < n_rows; i++)
    {
      output += " ";

      for (int j = 0; j < n_cols; j++) {
        double value = _matrix[getPosition(i, j)];
        if (Double.isInfinite(value)) {
          String sign = "";
          if (value < 0) {
            sign = "-";
          }
          output += String.format("%5s", sign + "Inf");
        } else {
          output += String.format(Locale.ENGLISH, "%1$ 10.5f", value);
        }
      }

      output += "\n";
    }
    output += "]";

    return output;
  }

  @Override
  public Iterator<Double> iterator() {
    class MatIterator implements Iterator<Double> {

      /**
       * The matrix
       */
      private AbstractMat _matrix;

      /**
       * Initialises the iterator.
       * 
       * @param matrix The matrix
       */
      public MatIterator(AbstractMat matrix) {
        _matrix = new Mat(matrix);
        _matrix.iteratorReset();
      }

      @Override
      public boolean hasNext() {
        return _matrix.iteratorHasNext();
      }

      @Override
      public Double next() {
        return _matrix._matrix[_matrix.iteratorNext()];
      }

      @Override
      public void remove() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Removal is not supported.");
      }
    }

    return new MatIterator(this);
  }
}
