/*******************************************************************************
 * Copyright 2013-2014 Sebastian Niemann <niemann@sra.uni-hannover.de>.
 * 
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://opensource.org/licenses/MIT
 * 
 * Developers:
 * Sebastian Niemann - Lead developer
 * Daniel Kiechle - Unit testing
 ******************************************************************************/
package org.armadillojava;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

abstract class AbstractMat {

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
   * The number of elements (same as {@code n_rows} * {@code n_cols}) .
   */
  public int         n_elem;

  /**
   * Returns the value of the {@code n}th element.
   * <p>
   * <b>Note:</b> Index checking is not enforced. However, the JVM should throw IndexOutOfBoundsException exceptions
   * upon errors.
   * 
   * @param n The position
   */
  public double at(final int n) {
    return _data[n];
  }

  /**
   * Performs an in-place unary operation on the {@code n}th element.
   * <p>
   * <b>Note:</b> Index checking is not enforced. However, the JVM should throw IndexOutOfBoundsException exceptions
   * upon errors.
   * 
   * @param n The position
   * @param unary_operator The unary operator
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code unary_operator}).
   */
  public void at(final int n, final Op unary_operator) throws UnsupportedOperationException {
    switch (unary_operator) {
      case INCREMENT:
        _data[n]++;
        break;
      case DECREMENT:
        _data[n]--;
        break;
      default:
        throw new UnsupportedOperationException("Unexpected operator (" + unary_operator + ").");
    }
  }

  /**
   * Performs an in-place binary operation on the {@code n}th element with the specified right-hand side operand.
   * <p>
   * <b>Note:</b> Index checking is not enforced. However, the JVM should throw IndexOutOfBoundsException exceptions
   * upon errors.
   * 
   * @param n The position
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void at(final int n, final Op binary_operator, final double operand) throws UnsupportedOperationException {
    switch (binary_operator) {
      case EQUAL:
        _data[n] = operand;
        break;
      case PLUS:
        _data[n] += operand;
        break;
      case MINUS:
        _data[n] -= operand;
        break;
      case TIMES:
        _data[n] *= operand;
        break;
      case DIVIDE:
        _data[n] /= operand;
        break;
      default:
        throw new UnsupportedOperationException("Unexpected operator (" + binary_operator + ").");
    }
  }

  /**
   * Returns {@code true} if the position is not out of bounds.
   * 
   * @param n The position
   */
  public boolean in_range(final int n) {
    return (n > -1 && n < n_elem);
  }

  /**
   * Returns {@code true} if neither position within the span is out of bounds.
   * 
   * @param span The span
   */
  public boolean in_range(final Span span) {
    /*
     * The parameter "span" was already validated during its instantiation.
     */
    return (span._isEntireRange || (span._first > -1 && span._last < n_elem));
  }

  /**
   * Returns {@code true} if neither the row nor the column is out of bound.
   * 
   * @param row The row
   * @param col The column
   */
  public boolean in_range(final int row, final int col) {
    return ((row > -1 && row < n_rows) && (col > -1 && col < n_cols));
  }

  /**
   * Returns {@code true} if neither position within the row and column span is out of bounds.
   * 
   * @param row_span The row span
   * @param col_span The column span
   */
  public boolean in_range(final Span row_span, final Span col_span) {
    /*
     * The parameters "row_span" and "col_span" were already validated during their instantiation.
     */
    return ((row_span._isEntireRange || (row_span._first > -1 && row_span._last < n_rows)) && (col_span._isEntireRange || (col_span._first > -1 && col_span._last < n_cols)));
  }

  /**
   * Returns {@code true} if neither position from {@code first_row} to {@code first_row + size.n_rows - 1} and
   * {@code first_col} to {@code first_col + size.n_cols - 1} is out of bounds.
   * 
   * @param first_row The first row
   * @param first_col The first column
   * @param size The size of a matrix or field
   */
  public boolean in_range(final int first_row, final int first_col, final Size size) {
    /*
     * The parameter "size" was already validated during its instantiation.
     */
    return ((first_row > -1 && first_row + size.n_rows - 1 < n_rows) && (first_col > -1 && first_col + size.n_cols - 1 < n_cols));
  }

  /**
   * Returns {@code true} if the matrix has no elements.
   */
  public boolean is_empty() {
    return (n_elem == 0);
  }

  /**
   * Returns {@code true} if the matrix contains only finite values.
   */
  public boolean is_finite() {
    for (int n = 0; n < n_elem; n++) {
      double value = _data[n];

      if (Double.isInfinite(value) || Double.isNaN(value)) {
        return false;
      }
    }

    return true;
  }

  /**
   * Returns a reference to the memory internally representing the matrix.
   */
  public double[] memptr() {
    return _data;
  }

  /**
   * Returns the smallest value within the matrix.
   * 
   * @throws RuntimeException The ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public double min() throws RuntimeException {
    if (is_empty()) {
      throw new RuntimeException("The (" + n_rows + ", " + n_cols + ")-matrix must have at least one element.");
    }
    double minimum = _data[0];
    for (int n = 1; n < n_elem; n++) {
      minimum = Math.min(minimum, _data[n]);
    }

    return minimum;
  }

  /**
   * Returns the smallest value within the matrix and stores its position in {@code index_of_min_val}.
   * <p>
   * <b>Note:</b> Unfortunately, the storage variable must be of the mutable type int[].
   * 
   * @param index_of_min_val The storage of the position
   * 
   * @throws RuntimeException The ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public double min(final int[] index_of_min_val) throws RuntimeException {
    if (is_empty()) {
      throw new RuntimeException("The (" + n_rows + ", " + n_cols + ")-matrix must have at least one element.");
    }

    double minimum = _data[0];
    index_of_min_val[0] = 0;
    for (int n = 1; n < n_elem; n++) {
      double value = _data[n];

      if (value < minimum) {
        minimum = value;
        index_of_min_val[0] = n;
      }
    }

    return minimum;
  }

  /**
   * Returns the largest value within the matrix.
   * 
   * @throws RuntimeException The ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public double max() throws RuntimeException {
    if (is_empty()) {
      throw new RuntimeException("The (" + n_rows + ", " + n_cols + ")-matrix must have at least one element.");
    }

    double maximum = _data[0];
    for (int n = 1; n < n_elem; n++) {
      maximum = Math.max(maximum, _data[n]);
    }

    return maximum;
  }

  /**
   * Returns the largest value within the matrix and stores its position in {@code index_of_max_val}.
   * <p>
   * <b>Note:</b> Unfortunately, the storage variable must be of the mutable type int[].
   * 
   * @param index_of_max_val The storage of the position
   * 
   * @throws RuntimeException The ({@code X.n_rows}, {@code X.n_cols})-matrix must have at least one element.
   */
  public double max(final int[] index_of_max_val) throws RuntimeException {
    if (is_empty()) {
      throw new RuntimeException("The (" + n_rows + ", " + n_cols + ")-matrix must have at least one element.");
    }

    double maximum = _data[0];
    index_of_max_val[0] = 0;
    for (int n = 1; n < n_elem; n++) {
      double value = _data[n];

      if (value > maximum) {
        maximum = value;
        index_of_max_val[0] = n;
      }
    }

    return maximum;
  }

  /**
   * Sets all elements to 1.
   */
  public void ones() {
    fill(1);
  }

  /**
   * Sets all elements to 0.
   */
  public void zeros() {
    fill(0);
  }

  /**
   * Sets all elements to the specified value.
   * 
   * @param value The value
   */
  public void fill(final double value) {
    Arrays.fill(_data, value);
  }

  /**
   * Prints the matrix to System.out.
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   */
  public void print() throws AssertionError {
    print("");
  }

  /**
   * Prints the matrix unformatted to System.out, with a specified header.
   * 
   * @param header The header
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   */
  public void print(final String header) throws AssertionError {
    print(System.out, header);
  }

  /**
   * Prints the matrix to a specified stream.
   * 
   * @param stream The stream
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   */
  public void print(final OutputStream stream) throws AssertionError {
    print(stream, "");
  }

  /**
   * Prints the matrix to a specified stream, with a specified header.
   * 
   * @param stream The stream
   * @param header The header
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   */
  public void print(final OutputStream stream, final String header) throws AssertionError {
    PrintWriter writer;
    try {
      writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(stream, "UTF-8")));
    } catch(UnsupportedEncodingException exception) {
      throw new AssertionError("UTF-8 not supported by your JVM.");
    }

    if (!header.isEmpty()) {
      writer.println(header);
    }
    writer.println(this);
  }

  /**
   * Prints the matrix unformatted to System.out.
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   */
  public void raw_print() throws AssertionError {
    raw_print("");
  }

  /**
   * Prints the matrix unformatted to System.out, with a specified header.
   * 
   * @param header The header
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   */
  public void raw_print(final String header) throws AssertionError {
    raw_print(System.out, "");
  }

  /**
   * Prints the matrix unformatted to a specified stream.
   * 
   * @param stream The stream
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   */
  public void raw_print(final OutputStream stream) throws AssertionError {
    raw_print(stream, "");
  }

  /**
   * Prints the matrix unformatted to a specified stream, with a specified header.
   * 
   * @param stream The stream
   * @param header The header
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   */
  public void raw_print(final OutputStream stream, final String header) {
    PrintWriter writer;
    try {
      writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(stream, "UTF-8")));
    } catch(UnsupportedEncodingException exception) {
      throw new AssertionError("UTF-8 not supported by your JVM.");
    }

    if (!header.isEmpty()) {
      writer.println(header);
    }

    writer.print("(" + n_rows + ", " + n_cols + ")-matrix: [");
    writer.println();

    for (int i = 0; i < n_rows; i++) {
      writer.print(" ");

      for (int j = 0; j < n_cols; j++) {
        double value = _data[i + j * n_rows];
        if (Double.isInfinite(value)) {
          String sign = "";
          if (value < 0) {
            sign = "-";
          }
          writer.print(sign + "Inf");
        } else {
          writer.print(value);
        }
      }
      writer.println();
    }
    writer.print("]");
    writer.println();
  }

  /**
   * Sets each element to a pseudo-random value drawn from the standard uniform distribution on the left-closed and
   * right-open interval [0,1).
   * <p>
   * <b>Non-canonical:</b> Drawn from [0,1) instead of the closed interval [0,1].
   */
  public void randu() {
    for (int n = 0; n < n_elem; n++) {
      _data[n] = RNG._rng.nextDouble();
    }
  }

  /**
   * Sets each element to a pseudo-random value drawn from the standard normal distribution with mean 0.0 and standard
   * deviation 1.0.
   */
  public void randn() {
    for (int n = 0; n < n_elem; n++) {
      _data[n] = RNG._rng.nextGaussian();
    }
  }

  /**
   * Stores the matrix at the specified file path and returns {@code true} on success.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param name The file path
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   * 
   * @see FileType
   */
  public boolean save(final String name) throws AssertionError, FileNotFoundException {
    return save(name, FileType.RAW_ASCII);
  }

  /**
   * Stores the matrix at the specified file path in the requested file format and returns {@code true} on success.
   * 
   * @param name The file path
   * @param file_type The file format
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   * 
   * @see FileType
   */
  public boolean save(final String name, final FileType file_type) throws AssertionError, FileNotFoundException {
    return save(new FileOutputStream(name, false), file_type);
  }

  /**
   * Stores the matrix at the specified stream and returns {@code true} on success.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param stream The stream
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   * 
   * @see FileType
   */
  public boolean save(final OutputStream stream) throws AssertionError {
    return save(stream, FileType.RAW_ASCII);
  }

  /**
   * Stores the matrix at the specified stream in the requested file format and returns {@code true} on success.
   * 
   * @param stream The stream
   * @param file_type The file format
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   * 
   * @see FileType
   */
  public boolean save(final OutputStream stream, final FileType file_type) throws AssertionError {
    /*
     * file_type is ignored since only FileType.RAW_ASCII is currently supported.
     */

    PrintWriter writer;
    try {
      writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(stream, "UTF-8")));
    } catch(UnsupportedEncodingException exception) {
      throw new AssertionError("UTF-8 not supported by your JVM.");
    }

    for (int i = 0; i < n_rows; i++)
    {
      writer.print(" ");

      for (int j = 0; j < n_cols; j++)
      {
        double value = _data[i + j * n_rows];
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
   * Fills the matrix with data from the specified file path and returns {@code true} on success.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param name The file path
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   * @throws IllegalArgumentException All rows must have the same number of columns.
   * 
   * @see FileType
   */
  public boolean load(final String name) throws AssertionError, IllegalArgumentException, FileNotFoundException, IOException {
    return load(name, FileType.RAW_ASCII);
  }

  /**
   * Fills the matrix with data expected to be in specified format from the specified file path and returns true
   * on success.
   * 
   * @param name The file path
   * @param file_type The file format
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   * @throws IllegalArgumentException All rows must have the same number of columns.
   * 
   * @see FileType
   */
  public boolean load(final String name, final FileType file_type) throws AssertionError, IllegalArgumentException, FileNotFoundException, IOException {
    return load(new FileInputStream(name), file_type);
  }

  /**
   * Fills the matrix with data from the specified stream and returns {@code true} on success.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param stream The stream
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   * @throws IllegalArgumentException All rows must have the same number of columns.
   * 
   * @see FileType
   */
  public boolean load(final InputStream stream) throws AssertionError, IllegalArgumentException, IOException {
    return load(stream, FileType.RAW_ASCII);
  }

  /**
   * Fills the matrix with data expected to be in specified format from the specified stream and returns {@code true} on
   * success.
   * 
   * @param stream The stream
   * @param file_type The file format
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   * @throws IllegalArgumentException All rows must have the same number of columns.
   * 
   * @see FileType
   */
  public boolean load(final InputStream stream, final FileType file_type) throws AssertionError, IllegalArgumentException, IOException {
    /*
     * file_type is ignored since only FileType.RAW_ASCII is currently supported.
     */

    BufferedReader reader;
    try {
      reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
    } catch(UnsupportedEncodingException exception) {
      throw new AssertionError("UTF-8 not supported by your JVM.");
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
        throw new IllegalArgumentException("All rows must have the same number of columns.");
      }

      double[] rowDouble = new double[numberOfColumns];
      for (int j = 0; j < numberOfColumns; j++) {
        switch (rowString[j].toLowerCase()) {
          case "inf":
            rowDouble[j] = Datum.inf;
            break;
          case "-inf":
            rowDouble[j] = -Datum.inf;
            break;
          case "nan":
          case "-nan":
            rowDouble[j] = Datum.nan;
            break;
          default:
            rowDouble[j] = Double.valueOf(rowString[j]);
            break;
        }
      }
      matrix.add(rowDouble);

    } while ((line = reader.readLine()) != null);
    int numberOfRows = matrix.size();

    n_cols = numberOfColumns;
    n_rows = numberOfRows;
    n_elem = n_rows * n_cols;
    _data = new double[n_elem];

    int n = 0;
    for (int j = 0; j < n_cols; j++) {
      for (int i = 0; i < n_rows; i++) {
        _data[n++] = matrix.get(i)[j];
      }
    }
    reader.close();

    return true;
  }

  /**
   * Same as {@link #save(String)} but without error reporting. Returns {@code false} if an error occurred.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param name The file name
   */
  public boolean quiet_save(final String name) {
    return quiet_save(name, FileType.RAW_ASCII);
  }

  /**
   * Same as {@link #save(String, FileType)} but without error reporting. Returns {@code false} if an error occurred.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param name The file name
   * @param file_type The file format
   */
  public boolean quiet_save(final String name, final FileType file_type) {
    try {
      return save(new FileOutputStream(name, false), file_type);
    } catch(FileNotFoundException e) {
      return false;
    }
  }

  /**
   * Same as {@link #save(OutputStream)} but without error reporting. Returns {@code false} if an error occurred.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param stream The stream
   */
  public boolean quiet_save(final OutputStream stream) {
    return quiet_save(stream, FileType.RAW_ASCII);
  }

  /**
   * Same as {@link #save(OutputStream, FileType)} but without error reporting. Returns {@code false} if an error
   * occurred.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param stream The stream
   * @param file_type The file format
   */
  public boolean quiet_save(final OutputStream stream, final FileType file_type) {
    try {
      return save(stream, file_type);
    } catch(Exception e) {
      return false;
    }
  }

  /**
   * Same as {@link #load(String)} but without error reporting. Returns {@code false} if an error occurred.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param name The file path
   */
  public boolean quiet_load(final String name) {
    return quiet_load(name, FileType.RAW_ASCII);
  }

  /**
   * Same as {@link #load(String, FileType)} but without error reporting. Returns {@code false} if an error occurred.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param name The file path
   * @param file_type The file format
   */
  public boolean quiet_load(final String name, final FileType file_type) {
    try {
      return quiet_load(new FileInputStream(name), file_type);
    } catch(FileNotFoundException e) {
      return false;
    }
  }

  /**
   * Same as {@link #load(InputStream)} but without error reporting. Returns {@code false} if an error occurred.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param stream The stream
   */
  public boolean quiet_load(final InputStream stream) {
    return quiet_load(stream, FileType.RAW_ASCII);
  }

  /**
   * Same as {@link #load(InputStream, FileType)} but without error reporting. Returns {@code false} if an error
   * occurred.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param stream The stream
   * @param file_type The file format
   */
  public boolean quiet_load(final InputStream stream, final FileType file_type) {
    try {
      return load(stream, file_type);
    } catch(Exception e) {
      return false;
    }
  }

  /**
   * Causes the matrix to be empty.
   */
  public void reset() {
    set_size(0);
  }

  /**
   * C++ STL-like function
   * <p>
   * Causes the matrix to be empty.
   */
  public void clear() {
    reset();
  }

  /**
   * C++ STL-like function
   * <p>
   * Returns {@code true} if the matrix has no elements.
   */
  public boolean empty() {
    return is_empty();
  }

  /**
   * C++ STL-like function
   * <p>
   * Returns the number of elements.
   */
  public int size() {
    return n_elem;
  }

  /**
   * Returns a deep copy of the {@code col_number}th column.
   * 
   * @param col_number The column
   * 
   * @throws IndexOutOfBoundsException The specified column ({@code col_number}) is out of bounds.
   */
  public Col col(final int col_number) throws IndexOutOfBoundsException {
    if (col_number < 0 || col_number > n_cols - 1) {
      throw new IndexOutOfBoundsException("The specified column (" + col_number + ") is out of bounds.");
    }

    int n = col_number * n_rows;
    return new Col(Arrays.copyOfRange(_data, n, n + n_rows));
  }

//  /**
//   * Performs an in-place unary operation on the {@code col_number}th column.
//   * 
//   * @param col_number The column
//   * @param unary_operator The unary operator
//   * 
//   * @throws IndexOutOfBoundsException The specified column ({@code col_number}) is out of bounds.
//   * @throws UnsupportedOperationException Unexpected operator ({@code unary_operator}).
//   */
//  public void col(final int col_number, final Op unary_operator) throws IndexOutOfBoundsException, UnsupportedOperationException {
//    /*
//     * The parameter "unary_operator" is validated within AbstractView.inPlace(Op).
//     */
//
//    if (col_number < 0 || col_number > n_cols - 1) {
//      throw new IndexOutOfBoundsException("The specified column (" + col_number + ") is out of bounds.");
//    }
//
//    new ViewSubCol(this, col_number).inPlace(unary_operator);
//  }

  /**
   * Performs an in-place binary operation on the {@code col_number}th column with the specified right-hand side
   * operand.
   * 
   * @param col_number The column
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The specified column ({@code col_number}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void col(final int col_number, final Op binary_operator, final double operand) throws IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, double).
     */

    if (col_number < 0 || col_number > n_cols - 1) {
      throw new IndexOutOfBoundsException("The specified column (" + col_number + ") is out of bounds.");
    }

    new ViewSubCol(this, col_number).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the {@code col_number}th column with the specified right-hand side
   * operand.
   * 
   * @param col_number The column
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The specified column ({@code col_number}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void col(final int col_number, final Op binary_operator, final AbstractMat operand) throws IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, AbstractMat).
     */

    if (col_number < 0 || col_number > n_cols - 1) {
      throw new IndexOutOfBoundsException("The specified column (" + col_number + ") is out of bounds.");
    }

    new ViewSubCol(this, col_number).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the {@code row_number}th row.
   * 
   * @param row_number The column
   * 
   * @throws IndexOutOfBoundsException The specified row ({@code row_number}) is out of bounds.
   */
  public Row row(final int row_number) throws IndexOutOfBoundsException {
    if (row_number < 0 || row_number > n_rows - 1) {
      throw new IndexOutOfBoundsException("The specified row (" + row_number + ") is out of bounds.");
    }

    return new Row(new ViewSubRow(this, row_number));
  }

//  /**
//   * Performs an in-place unary operation on the {@code row_number}th row.
//   * 
//   * @param row_number The row
//   * @param unary_operator The unary operator
//   * 
//   * @throws IndexOutOfBoundsException The specified row ({@code row_number}) is out of bounds.
//   * @throws UnsupportedOperationException Unexpected operator ({@code unary_operator}).
//   */
//  public void row(final int row_number, final Op unary_operator) throws IndexOutOfBoundsException, UnsupportedOperationException {
//    /*
//     * The parameter "unary_operator" is validated within AbstractView.inPlace(Op).
//     */
//
//    if (row_number < 0 || row_number > n_rows - 1) {
//      throw new IndexOutOfBoundsException("The specified row (" + row_number + ") is out of bounds.");
//    }
//
//    new ViewSubRow(this, row_number).inPlace(unary_operator);
//  }

  /**
   * Performs an in-place binary operation on the {@code row_number}th row with the specified right-hand side operand.
   * 
   * @param row_number The row
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The specified row ({@code row_number}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void row(final int row_number, final Op binary_operator, final double operand) throws IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, double).
     */

    if (row_number < 0 || row_number > n_rows - 1) {
      throw new IndexOutOfBoundsException("The specified row (" + row_number + ") is out of bounds.");
    }

    new ViewSubRow(this, row_number).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the {@code row_number}th row with the specified right-hand side operand.
   * 
   * @param row_number The row
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The specified row ({@code row_number}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void row(final int row_number, final Op binary_operator, final AbstractMat operand) throws IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, AbstractMat).
     */

    if (row_number < 0 || row_number > n_rows - 1) {
      throw new IndexOutOfBoundsException("The specified row (" + row_number + ") is out of bounds.");
    }

    new ViewSubRow(this, row_number).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the {@code first_col}th to {@code last_col} column.
   * 
   * @param first_col The first column
   * @param last_col The last column
   * 
   * @throws RuntimeException The first specified column ({@code first_col}) must be less than or equal the last
   *           specified column ({@code last_col}).
   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bounds.
   */
  public Mat cols(final int first_col, final int last_col) throws RuntimeException, IndexOutOfBoundsException {
    if (last_col < first_col) {
      throw new RuntimeException("The first specified column (" + first_col + ") must be less than or equal the last specified column (" + last_col + ").");
    }

    if (first_col < 0) {
      throw new IndexOutOfBoundsException("The first specified column (" + first_col + ") is out of bounds.");
    }

    if (last_col > n_cols - 1) {
      throw new IndexOutOfBoundsException("The last specified column (" + last_col + ") is out of bounds.");
    }

    /*
     * Faster than new Mat(ViewSubCols(this, first_col, last_col - first_col + 1))
     */
    Mat cols = new Mat(n_rows, last_col - first_col + 1);
    System.arraycopy(_data, first_col * n_rows, cols._data, 0, cols.n_cols * n_rows);
    return cols;
  }

//  /**
//   * Performs an in-place unary operation on the {@code first_col}th to {@code last_col} column.
//   * 
//   * @param first_col The first column
//   * @param last_col The last column
//   * @param unary_operator The unary operator
//   * 
//   * @throws RuntimeException The first specified column ({@code first_col}) must be less than or equal the last
//   *           specified column ({@code last_col}).
//   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bounds.
//   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bounds.
//   * @throws UnsupportedOperationException Unexpected operator ({@code unary_operator}).
//   */
//  public void cols(final int first_col, final int last_col, final Op unary_operator) throws RuntimeException, IndexOutOfBoundsException, UnsupportedOperationException {
//    /*
//     * The parameter "unary_operator" is validated within AbstractView.inPlace(Op).
//     */
//
//    if (last_col < first_col) {
//      throw new RuntimeException("The first specified column (" + first_col + ") must be less than or equal the last specified column (" + last_col + ").");
//    }
//
//    if (first_col < 0) {
//      throw new IndexOutOfBoundsException("The first specified column (" + first_col + ") is out of bounds.");
//    }
//
//    if (last_col > n_cols - 1) {
//      throw new IndexOutOfBoundsException("The last specified column (" + last_col + ") is out of bounds.");
//    }
//
//    new ViewSubCols(this, first_col, last_col - first_col + 1).inPlace(unary_operator);
//  }

  /**
   * Performs an in-place binary operation on the {@code first_col}th to {@code last_col} column with the specified
   * right-hand side operand.
   * 
   * @param first_col The first column
   * @param last_col The last column
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws RuntimeException The first specified column ({@code first_col}) must be less than or equal the last
   *           specified column ({@code last_col}).
   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void cols(final int first_col, final int last_col, Op binary_operator, double operand) throws RuntimeException, IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, double).
     */

    if (last_col < first_col) {
      throw new RuntimeException("The first specified column (" + first_col + ") must be less than or equal the last specified column (" + last_col + ").");
    }

    if (first_col < 0) {
      throw new IndexOutOfBoundsException("The first specified column (" + first_col + ") is out of bounds.");
    }

    if (last_col > n_cols - 1) {
      throw new IndexOutOfBoundsException("The last specified column (" + last_col + ") is out of bounds.");
    }

    new ViewSubCols(this, first_col, last_col - first_col + 1).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the {@code first_col}th to {@code last_col} column with the specified
   * right-hand side operand.
   * 
   * @param first_col The first column
   * @param last_col The last column
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws RuntimeException The first specified column ({@code first_col}) must be less than or equal the last
   *           specified column ({@code last_col}).
   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void cols(final int first_col, final int last_col, Op binary_operator, AbstractMat operand) throws RuntimeException, IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, AbstractMat).
     */

    if (last_col < first_col) {
      throw new RuntimeException("The first specified column (" + first_col + ") must be less than or equal the last specified column (" + last_col + ").");
    }

    if (first_col < 0) {
      throw new IndexOutOfBoundsException("The first specified column (" + first_col + ") is out of bounds.");
    }

    if (last_col > n_cols - 1) {
      throw new IndexOutOfBoundsException("The last specified column (" + last_col + ") is out of bounds.");
    }

    new ViewSubCols(this, first_col, last_col - first_col + 1).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the {@code first_row}th to {@code last_row} row.
   * 
   * @param first_row The first row
   * @param last_row The last row
   * 
   * @throws RuntimeException The first specified row ({@code first_col}) must be less than or equal the last specified
   *           row ({@code last_col}).
   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_col}) is out of bounds.
   */
  public Mat rows(final int first_row, final int last_row) throws RuntimeException, IndexOutOfBoundsException {
    if (last_row < first_row) {
      throw new RuntimeException("The first specified row (" + first_row + ") must be less than or equal the last specified row (" + last_row + ").");
    }

    if (first_row < 0) {
      throw new IndexOutOfBoundsException("The first specified row (" + first_row + ") is out of bounds.");
    }

    if (last_row > n_rows - 1) {
      throw new IndexOutOfBoundsException("The last specified row (" + last_row + ") is out of bounds.");
    }

    return new Mat(new ViewSubRows(this, first_row, last_row - first_row + 1));
  }

//  /**
//   * Performs an in-place unary operation on the {@code first_row}th to {@code last_row} row.
//   * 
//   * @param first_row The first row
//   * @param last_row The last row
//   * @param unary_operator The unary operator
//   * 
//   * @throws RuntimeException The first specified row ({@code first_col}) must be less than or equal the last specified
//   *           row ({@code last_col}).
//   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
//   * @throws IndexOutOfBoundsException The last specified row ({@code last_col}) is out of bounds.
//   * @throws UnsupportedOperationException Unexpected operator ({@code unary_operator}).
//   */
//  public void rows(final int first_row, final int last_row, final Op unary_operator) throws RuntimeException, IndexOutOfBoundsException, UnsupportedOperationException {
//    /*
//     * The parameter "unary_operator" is validated within AbstractView.inPlace(Op).
//     */
//
//    if (last_row < first_row) {
//      throw new RuntimeException("The first specified row (" + first_row + ") must be less than or equal the last specified row (" + last_row + ").");
//    }
//
//    if (first_row < 0) {
//      throw new IndexOutOfBoundsException("The first specified row (" + first_row + ") is out of bounds.");
//    }
//
//    if (last_row > n_rows - 1) {
//      throw new IndexOutOfBoundsException("The last specified row (" + last_row + ") is out of bounds.");
//    }
//
//    new ViewSubRows(this, first_row, last_row - first_row + 1).inPlace(unary_operator);
//  }

  /**
   * Performs an in-place binary operation on the {@code first_row}th to {@code last_row} row with the specified
   * right-hand side operand.
   * 
   * @param first_row The first row
   * @param last_row The last row
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws RuntimeException The first specified row ({@code first_col}) must be less than or equal the last specified
   *           row ({@code last_col}).
   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_col}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void rows(final int first_row, final int last_row, final Op binary_operator, final double operand) throws RuntimeException, IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, double).
     */

    if (last_row < first_row) {
      throw new RuntimeException("The first specified row (" + first_row + ") must be less than or equal the last specified row (" + last_row + ").");
    }

    if (first_row < 0) {
      throw new IndexOutOfBoundsException("The first specified row (" + first_row + ") is out of bounds.");
    }

    if (last_row > n_rows - 1) {
      throw new IndexOutOfBoundsException("The last specified row (" + last_row + ") is out of bounds.");
    }

    new ViewSubRows(this, first_row, last_row - first_row + 1).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the {@code first_row}th to {@code last_row} row with the specified
   * right-hand side operand.
   * 
   * @param first_row The first row
   * @param last_row The last row
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws RuntimeException The first specified row ({@code first_col}) must be less than or equal the last specified
   *           row ({@code last_col}).
   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_col}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void rows(final int first_row, final int last_row, final Op binary_operator, final AbstractMat operand) throws RuntimeException, IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, AbstractMat).
     */

    if (last_row < first_row) {
      throw new RuntimeException("The first specified row (" + first_row + ") must be less than or equal the last specified row (" + last_row + ").");
    }

    if (first_row < 0) {
      throw new IndexOutOfBoundsException("The first specified row (" + first_row + ") is out of bounds.");
    }

    if (last_row > n_rows - 1) {
      throw new IndexOutOfBoundsException("The last specified row (" + last_row + ") is out of bounds.");
    }

    new ViewSubRows(this, first_row, last_row - first_row + 1).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the {@code span._first}th to {@code span._last} row of the {@code col_number}th column.
   * 
   * @param span The span
   * @param col_number The column
   * 
   * @throws IndexOutOfBoundsException The specified column ({@code col_number}) is out of bounds.
   * @throws IndexOutOfBoundsException The first specified row ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code span._last}) is out of bounds.
   */
  public Col col(final Span span, final int col_number) throws IndexOutOfBoundsException {
    /*
     * The parameter "span" was already validated during its instantiation.
     */

    if (span._isEntireRange) {
      return col(col_number);
    } else {
      if (col_number < 0 || col_number > n_cols - 1) {
        throw new IndexOutOfBoundsException("The specified column (" + col_number + ") is out of bounds.");
      }

      if (span._first < 0) {
        throw new IndexOutOfBoundsException("The first specified row (" + span._first + ") is out of bounds.");
      }

      if (span._last > n_rows - 1) {
        throw new IndexOutOfBoundsException("The last specified row (" + span._last + ") is out of bounds.");
      }

      int n = col_number * n_rows;
      return new Col(Arrays.copyOfRange(_data, span._first + n, span._last + 1 + n));
    }
  }

//  /**
//   * Performs an in-place unary operation on the {@code span._first}th to {@code span._last} row of the
//   * {@code col_number}th column.
//   * 
//   * @param span The span
//   * @param col_number The column
//   * @param unary_operator The unary operator
//   * 
//   * @throws IndexOutOfBoundsException The specified column ({@code col_number}) is out of bounds.
//   * @throws IndexOutOfBoundsException The first specified row ({@code span._first}) is out of bounds.
//   * @throws IndexOutOfBoundsException The last specified row ({@code span._last}) is out of bounds.
//   * @throws UnsupportedOperationException Unexpected operator ({@code unary_operator}).
//   */
//  public void col(final Span span, final int col_number, final Op unary_operator) throws IndexOutOfBoundsException, UnsupportedOperationException {
//    /*
//     * The parameter "span" was already validated during its instantiation.
//     * The parameter "unary_operator" is validated within AbstractView.inPlace(Op).
//     */
//
//    if (span._isEntireRange) {
//      col(col_number, unary_operator);
//    } else {
//      if (col_number < 0 || col_number > n_cols - 1) {
//        throw new IndexOutOfBoundsException("The specified column (" + col_number + ") is out of bounds.");
//      }
//
//      if (span._first < 0) {
//        throw new IndexOutOfBoundsException("The first specified row (" + span._first + ") is out of bounds.");
//      }
//
//      if (span._last > n_rows - 1) {
//        throw new IndexOutOfBoundsException("The last specified row (" + span._last + ") is out of bounds.");
//      }
//
//      new ViewSubCol(this, col_number, span._first, span._last - span._first + 1).inPlace(unary_operator);
//    }
//  }

  /**
   * Performs an in-place binary operation on the {@code span._first}th to {@code span._last} row of the
   * {@code col_number}th column with the specified right-hand side operand.
   * 
   * @param span The span
   * @param col_number The column
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The specified column ({@code col_number}) is out of bounds.
   * @throws IndexOutOfBoundsException The first specified row ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code span._last}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void col(final Span span, final int col_number, final Op binary_operator, final double operand) throws IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "span" was already validated during its instantiation.
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, double).
     */

    if (span._isEntireRange) {
      col(col_number, binary_operator, operand);
    } else {
      if (col_number < 0 || col_number > n_cols - 1) {
        throw new IndexOutOfBoundsException("The specified column (" + col_number + ") is out of bounds.");
      }

      if (span._first < 0) {
        throw new IndexOutOfBoundsException("The first specified row (" + span._first + ") is out of bounds.");
      }

      if (span._last > n_rows - 1) {
        throw new IndexOutOfBoundsException("The last specified row (" + span._last + ") is out of bounds.");
      }

      new ViewSubCol(this, col_number, span._first, span._last - span._first + 1).inPlace(binary_operator, operand);
    }
  }

  /**
   * Performs an in-place binary operation on the {@code span._first}th to {@code span._last} row of the
   * {@code col_number}th column with the specified right-hand side operand.
   * 
   * @param span The span
   * @param col_number The column
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The specified column ({@code col_number}) is out of bounds.
   * @throws IndexOutOfBoundsException The first specified row ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code span._last}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void col(final Span span, final int col_number, final Op binary_operator, AbstractMat operand) throws IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "span" was already validated during its instantiation.
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, AbstractMat).
     */

    if (span._isEntireRange) {
      col(col_number, binary_operator, operand);
    } else {
      if (col_number < 0 || col_number > n_cols - 1) {
        throw new IndexOutOfBoundsException("The specified column (" + col_number + ") is out of bounds.");
      }

      if (span._first < 0) {
        throw new IndexOutOfBoundsException("The first specified row (" + span._first + ") is out of bounds.");
      }

      if (span._last > n_rows - 1) {
        throw new IndexOutOfBoundsException("The last specified row (" + span._last + ") is out of bounds.");
      }

      new ViewSubCol(this, col_number, span._first, span._last - span._first + 1).inPlace(binary_operator, operand);
    }
  }

  /**
   * Returns a deep copy of the {@code row_number}th row of the {@code span._first}th to {@code span._last} column.
   * 
   * @param row_number The row
   * @param span The span
   * 
   * @throws IndexOutOfBoundsException The specified row ({@code row_number}) is out of bounds.
   * @throws IndexOutOfBoundsException The first specified column ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified column ({@code span._last}) is out of bounds.
   */
  public Row row(final int row_number, final Span span) throws IndexOutOfBoundsException {
    /*
     * The parameter "span" was already validated during its instantiation.
     */

    if (span._isEntireRange) {
      return row(row_number);
    } else {
      if (row_number < 0 || row_number > n_rows - 1) {
        throw new IndexOutOfBoundsException("The specified row (" + row_number + ") is out of bounds.");
      }

      if (span._first < 0) {
        throw new IndexOutOfBoundsException("The first specified column (" + span._first + ") is out of bounds.");
      }

      if (span._last > n_cols - 1) {
        throw new IndexOutOfBoundsException("The last specified column (" + span._last + ") is out of bounds.");
      }

      return new Row(new ViewSubRow(this, row_number, span._first, span._last - span._first + 1));
    }
  }

//  /**
//   * Performs an in-place unary operation on the {@code row_number}th row of the {@code span._first}th to
//   * {@code span._last} column.
//   * 
//   * @param row_number The row
//   * @param span The span
//   * @param unary_operator The unary operator
//   * 
//   * @throws IndexOutOfBoundsException The specified row ({@code row_number}) is out of bounds.
//   * @throws IndexOutOfBoundsException The first specified column ({@code span._first}) is out of bounds.
//   * @throws IndexOutOfBoundsException The last specified column ({@code span._last}) is out of bounds.
//   * @throws UnsupportedOperationException Unexpected operator ({@code unary_operator}).
//   */
//  public void row(final int row_number, final Span span, final Op unary_operator) throws IndexOutOfBoundsException, UnsupportedOperationException {
//    /*
//     * The parameter "span" was already validated during its instantiation.
//     * The parameter "unary_operator" is validated within AbstractView.inPlace(Op).
//     */
//
//    if (span._isEntireRange) {
//      row(row_number, unary_operator);
//    } else {
//      if (row_number < 0 || row_number > n_rows - 1) {
//        throw new IndexOutOfBoundsException("The specified row (" + row_number + ") is out of bounds.");
//      }
//
//      if (span._first < 0) {
//        throw new IndexOutOfBoundsException("The first specified column (" + span._first + ") is out of bounds.");
//      }
//
//      if (span._last > n_cols - 1) {
//        throw new IndexOutOfBoundsException("The last specified column (" + span._last + ") is out of bounds.");
//      }
//
//      new ViewSubRow(this, row_number, span._first, span._last - span._first + 1).inPlace(unary_operator);
//    }
//  }

  /**
   * Performs an in-place binary operation on the {@code row_number}th row of the {@code span._first}th to
   * {@code span._last} column with the specified right-hand side operand.
   * 
   * @param row_number The row
   * @param span The span
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The specified row ({@code row_number}) is out of bounds.
   * @throws IndexOutOfBoundsException The first specified column ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified column ({@code span._last}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void row(final int row_number, final Span span, final Op binary_operator, final double operand) throws IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "span" was already validated during its instantiation.
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, double).
     */

    if (span._isEntireRange) {
      row(row_number, binary_operator, operand);
    } else {
      if (row_number < 0 || row_number > n_rows - 1) {
        throw new IndexOutOfBoundsException("The specified row (" + row_number + ") is out of bounds.");
      }

      if (span._first < 0) {
        throw new IndexOutOfBoundsException("The first specified column (" + span._first + ") is out of bounds.");
      }

      if (span._last > n_cols - 1) {
        throw new IndexOutOfBoundsException("The last specified column (" + span._last + ") is out of bounds.");
      }

      new ViewSubRow(this, row_number, span._first, span._last - span._first + 1).inPlace(binary_operator, operand);
    }
  }

  /**
   * Performs an in-place binary operation on the {@code row_number}th row of the {@code span._first}th to
   * {@code span._last} column with the specified right-hand side operand.
   * 
   * @param row_number The row
   * @param span The span
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The specified row ({@code row_number}) is out of bounds.
   * @throws IndexOutOfBoundsException The first specified column ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified column ({@code span._last}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void row(final int row_number, final Span span, final Op binary_operator, final AbstractMat operand) throws IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "span" was already validated during its instantiation.
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, AbstractMat).
     */

    if (span._isEntireRange) {
      row(row_number, binary_operator, operand);
    } else {
      if (row_number < 0 || row_number > n_rows - 1) {
        throw new IndexOutOfBoundsException("The specified row (" + row_number + ") is out of bounds.");
      }

      if (span._first < 0) {
        throw new IndexOutOfBoundsException("The first specified column (" + span._first + ") is out of bounds.");
      }

      if (span._last > n_cols - 1) {
        throw new IndexOutOfBoundsException("The last specified column (" + span._last + ") is out of bounds.");
      }

      new ViewSubRow(this, row_number, span._first, span._last - span._first + 1).inPlace(binary_operator, operand);
    }
  }

  /**
   * Returns a deep copy of the {@code first_row}th to {@code last_row} row of the {@code first_col}th to
   * {@code last_col} column.
   * 
   * @param first_row The first row
   * @param first_col The first column
   * @param last_row The last row
   * @param last_col The last column
   * 
   * @throws RuntimeException The first specified column ({@code first_col}) must be less than or equal the last
   *           specified column ({@code last_col}).
   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bounds.
   * @throws RuntimeException The first specified row ({@code first_col}) must be less than or equal the last specified
   *           row ({@code last_col}).
   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_col}) is out of bounds.
   */
  public Mat submat(final int first_row, final int first_col, final int last_row, final int last_col) throws RuntimeException, IndexOutOfBoundsException {
    if (last_col < first_col) {
      throw new RuntimeException("The first specified column (" + first_col + ") must be less than or equal the last specified column (" + last_col + ").");
    }

    if (first_col < 0) {
      throw new IndexOutOfBoundsException("The first specified column (" + first_col + ") is out of bounds.");
    }

    if (last_col > n_cols - 1) {
      throw new IndexOutOfBoundsException("The last specified column (" + last_col + ") is out of bounds.");
    }

    if (last_row < first_row) {
      throw new RuntimeException("The first specified row (" + first_row + ") must be less than or equal the last specified row (" + last_row + ").");
    }

    if (first_row < 0) {
      throw new IndexOutOfBoundsException("The first specified row (" + first_row + ") is out of bounds.");
    }

    if (last_row > n_rows - 1) {
      throw new IndexOutOfBoundsException("The last specified row (" + last_row + ") is out of bounds.");
    }

    return new Mat(new ViewSubMat(this, first_row, first_col, last_row - first_row + 1, last_col - first_col + 1));
  }

//  /**
//   * Performs an in-place unary operation on the {@code first_row}th to {@code last_row} row of the {@code first_col}th
//   * to {@code last_col} column.
//   * 
//   * @param first_row The first row
//   * @param first_col The first column
//   * @param last_row The last row
//   * @param last_col The last column
//   * @param unary_operator The unary operator
//   * 
//   * @throws RuntimeException The first specified column ({@code first_col}) must be less than or equal the last
//   *           specified column ({@code last_col}).
//   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bounds.
//   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bounds.
//   * @throws RuntimeException The first specified row ({@code first_col}) must be less than or equal the last specified
//   *           row ({@code last_col}).
//   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
//   * @throws IndexOutOfBoundsException The last specified row ({@code last_col}) is out of bounds.
//   * @throws UnsupportedOperationException Unexpected operator ({@code unary_operator}).
//   */
//  public void submat(final int first_row, final int first_col, final int last_row, final int last_col, final Op unary_operator) throws RuntimeException, IndexOutOfBoundsException, UnsupportedOperationException {
//    /*
//     * The parameter "unary_operator" is validated within AbstractView.inPlace(Op).
//     */
//
//    if (last_col < first_col) {
//      throw new RuntimeException("The first specified column (" + first_col + ") must be less than or equal the last specified column (" + last_col + ").");
//    }
//
//    if (first_col < 0) {
//      throw new IndexOutOfBoundsException("The first specified column (" + first_col + ") is out of bounds.");
//    }
//
//    if (last_col > n_cols - 1) {
//      throw new IndexOutOfBoundsException("The last specified column (" + last_col + ") is out of bounds.");
//    }
//
//    if (last_row < first_row) {
//      throw new RuntimeException("The first specified row (" + first_row + ") must be less than or equal the last specified row (" + last_row + ").");
//    }
//
//    if (first_row < 0) {
//      throw new IndexOutOfBoundsException("The first specified row (" + first_row + ") is out of bounds.");
//    }
//
//    if (last_row > n_rows - 1) {
//      throw new IndexOutOfBoundsException("The last specified row (" + last_row + ") is out of bounds.");
//    }
//
//    new ViewSubMat(this, first_row, first_col, last_row - first_row + 1, last_col - first_col + 1).inPlace(unary_operator);
//  }

  /**
   * Performs an in-place binary operation on the {@code first_row}th to {@code last_row} row of the {@code first_col}th
   * to {@code last_col} column with the specified right-hand side operand.
   * 
   * @param first_row The first row
   * @param first_col The first column
   * @param last_row The last row
   * @param last_col The last column
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws RuntimeException The first specified column ({@code first_col}) must be less than or equal the last
   *           specified column ({@code last_col}).
   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bounds.
   * @throws RuntimeException The first specified row ({@code first_col}) must be less than or equal the last specified
   *           row ({@code last_col}).
   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_col}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void submat(final int first_row, final int first_col, final int last_row, final int last_col, final Op binary_operator, final double operand) throws RuntimeException, IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, double).
     */

    if (last_col < first_col) {
      throw new RuntimeException("The first specified column (" + first_col + ") must be less than or equal the last specified column (" + last_col + ").");
    }

    if (first_col < 0) {
      throw new IndexOutOfBoundsException("The first specified column (" + first_col + ") is out of bounds.");
    }

    if (last_col > n_cols - 1) {
      throw new IndexOutOfBoundsException("The last specified column (" + last_col + ") is out of bounds.");
    }

    if (last_row < first_row) {
      throw new RuntimeException("The first specified row (" + first_row + ") must be less than or equal the last specified row (" + last_row + ").");
    }

    if (first_row < 0) {
      throw new IndexOutOfBoundsException("The first specified row (" + first_row + ") is out of bounds.");
    }

    if (last_row > n_rows - 1) {
      throw new IndexOutOfBoundsException("The last specified row (" + last_row + ") is out of bounds.");
    }

    new ViewSubMat(this, first_row, first_col, last_row - first_row + 1, last_col - first_col + 1).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the {@code first_row}th to {@code last_row} row of the {@code first_col}th
   * to {@code last_col} column with the specified right-hand side operand.
   * 
   * @param first_row The first row
   * @param first_col The first column
   * @param last_row The last row
   * @param last_col The last column
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws RuntimeException The first specified column ({@code first_col}) must be less than or equal the last
   *           specified column ({@code last_col}).
   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bounds.
   * @throws RuntimeException The first specified row ({@code first_col}) must be less than or equal the last specified
   *           row ({@code last_col}).
   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_col}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void submat(final int first_row, final int first_col, final int last_row, final int last_col, final Op binary_operator, final AbstractMat operand) throws RuntimeException, IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "binary_operator" is validated within AbstractView.inPlace(Op, AbstractMat).
     */

    if (last_col < first_col) {
      throw new RuntimeException("The first specified column (" + first_col + ") must be less than or equal the last specified column (" + last_col + ").");
    }

    if (first_col < 0) {
      throw new IndexOutOfBoundsException("The first specified column (" + first_col + ") is out of bounds.");
    }

    if (last_col > n_cols - 1) {
      throw new IndexOutOfBoundsException("The last specified column (" + last_col + ") is out of bounds.");
    }

    if (last_row < first_row) {
      throw new RuntimeException("The first specified row (" + first_row + ") must be less than or equal the last specified row (" + last_row + ").");
    }

    if (first_row < 0) {
      throw new IndexOutOfBoundsException("The first specified row (" + first_row + ") is out of bounds.");
    }

    if (last_row > n_rows - 1) {
      throw new IndexOutOfBoundsException("The last specified row (" + last_row + ") is out of bounds.");
    }

    new ViewSubMat(this, first_row, first_col, last_row - first_row + 1, last_col - first_col + 1).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the {@code row_span._first}th to {@code row_span._last} row of the {@code col_span._first}th
   * to {@code col_span._last} column.
   * 
   * @param row_span The row span
   * @param col_span The column span
   * 
   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_col}) is out of bounds.
   */
  public Mat submat(final Span row_span, final Span col_span) throws IndexOutOfBoundsException {
    /*
     * The parameters "row_span" and "col_span" were already validated during their instantiation.
     */
    return submat(row_span._first, col_span._first, row_span._last, col_span._last);
  }

//  /**
//   * Performs an in-place unary operation on the {@code row_span._first}th to {@code row_span._last} row of the
//   * {@code col_span._first}th to {@code col_span._last} column.
//   * 
//   * @param row_span The row span
//   * @param col_span The column span
//   * @param unary_operator The unary operator
//   * 
//   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bounds.
//   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bounds.
//   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
//   * @throws IndexOutOfBoundsException The last specified row ({@code last_col}) is out of bounds.
//   * @throws UnsupportedOperationException Unexpected operator ({@code unary_operator}).
//   */
//  public void submat(final Span row_span, final Span col_span, final Op unary_operator) throws IndexOutOfBoundsException, UnsupportedOperationException {
//    /*
//     * The parameters "row_span" and "col_span" were already validated during their instantiation.
//     */
//    submat(row_span._first, col_span._first, row_span._last, col_span._last, unary_operator);
//  }

  /**
   * Performs an in-place binary operation on the {@code row_span._first}th to {@code row_span._last} row of the
   * {@code col_span._first}th to {@code col_span._last} column with the specified right-hand side operand.
   * 
   * @param row_span The row span
   * @param col_span The column span
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_col}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void submat(final Span row_span, final Span col_span, final Op binary_operator, final double operand) throws IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameters "row_span" and "col_span" were already validated during their instantiation.
     */
    submat(row_span._first, col_span._first, row_span._last, col_span._last, binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the {@code row_span._first}th to {@code row_span._last} row of the
   * {@code col_span._first}th to {@code col_span._last} column with the specified right-hand side operand.
   * 
   * @param row_span The row span
   * @param col_span The column span
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_col}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void submat(final Span row_span, final Span col_span, final Op binary_operator, final AbstractMat operand) throws IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameters "row_span" and "col_span" were already validated during their instantiation.
     */
    submat(row_span._first, col_span._first, row_span._last, col_span._last, binary_operator, operand);
  }

  /**
   * Returns a deep copy starting at position ({@code first_row}, {@code first_col}) of {@code size.n_rows} rows and
   * {@code size.n_cols} columns.
   * 
   * @param first_row The first row
   * @param first_col The first column
   * @param size The size
   * 
   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_col}) is out of bounds.
   */
  public Mat submat(final int first_row, final int first_col, final Size size) throws IndexOutOfBoundsException {
    /*
     * The parameter "size" was already validated during its instantiation.
     */
    return submat(first_row, first_col, first_row + size.n_rows - 1, first_col + size.n_cols - 1);
  }

//  /**
//   * Performs an in-place unary operation on the position ({@code first_row}, {@code first_col}) of {@code size.n_rows}
//   * rows and {@code size.n_cols} columns.
//   * 
//   * @param first_row The first row
//   * @param first_col The first column
//   * @param size The size
//   * @param unary_operator The unary operator
//   * 
//   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bounds.
//   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bounds.
//   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
//   * @throws IndexOutOfBoundsException The last specified row ({@code last_col}) is out of bounds.
//   * @throws UnsupportedOperationException Unexpected operator ({@code unary_operator}).
//   */
//  public void submat(final int first_row, final int first_col, final Size size, final Op unary_operator) throws IndexOutOfBoundsException, UnsupportedOperationException {
//    /*
//     * The parameter "size" was already validated during its instantiation.
//     */
//    submat(first_row, first_col, first_row + size.n_rows - 1, first_col + size.n_cols - 1, unary_operator);
//  }

  /**
   * Performs an in-place binary operation on the position ({@code first_row}, {@code first_col}) of {@code size.n_rows}
   * rows and {@code size.n_cols} columns with the specified right-hand side operand.
   * 
   * @param first_row The first row
   * @param first_col The first column
   * @param size The size
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_col}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void submat(final int first_row, final int first_col, final Size size, final Op binary_operator, final double operand) throws IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "size" was already validated during its instantiation.
     */
    submat(first_row, first_col, first_row + size.n_rows - 1, first_col + size.n_cols - 1, binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the position ({@code first_row}, {@code first_col}) of {@code size.n_rows}
   * rows and {@code size.n_cols} columns with the specified right-hand side operand.
   * 
   * @param first_row The first row
   * @param first_col The first column
   * @param size The size
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The first specified column ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified column ({@code last_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The first specified row ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last specified row ({@code last_col}) is out of bounds.
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void submat(final int first_row, final int first_col, final Size size, final Op binary_operator, final AbstractMat operand) throws IndexOutOfBoundsException, UnsupportedOperationException {
    /*
     * The parameter "size" was already validated during its instantiation.
     */
    submat(first_row, first_col, first_row + size.n_rows - 1, first_col + size.n_cols - 1, binary_operator, operand);
  }

  /**
   * Returns a deep copy of the specified elements.
   * <p>
   * <b>Note:</b> No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exceptions upon
   * errors.
   * 
   * @param vector_of_indices The positions
   */
  public Col elem(final AbstractVector vector_of_indices) {
    return new Col(new ViewElemMat(this, vector_of_indices._data));
  }

//  /**
//   * Performs an in-place unary operation on the specified elements.
//   * <p>
//   * <b>Note:</b> No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exceptions upon
//   * errors.
//   * 
//   * @param vector_of_indices The positions
//   * @param unary_operator The unary operator
//   * 
//   * @throws UnsupportedOperationException Unexpected operator ({@code unary_operator}).
//   */
//  public void elem(final AbstractVector vector_of_indices, final Op unary_operator) throws UnsupportedOperationException {
//    new ViewElemMat(this, vector_of_indices._data).inPlace(unary_operator);
//  }

  /**
   * Performs an in-place binary operation on the specified elements with the specified right-hand side operand.
   * <p>
   * <b>Note:</b> No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exceptions upon
   * errors.
   * 
   * @param vector_of_indices The positions
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void elem(final AbstractVector vector_of_indices, final Op binary_operator, final double operand) throws UnsupportedOperationException {
    new ViewElemMat(this, vector_of_indices._data).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the specified elements with the specified right-hand side operand.
   * <p>
   * <b>Note:</b> No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exceptions upon
   * errors.
   * 
   * @param vector_of_indices The positions
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void elem(final AbstractVector vector_of_indices, final Op binary_operator, final AbstractMat operand) throws UnsupportedOperationException {
    new ViewElemMat(this, vector_of_indices._data).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the specified columns.
   * <p>
   * <b>Note:</b> No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exceptions upon
   * errors.
   * 
   * @param vector_of_column_indices The columns
   */
  public Mat cols(final AbstractVector vector_of_column_indices) {
    return new Mat(new ViewElemCols(this, vector_of_column_indices._data));
  }

//  /**
//   * Performs an in-place unary operation on the specified columns.
//   * <p>
//   * <b>Note:</b> No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exceptions upon
//   * errors.
//   * 
//   * @param vector_of_column_indices The columns
//   * @param unary_operator The unary operator
//   * 
//   * @throws UnsupportedOperationException Unexpected operator ({@code unary_operator}).
//   */
//  public void cols(final AbstractVector vector_of_column_indices, final Op unary_operator) throws UnsupportedOperationException {
//    new ViewElemCols(this, vector_of_column_indices._data).inPlace(unary_operator);
//  }

  /**
   * Performs an in-place binary operation on the specified columns with the specified right-hand side operand.
   * <p>
   * <b>Note:</b> No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exceptions upon
   * errors.
   * 
   * @param vector_of_column_indices The columns
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void cols(final AbstractVector vector_of_column_indices, final Op binary_operator, final double operand) throws UnsupportedOperationException {
    new ViewElemCols(this, vector_of_column_indices._data).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the specified columns with the specified right-hand side operand.
   * <p>
   * <b>Note:</b> No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exceptions upon
   * errors.
   * 
   * @param vector_of_column_indices The columns
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void cols(final AbstractVector vector_of_column_indices, final Op binary_operator, final AbstractMat operand) throws UnsupportedOperationException {
    new ViewElemCols(this, vector_of_column_indices._data).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the specified rows.
   * <p>
   * <b>Note:</b> No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exceptions upon
   * errors.
   * 
   * @param vector_of_row_indices The rows
   */
  public Mat rows(final AbstractVector vector_of_row_indices) {
    return new Mat(new ViewElemRows(this, vector_of_row_indices._data));
  }

//  /**
//   * Performs an in-place unary operation on the specified rows.
//   * <p>
//   * <b>Note:</b> No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exceptions upon
//   * errors.
//   * 
//   * @param vector_of_row_indices The rows
//   * @param unary_operator The unary operator
//   * 
//   * @throws UnsupportedOperationException Unexpected operator ({@code unary_operator}).
//   */
//  public void rows(final AbstractVector vector_of_row_indices, final Op unary_operator) throws UnsupportedOperationException {
//    new ViewElemRows(this, vector_of_row_indices._data).inPlace(unary_operator);
//  }

  /**
   * Performs an in-place binary operation on the specified rows with the specified right-hand side operand.
   * <p>
   * <b>Note:</b> No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exceptions upon
   * errors.
   * 
   * @param vector_of_row_indices The rows
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void rows(final AbstractVector vector_of_row_indices, final Op binary_operator, final double operand) throws UnsupportedOperationException {
    new ViewElemRows(this, vector_of_row_indices._data).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the specified rows with the specified right-hand side operand.
   * <p>
   * <b>Note:</b> No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exceptions upon
   * errors.
   * 
   * @param vector_of_row_indices The rows
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void rows(final AbstractVector vector_of_row_indices, final Op binary_operator, final AbstractVector operand) throws UnsupportedOperationException {
    new ViewElemRows(this, vector_of_row_indices._data).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the specified rows of the specified columns.
   * <p>
   * <b>Note:</b> No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exceptions upon
   * errors.
   * 
   * @param vector_of_row_indices The rows
   * @param vector_of_column_indices The columns
   */
  public Mat submat(final AbstractVector vector_of_row_indices, final AbstractVector vector_of_column_indices) {
    return new Mat(new ViewElemSubMat(this, vector_of_row_indices._data, vector_of_column_indices._data));
  }

//  /**
//   * Performs an in-place unary operation on the specified rows of the specified columns.
//   * <p>
//   * <b>Note:</b> No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exceptions upon
//   * errors.
//   * 
//   * @param vector_of_row_indices The rows
//   * @param vector_of_column_indices The columns
//   * @param unary_operator The unary operator
//   * 
//   * @throws UnsupportedOperationException Unexpected operator ({@code unary_operator}).
//   */
//  public void submat(final AbstractVector vector_of_row_indices, final AbstractVector vector_of_column_indices, final Op unary_operator) throws UnsupportedOperationException {
//    new ViewElemSubMat(this, vector_of_row_indices._data, vector_of_column_indices._data).inPlace(unary_operator);
//  }

  /**
   * Performs an in-place binary operation on the specified rows of the specified columns with the specified right-hand
   * side operand.
   * <p>
   * <b>Note:</b> No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exceptions upon
   * errors.
   * 
   * @param vector_of_row_indices The rows
   * @param vector_of_column_indices The columns
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void submat(final AbstractVector vector_of_row_indices, final AbstractVector vector_of_column_indices, final Op binary_operator, final double operand) throws UnsupportedOperationException {
    new ViewElemSubMat(this, vector_of_row_indices._data, vector_of_column_indices._data).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the specified rows of the specified columns with the specified right-hand
   * side operand.
   * <p>
   * <b>Note:</b> No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exceptions upon
   * errors.
   * 
   * @param vector_of_row_indices The rows
   * @param vector_of_column_indices The columns
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void submat(final AbstractVector vector_of_row_indices, final AbstractVector vector_of_column_indices, final Op binary_operator, final AbstractMat operand) throws UnsupportedOperationException {
    new ViewElemSubMat(this, vector_of_row_indices._data, vector_of_column_indices._data).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the whole matrix with the specified right-hand side operand.
   * 
   * @param unary_operator The unary operator
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code unary_operator}).
   */
  public void inPlace(final Op unary_operator) {
    switch (unary_operator) {
      case INCREMENT:
        for (int n = 0; n < n_elem; n++) {
          _data[n]++;
        }
        break;
      case DECREMENT:
        for (int n = 0; n < n_elem; n++) {
          _data[n]--;
        }
        break;
      default:
        throw new UnsupportedOperationException("Unsupported operation (" + unary_operator + ").");
    }
  }

  /**
   * Performs an in-place binary operation on the whole matrix with the specified right-hand side operand.
   * 
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   */
  public void inPlace(final Op binary_operator, final double rightHandOperand) throws UnsupportedOperationException {
    switch (binary_operator) {
      case EQUAL:
        set_size(1);
        _data[0] = rightHandOperand;
        break;
      case PLUS:
        for (int n = 0; n < n_elem; n++) {
          _data[n] += rightHandOperand;
        }
        break;
      case MINUS:
        for (int n = 0; n < n_elem; n++) {
          _data[n] -= rightHandOperand;
        }
        break;
      case TIMES:
        for (int n = 0; n < n_elem; n++) {
          _data[n] *= rightHandOperand;
        }
        break;
      case DIVIDE:
        for (int n = 0; n < n_elem; n++) {
          _data[n] /= rightHandOperand;
        }
        break;
      default:
        throw new UnsupportedOperationException("Unsupported operation (" + binary_operator + ").");
    }
  }

  /**
   * Performs an in-place binary operation on the whole matrix with the specified right-hand side operand.
   * <p>
   * <b>Note:</b> Both matrices must have the same shape if used with Op.PLUS, Op.MINUS, Op.ELEMTIMES or Op.ELEMDIVIDE.
   * <br>
   * The number of columns of first matrix must be equal to the number of rows of the second matrix if used with
   * Op.TIMES. <br>
   * Nothing about the shape of both matrices is required if used with Op.EQUAL.
   * 
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws UnsupportedOperationException Unexpected operator ({@code binary_operator}).
   * @throws RuntimeException Both matrices ({@code n_rows}, {@code n_cols} and {@code rightHandOperand.n_rows},
   *           {@code rightHandOperand.n_cols}) must have the same shape.
   * @throws RuntimeException The number of columns ({@code n_cols}) must be equal to the number of rows (
   *           {@code rightHandOperand.n_rows}) in the specified multiplier.
   */
  public void inPlace(final Op binary_operator, final AbstractMat rightHandOperand) throws UnsupportedOperationException, RuntimeException {
    switch (binary_operator) {
      case EQUAL:
        copy_size(rightHandOperand);
        System.arraycopy(rightHandOperand._data, 0, _data, 0, rightHandOperand.n_elem);
        break;
      case PLUS:
        if (n_rows != rightHandOperand.n_rows || n_cols != rightHandOperand.n_cols) {
          throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + rightHandOperand.n_rows + ", " + rightHandOperand.n_cols + ") must have the same shape.");
        }

        for (int n = 0; n < n_elem; n++) {
          _data[n] += rightHandOperand._data[n];
        }
        break;
      case MINUS:
        if (n_rows != rightHandOperand.n_rows || n_cols != rightHandOperand.n_cols) {
          throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + rightHandOperand.n_rows + ", " + rightHandOperand.n_cols + ") must have the same shape.");
        }

        for (int n = 0; n < n_elem; n++) {
          _data[n] -= rightHandOperand._data[n];
        }
        break;
      case TIMES:
        /*
         * The parameter "rightHandOperand" is validated within times(AbstractMat).
         */
        AbstractMat result = times(rightHandOperand);
        copy_size(result);
        System.arraycopy(result._data, 0, _data, 0, result.n_elem);
        break;
      case ELEMTIMES:
        if (n_rows != rightHandOperand.n_rows || n_cols != rightHandOperand.n_cols) {
          throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + rightHandOperand.n_rows + ", " + rightHandOperand.n_cols + ") must have the same shape.");
        }

        for (int n = 0; n < n_elem; n++) {
          _data[n] *= rightHandOperand._data[n];
        }
        break;
      case ELEMDIVIDE:
        if (n_rows != rightHandOperand.n_rows || n_cols != rightHandOperand.n_cols) {
          throw new RuntimeException("Both matrices (" + n_rows + ", " + n_cols + " and " + rightHandOperand.n_rows + ", " + rightHandOperand.n_cols + ") must have the same shape.");
        }

        for (int n = 0; n < n_elem; n++) {
          _data[n] /= rightHandOperand._data[n];
        }
        break;
      default:
        throw new UnsupportedOperationException("Unsupported operation (" + binary_operator + ").");
    }
  }

  /**
   * Swaps the content of this matrix with another one.
   * 
   * @param X The matrix
   * 
   * @throws RuntimeException The content of column vectors can only be swapped with matrices that are equivalent in
   *           shape to a column vector.
   * @throws RuntimeException The content of row vectors can only be swapped with matrices that are equivalent in shape
   *           to a row vector.
   */
  abstract public void swap(final Mat X) throws RuntimeException;

  /**
   * Swaps the content of this matrix with another one.
   * 
   * @param X The column vector
   * 
   * @throws RuntimeException The content of column vectors can only be swapped with matrices that are equivalent in
   *           shape to a column vector.
   * @throws RuntimeException The content of row vectors can only be swapped with matrices that are equivalent in shape
   *           to a row vector.
   */
  abstract public void swap(final Col X) throws RuntimeException;

  /**
   * Swaps the content of this matrix with another one.
   * 
   * @param X The row vector
   * 
   * @throws RuntimeException The content of column vectors can only be swapped with matrices that are equivalent in
   *           shape to a column vector.
   * @throws RuntimeException The content of row vectors can only be swapped with matrices that are equivalent in shape
   *           to a row vector.
   */
  abstract public void swap(final Row X) throws RuntimeException;

  /**
   * Swaps the content of the {@code row1}th row with the {@code row2}th.
   * 
   * @param row1 The first row
   * @param row2 The second row
   * 
   * @throws IndexOutOfBoundsException The first specified row ({@code row1}) is out of bounds.
   * @throws IndexOutOfBoundsException The first specified row ({@code row2}) is out of bounds.
   */
  public void swap_rows(final int row1, final int row2) throws IndexOutOfBoundsException {
    if (!in_range(row1)) {
      throw new IndexOutOfBoundsException("The first row (" + row1 + ") is out of bounds.");
    }

    if (!in_range(row2)) {
      throw new IndexOutOfBoundsException("The second row (" + row2 + ") is out of bounds.");
    }

    Row copyOfRow1 = row(row1);
    ViewSubRow viewOfRow2 = new ViewSubRow(this, row2);

    new ViewSubRow(this, row1).inPlace(Op.EQUAL, viewOfRow2);
    viewOfRow2.inPlace(Op.EQUAL, copyOfRow1);
  }

  /**
   * Swaps the content of the {@code col1}th column with the {@code col2}th.
   * 
   * @param col1 The first column
   * @param col2 The second column
   * 
   * @throws IndexOutOfBoundsException The first column ({@code col1}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column ({@code col2}) is out of bounds.
   */
  public void swap_cols(final int col1, final int col2) throws IndexOutOfBoundsException {
    if (!in_range(col1)) {
      throw new IndexOutOfBoundsException("The first column (" + col1 + ") is out of bounds.");
    }

    if (!in_range(col2)) {
      throw new IndexOutOfBoundsException("The second column (" + col2 + ") is out of bounds.");
    }

    double[] temp = Arrays.copyOfRange(_data, n_rows * col1, n_rows * (col1 + 1));
    System.arraycopy(_data, n_rows * col2, _data, n_rows * col1, n_rows);
    System.arraycopy(temp, 0, _data, n_rows * col2, n_rows);
  }

  /**
   * Returns the transpose.
   */
  abstract public AbstractMat t();

  /**
   * Sets the size to be the same as {@code A}.
   * 
   * @param A The matrix
   * 
   * @throws RuntimeException Column vectors can only copy the size of matrices equivalent in shape to a column vector.
   * @throws RuntimeException Row vectors can only copy the size of matrices equivalent in shape to a row vector.
   */
  abstract public void copy_size(final AbstractMat A) throws RuntimeException;

  protected static void plus(final double[] result, final double[] leftHandOperand, final double rightHandOperand) {
    for (int n = 0; n < leftHandOperand.length; n++) {
      result[n] = leftHandOperand[n] + rightHandOperand;
    }
  }

  /**
   * Returns the out-of-place addition with the specified right-hand side addend.
   * 
   * @param X The addend
   */
  abstract public AbstractMat plus(final double X);

  protected static void plus(final double[] result, final double[] leftHandOperand, final double[] rightHandOperand) {
    for (int n = 0; n < leftHandOperand.length; n++) {
      result[n] = leftHandOperand[n] + rightHandOperand[n];
    }
  }

  /**
   * Returns the out-of-place addition with the provided right-hand side addend.
   * 
   * @param X The addend
   * 
   * @throws RuntimeException Both matrices ({@code n_rows}, {@code n_cols} and {@code X.n_rows}, {@code X.n_cols}) must
   *           have the same shape.
   */
  abstract public AbstractMat plus(final AbstractMat X) throws RuntimeException;

  protected static void minus(final double[] result, final double[] leftHandOperand, final double rightHandOperand) {
    for (int n = 0; n < leftHandOperand.length; n++) {
      result[n] = leftHandOperand[n] - rightHandOperand;
    }
  }

  /**
   * Returns the out-of-place subtraction with the specified right-hand side subtrahend.
   * 
   * @param X The subtrahend
   */
  abstract public AbstractMat minus(final double X);

  protected static void minus(final double[] result, final double[] leftHandOperand, final double[] rightHandOperand) {
    for (int n = 0; n < leftHandOperand.length; n++) {
      result[n] = leftHandOperand[n] - rightHandOperand[n];
    }
  }

  /**
   * Returns the out-of-place subtraction with the provided right-hand side subtrahend.
   * 
   * @param X The subtrahend
   * 
   * @throws RuntimeException Both matrices ({@code n_rows}, {@code n_cols} and {@code X.n_rows}, {@code X.n_cols}) must
   *           have the same shape.
   */
  abstract public AbstractMat minus(final AbstractMat X) throws RuntimeException;

  protected static void divide(final double[] result, final double[] leftHandOperand, final double rightHandOperand) {
    for (int n = 0; n < leftHandOperand.length; n++) {
      result[n] = leftHandOperand[n] / rightHandOperand;
    }
  }

  /**
   * Returns the out-of-place element-wise division with the specified right-hand side divisor.
   * 
   * @param X The divisor
   */
  abstract public AbstractMat divide(final double X);

  protected static void elemDivide(final double[] result, final double[] leftHandOperand, final double[] rightHandOperand) {
    for (int n = 0; n < leftHandOperand.length; n++) {
      result[n] = leftHandOperand[n] / rightHandOperand[n];
    }
  }

  /**
   * Returns the out-of-place element-wise division with the provided right-hand side divisor.
   * 
   * @param X The divisor
   * 
   * @throws RuntimeException Both matrices ({@code n_rows}, {@code n_cols} and {@code X.n_rows}, {@code X.n_cols}) must
   *           have the same shape.
   */
  abstract public AbstractMat elemDivide(final AbstractMat X) throws RuntimeException;

  protected static void times(final double[] result, final double[] leftHandOperand, final double rightHandOperand) {
    for (int n = 0; n < leftHandOperand.length; n++) {
      result[n] = leftHandOperand[n] * rightHandOperand;
    }
  }
  
  /**
   * Return the out-of-place matrix multiplication with the specified right-hand side multiplier.
   * 
   * @param X The multiplier
   * 
   * @throws RuntimeException The number of columns ({@code n_cols}) must be equal to the number of rows (
   *           {@code X.n_rows}) in the specified multiplier.
   */
  abstract public AbstractMat times(final double X) throws RuntimeException;

  abstract protected AbstractMat times(final AbstractMat X) throws RuntimeException;

  /**
   * Return the out-of-place matrix multiplication with the provided right-hand side multiplier.
   * 
   * @param X The multiplier
   * 
   * @throws RuntimeException The number of columns ({@code n_cols}) must be equal to the number of rows (
   *           {@code X.n_rows}) in the specified multiplier.
   */
  abstract public AbstractMat times(final Col X) throws RuntimeException;

  /**
   * Return the out-of-place matrix multiplication with the provided right-hand side multiplier.
   * 
   * @param X The multiplier
   * 
   * @throws RuntimeException The number of columns ({@code n_cols}) must be equal to the number of rows (
   *           {@code X.n_rows}) in the specified multiplier.
   */
  abstract public AbstractMat times(final Row X) throws RuntimeException;

  /**
   * Return the out-of-place matrix multiplication with the provided right-hand side multiplier.
   * 
   * @param X The multiplier
   * 
   * @throws RuntimeException The number of columns ({@code n_cols}) must be equal to the number of rows (
   *           {@code X.n_rows}) in the specified multiplier.
   */
  abstract public AbstractMat times(final Mat X) throws RuntimeException;

  protected static void elemTimes(final double[] result, final double[] leftHandOperand, final double[] rightHandOperand) {
    for (int n = 0; n < leftHandOperand.length; n++) {
      result[n] = leftHandOperand[n] * rightHandOperand[n];
    }
  }

  /**
   * Returns the out-of-place element-wise multiplication with the provided right-hand side multiplier.
   * 
   * @param X The multiplier
   * 
   * @throws RuntimeException Both matrices ({@code n_rows}, {@code n_cols} and {@code X.n_rows}, {@code X.n_cols}) must
   *           have the same shape.
   */
  abstract public AbstractMat elemTimes(final AbstractMat X) throws RuntimeException;

  protected static void equals(final double[] result, final double[] leftHandOperand, final double rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < leftHandOperand.length; n++) {
      if (leftHandOperand[n] == rightHandOperand) {
        result[n] = 1;
      }
    }
  }

  /**
   * Returns the out-of-place element-wise equality evaluation with the specified right-hand side operand.
   * <p>
   * The returned matrix will be set to 1 for each positions where the left-hand side value is equal to the right-hand
   * side and 0 otherwise.
   * 
   * @param X The operand
   */
  abstract public AbstractMat equals(final double X);

  protected static void equals(final double[] result, final double[] leftHandOperand, final double[] rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < leftHandOperand.length; n++) {
      if (leftHandOperand[n] == rightHandOperand[n]) {
        result[n] = 1;
      }
    }
  }

  /**
   * Returns the out-of-place element-wise equality evaluation with the provided right-hand side operand.
   * <p>
   * The returned matrix will be set to 1 for each positions where the left-hand side value is equal to the right-hand
   * side and 0 otherwise.
   * 
   * @param X The operand
   * 
   * @throws RuntimeException Both matrices ({@code n_rows}, {@code n_cols} and {@code X.n_rows}, {@code X.n_cols}) must
   *           have the same shape.
   */
  abstract public AbstractMat equals(final AbstractMat X) throws RuntimeException;

  protected static void nonEquals(final double[] result, final double[] leftHandOperand, final double rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < leftHandOperand.length; n++) {
      if (leftHandOperand[n] != rightHandOperand) {
        result[n] = 1;
      }
    }
  }

  /**
   * Returns the out-of-place element-wise non-equality evaluation with the specified right-hand side operand.
   * <p>
   * The returned matrix will be set to 1 for each positions where the left-hand side value is non-equal to the
   * right-hand side and 0 otherwise.
   * 
   * @param X The operand
   */
  abstract public AbstractMat nonEquals(final double X);

  protected static void nonEquals(final double[] result, final double[] leftHandOperand, final double[] rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < leftHandOperand.length; n++) {
      if (leftHandOperand[n] != rightHandOperand[n]) {
        result[n] = 1;
      }
    }
  }

  /**
   * Returns the out-of-place element-wise non-equality evaluation with the provided right-hand side operand.
   * <p>
   * The returned matrix will be set to 1 for each positions where the left-hand side value is non-equal to the
   * right-hand side and 0 otherwise.
   * 
   * @param X The operand
   * 
   * @throws RuntimeException Both matrices ({@code n_rows}, {@code n_cols} and {@code X.n_rows}, {@code X.n_cols}) must
   *           have the same shape.
   */
  abstract public AbstractMat nonEquals(final AbstractMat X) throws RuntimeException;

  protected static void greaterThan(final double[] result, final double[] leftHandOperand, final double rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < leftHandOperand.length; n++) {
      if (leftHandOperand[n] >= rightHandOperand) {
        result[n] = 1;
      }
    }
  }

  /**
   * Returns the out-of-place element-wise <em>greater than</em> evaluation with the specified right-hand side operand.
   * <p>
   * The returned matrix will be set to 1 for each positions where the left-hand side value is greater than the
   * right-hand side and 0 otherwise.
   * 
   * @param X The operand
   */
  abstract public AbstractMat greaterThan(final double X);

  protected static void greaterThan(final double[] result, final double[] leftHandOperand, final double[] rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < leftHandOperand.length; n++) {
      if (leftHandOperand[n] >= rightHandOperand[n]) {
        result[n] = 1;
      }
    }
  }

  /**
   * Returns the out-of-place element-wise <em>greater than</em> evaluation with the provided right-hand side operand.
   * <p>
   * The returned matrix will be set to 1 for each positions where the left-hand side value is greater than the
   * right-hand side and 0 otherwise.
   * 
   * @param X The operand
   * 
   * @throws RuntimeException Both matrices ({@code n_rows}, {@code n_cols} and {@code X.n_rows}, {@code X.n_cols}) must
   *           have the same shape.
   */
  abstract public AbstractMat greaterThan(final AbstractMat X) throws RuntimeException;

  protected static void lessThan(final double[] result, final double[] leftHandOperand, final double rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < leftHandOperand.length; n++) {
      if (leftHandOperand[n] <= rightHandOperand) {
        result[n] = 1;
      }
    }
  }

  /**
   * Returns the out-of-place element-wise <em>less than</em> evaluation with the specified right-hand side operand.
   * <p>
   * The returned matrix will be set to 1 for each positions where the left-hand side value is less than the right-hand
   * side and 0 otherwise.
   * 
   * @param X The operand
   */
  abstract public AbstractMat lessThan(final double X);

  protected static void lessThan(final double[] result, final double[] leftHandOperand, final double[] rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < leftHandOperand.length; n++) {
      if (leftHandOperand[n] <= rightHandOperand[n]) {
        result[n] = 1;
      }
    }
  }

  /**
   * Returns the out-of-place element-wise <em>less than</em> evaluation with the provided right-hand side operand.
   * <p>
   * The returned matrix will be set to 1 for each positions where the left-hand side value is less than the right-hand
   * side and 0 otherwise.
   * 
   * @param X The operand
   * 
   * @throws RuntimeException Both matrices ({@code n_rows}, {@code n_cols} and {@code X.n_rows}, {@code X.n_cols}) must
   *           have the same shape.
   */
  abstract public AbstractMat lessThan(final AbstractMat X) throws RuntimeException;

  protected static void strictGreaterThan(final double[] result, final double[] leftHandOperand, final double rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < leftHandOperand.length; n++) {
      if (leftHandOperand[n] > rightHandOperand) {
        result[n] = 1;
      }
    }
  }

  /**
   * Returns the out-of-place element-wise <em>strict greater than</em> evaluation with the specified right-hand side
   * operand.
   * <p>
   * The returned matrix will be set to 1 for each positions where the left-hand side value is strict greater than the
   * right-hand side and 0 otherwise.
   * 
   * @param X The operand
   */
  abstract public AbstractMat strictGreaterThan(final double X);

  protected static void strictGreaterThan(final double[] result, final double[] leftHandOperand, final double[] rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < leftHandOperand.length; n++) {
      if (leftHandOperand[n] >= rightHandOperand[n]) {
        result[n] = 1;
      }
    }
  }

  /**
   * Returns the out-of-place element-wise <em>strict greater than</em> evaluation with the provided right-hand side
   * operand.
   * <p>
   * The returned matrix will be set to 1 for each positions where the left-hand side value is strict greater than the
   * right-hand side and 0 otherwise.
   * 
   * @param X The operand
   * 
   * @throws RuntimeException Both matrices ({@code n_rows}, {@code n_cols} and {@code X.n_rows}, {@code X.n_cols}) must
   *           have the same shape.
   */
  abstract public AbstractMat strictGreaterThan(final AbstractMat X) throws RuntimeException;

  protected static void strictLessThan(final double[] result, final double[] leftHandOperand, final double rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < leftHandOperand.length; n++) {
      if (leftHandOperand[n] < rightHandOperand) {
        result[n] = 1;
      }
    }
  }

  /**
   * Returns the out-of-place element-wise <em>strict less than</em> evaluation with the specified right-hand side
   * operand.
   * <p>
   * The returned matrix will be set to 1 for each positions where the left-hand side value is strict less than the
   * right-hand side and 0 otherwise.
   * 
   * @param X The operand
   */
  abstract public AbstractMat strictLessThan(final double X);

  protected static void strictLessThan(final double[] result, final double[] leftHandOperand, final double[] rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < leftHandOperand.length; n++) {
      if (leftHandOperand[n] < rightHandOperand[n]) {
        result[n] = 1;
      }
    }
  }

  /**
   * Returns the out-of-place element-wise <em>strict less than</em> evaluation with the provided right-hand side
   * operand.
   * <p>
   * The returned matrix will be set to 1 for each positions where the left-hand side value is strict less than the
   * right-hand side and 0 otherwise.
   * 
   * @param X The operand
   * 
   * @throws RuntimeException Both matrices ({@code n_rows}, {@code n_cols} and {@code X.n_rows}, {@code X.n_cols}) must
   *           have the same shape.
   */
  abstract public AbstractMat strictLessThan(final AbstractMat X) throws RuntimeException;

  @Override
  public String toString() {
    String output = "(" + n_rows + ", " + n_cols + ")-matrix: [" + System.lineSeparator();
    for (int i = 0; i < n_rows; i++) {
      output += " ";

      for (int j = 0; j < n_cols; j++) {
        double value = _data[i + j * n_rows];
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
      output += System.lineSeparator();
    }
    output += "]";

    return output;
  }

  abstract protected void set_size(final int n_elem);

  /**
   * Returns {@code true} if the matrix has only one row or column.
   */
  protected boolean is_vec() {
    return (is_colvec() || is_rowvec());
  }

  /**
   * Returns {@code true} if the matrix has only one column.
   */
  protected boolean is_colvec() {
    return (n_cols == 1);
  }

  /**
   * Returns {@code true} if the matrix has only one row.
   */
  protected boolean is_rowvec() {
    return (n_rows == 1);
  }
}
