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
   * The number of elements (same as {@code n_rows * n_cols}) .
   */
  public int         n_elem;

  /**
   * Returns the value of the {@code n}th element.
   * 
   * @param n The position
   */
  public double at(int n) {
    return _data[n];
  }

  /**
   * Performs an in-place unary operation on the {@code n}th element.
   * 
   * @param n The position
   * @param unary_operator The unary operator
   * @param operand The operand
   */
  public void at(int n, Op unary_operator) {
    switch (unary_operator) {
      case NEGATE:
        _data[n] = -_data[n];
        break;
      case INCREMENT:
        _data[n]++;
        break;
      case DECREMENT:
        _data[n]--;
        break;
      default:
        throw new UnsupportedOperationException("Internal Error: Unsupported operation.");
    }
  }

  /**
   * Performs an in-place binary operation on the {@code n}th element with the specified right-hand side operand.
   * 
   * @param n The position
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void at(int n, Op binary_operator, double operand) {
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
      case ELEMTIMES:
        _data[n] *= operand;
        break;
      case ELEMDIVIDE:
        _data[n] /= operand;
        break;
      default:
        throw new UnsupportedOperationException("Internal Error: Unsupported operation.");
    }
  }

  /**
   * Returns true if the position is not out of bounds.
   * 
   * @param n The position
   */
  public boolean in_range(int n) {
    return (n > -1 && n < n_elem);
  }

  /**
   * Returns true if neither position within the span is out of bounds.
   * 
   * @param span The span
   */
  public boolean in_range(Span span) {
    return (span._isEntireRange || (span._first > -1 && span._last < n_elem));
  }

  /**
   * Returns true if neither the row nor the column position is out of bound.
   * 
   * @param row The row position
   * @param col The column position
   */
  public boolean in_range(int row, int col) {
    return ((row > -1 && row < n_rows) && (col > -1 && col < n_cols));
  }

  /**
   * Returns true if neither position within the row and column span is out of bounds.
   * 
   * @param rowSpan The row span
   * @param colSpan The column span
   */
  public boolean in_range(Span row_span, Span col_span) {
    return ((row_span._isEntireRange || (row_span._first > -1 && row_span._last < n_rows)) && (col_span._isEntireRange || (col_span._first > -1 && col_span._last < n_cols)));
  }

  /**
   * Returns true if neither position from {@code first_row} to {@code first_row + size.n_rows - 1} and
   * {@code first_col} to {@code first_col + size.n_cols - 1} is out of bounds.
   * 
   * @param first_row The first row position
   * @param first_col The first column position
   * @param size The size of a matrix or field
   */
  public boolean in_range(int first_row, int first_col, Size size) {
    return ((first_row > -1 && first_row + size.n_rows - 1 < n_rows) && (first_col > -1 && first_col + size.n_cols - 1 < n_cols));
  }

  /**
   * Returns true if the matrix has no elements.
   */
  public boolean is_empty() {
    return (n_elem == 0);
  }

  /**
   * Returns true if the matrix contains only finite values.
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
   * @throws RuntimeException The matrix must have at least one element.
   */
  public double min() throws RuntimeException {
    if (is_empty()) {
      throw new RuntimeException("The matrix must have at least one element.");
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
   * <b>Note:</b> Unfortunately, the position variable must be of the mutable type int[].
   * 
   * @param index_of_min_val The position storage
   * 
   * @throws RuntimeException The matrix must have at least one element.
   */
  public double min(int[] index_of_min_val) throws RuntimeException {
    if (is_empty()) {
      throw new RuntimeException("The matrix must have at least one element.");
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
   * @throws RuntimeException The matrix must have at least one element.
   */
  public double max() throws RuntimeException {
    if (is_empty()) {
      throw new RuntimeException("The matrix must have at least one element.");
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
   * <b>Note:</b> Unfortunately, the position variable must be of the mutable type int[].
   * 
   * @param index_of_max_val The position storage
   * 
   * @throws RuntimeException The matrix must have at least one element.
   */
  public double max(int[] index_of_max_val) throws RuntimeException {
    if (is_empty()) {
      throw new RuntimeException("The matrix must have at least one element.");
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
  public void fill(double value) {
    Arrays.fill(_data, value);
  }

  /**
   * Prints the matrix to System.out.
   */
  public void print() {
    print("");
  }

  /**
   * Prints the matrix unformatted to System.out, with a specified header.
   * 
   * @param header The header
   */
  public void print(String header) {
    print(System.out, header);
  }

  /**
   * Prints the matrix to a specified stream.
   * 
   * @param OutputStream stream
   */
  public void print(OutputStream stream) {
    print(stream, "");
  }

  /**
   * Prints the matrix to a specified stream, with a specified header.
   * 
   * @param OutputStream stream
   * @param header The header
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   */
  public void print(OutputStream stream, String header) throws AssertionError {
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
   */
  public void raw_print() {
    print("");
  }

  /**
   * Prints the matrix unformatted to System.out, with a specified header.
   * 
   * @param header The header
   */
  public void raw_print(String header) {
    print(System.out, "");
  }

  /**
   * Prints the matrix unformatted to a specified stream.
   * 
   * @param OutputStream stream
   */
  public void raw_print(OutputStream stream) {
    print(stream, "");
  }

  /**
   * Prints the matrix unformatted to a specified stream, with a specified header.
   * 
   * @param OutputStream stream
   * @param header The header
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   */
  public void raw_print(OutputStream stream, String header) {
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

        writer.println();
      }
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
   * Stores the matrix at the specified file path and returns true on success.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param name The file path
   * 
   * @see FileType
   */
  public boolean save(String name) throws FileNotFoundException {
    return save(name, FileType.RAW_ASCII);
  }

  /**
   * Stores the matrix at the specified file path in the requested file format and returns true on success.
   * 
   * @param name The file path
   * @param file_type The file format
   * 
   * @see FileType
   */
  public boolean save(String name, FileType file_type) throws FileNotFoundException {
    return save(new FileOutputStream(name, false), file_type);
  }

  /**
   * Stores the matrix at the specified stream and returns true on success.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param stream The stream
   * 
   * @see FileType
   */
  public boolean save(OutputStream stream) {
    return save(stream, FileType.RAW_ASCII);
  }

  /**
   * Stores the matrix at the specified stream in the requested file format and returns true on success.
   * 
   * @param stream The stream
   * @param file_type The file format
   * 
   * @see FileType
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   */
  public boolean save(OutputStream stream, FileType file_type) throws AssertionError {
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

        writer.println();
      }
    }

    writer.close();

    return true;
  }

  /**
   * Fills the matrix with data from the specified file path and returns true on success.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param name The file path
   */
  public boolean load(String name) throws FileNotFoundException, IOException {
    return load(name, FileType.RAW_ASCII);
  }

  /**
   * Fills the matrix with data expected to be in specified format from the specified file path and returns true
   * on success.
   * 
   * @param name The file path
   * @param file_type The file format
   */
  public boolean load(String name, FileType file_type) throws FileNotFoundException, IOException {
    return load(new FileInputStream(name), file_type);
  }

  /**
   * Fills the matrix with data from the specified stream and returns true on success.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param stream The stream
   */
  public boolean load(InputStream stream) throws IOException {
    return load(stream, FileType.RAW_ASCII);
  }

  /**
   * Fills the matrix with data expected to be in specified format from the specified stream and returns true on
   * success.
   * 
   * @param name The stream
   * @param file_type The file format
   * 
   * @throws AssertionError UTF-8 not supported by your JVM.
   * @throws IllegalArgumentException All columns must have the same length.
   */
  public boolean load(InputStream stream, FileType file_type) throws AssertionError, IllegalArgumentException, IOException {
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
    int numberOfRows = matrix.size();

    n_cols = numberOfColumns;
    n_rows = numberOfRows;
    n_elem = n_rows * n_cols;
    _data = new double[n_elem];

    int n = 0;
    for (int i = 0; i < n_rows; i++) {
      for (int j = 0; j < n_cols; j++) {
        _data[n++] = matrix.get(i)[j];
      }
    }
    reader.close();

    return true;
  }

  /**
   * Same as {@link #save(String)} but without error reporting.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param name The file name
   */
  public boolean quiet_save(String name) {
    return quiet_save(name, FileType.RAW_ASCII);
  }

  /**
   * Same as {@link #save(String, FileType)} but without error reporting.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param name The file name
   * @param file_type The file format
   */
  public boolean quiet_save(String name, FileType file_type) {
    try {
      return save(new FileOutputStream(name, false), file_type);
    } catch(FileNotFoundException e) {
      return false;
    }
  }

  /**
   * Same as {@link #save(OutputStream)} but without error reporting.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param stream The stream
   */
  public boolean quiet_save(OutputStream stream) {
    return quiet_save(stream, FileType.RAW_ASCII);
  }

  /**
   * Same as {@link #save(OutputStream, FileType)} but without error reporting.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param stream The stream
   * @param file_type The file format
   */
  public boolean quiet_save(OutputStream stream, FileType file_type) {
    try {
      return save(stream, file_type);
    } catch(Exception e) {
      return false;
    }
  }

  /**
   * Same as {@link #load(String)} but without error reporting.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param name The file path
   * @param file_type The file format
   */
  public boolean quiet_load(String name) {
    return quiet_load(name, FileType.RAW_ASCII);
  }

  /**
   * Same as {@link #load(String, FileType)} but without error reporting.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param name The file path
   * @param file_type The file format
   */
  public boolean quiet_load(String name, FileType file_type) {
    try {
      return quiet_load(new FileInputStream(name), file_type);
    } catch(FileNotFoundException e) {
      return false;
    }
  }

  /**
   * Same as {@link #load(InputStream)} but without error reporting.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param stream The stream
   * @param file_type The file format
   */
  public boolean quiet_load(InputStream stream) {
    return quiet_load(stream, FileType.RAW_ASCII);
  }

  /**
   * Same as {@link #load(InputStream, FileType)} but without error reporting.
   * <p>
   * <b>Non-canonical:</b> The default file format is {@code FileType.RAW_ASCII}.
   * 
   * @param stream The stream
   * @param file_type The file format
   */
  public boolean quiet_load(InputStream stream, FileType file_type) {
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
    _data = new double[0];
    n_rows = 0;
    n_cols = 0;
    n_elem = 0;
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
   * Returns true if the matrix has no elements.
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
   * @param col_number The column position
   * 
   * @throws IndexOutOfBoundsException The column position ({@code col_number}) is out of bounds.
   */
  abstract public AbstractMat col(int col_number) throws IndexOutOfBoundsException;

  /**
   * Performs an in-place unary operation on the {@code col_number}th column.
   * 
   * @param col_number The column position
   * @param unary_operator The unary operator
   * 
   * @throws IndexOutOfBoundsException The column position ({@code col_number}) is out of bounds.
   */
  public void col(int col_number, Op unary_operator) throws IndexOutOfBoundsException {
    if (col_number != 0) {
      throw new IndexOutOfBoundsException("The column position (" + col_number + ") is out of bounds.");
    }

    new ViewSubCol(this, col_number).inPlace(unary_operator);
  }

  /**
   * Performs an in-place binary operation on the {@code col_number}th column with the specified right-hand side
   * operand.
   * 
   * @param col_number The column position
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The column position ({@code col_number}) is out of bounds.
   */
  public void col(int col_number, Op binary_operator, double operand) throws IndexOutOfBoundsException {
    if (col_number != 0) {
      throw new IndexOutOfBoundsException("The column position (" + col_number + ") is out of bounds.");
    }

    new ViewSubCol(this, col_number).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the {@code col_number}th column with the specified right-hand side
   * operand.
   * 
   * @param col_number The column position
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The column position ({@code col_number}) is out of bounds.
   */
  public void col(int col_number, Op binary_operator, AbstractMat operand) throws IndexOutOfBoundsException {
    if (col_number != 0) {
      throw new IndexOutOfBoundsException("The column position (" + col_number + ") is out of bounds.");
    }

    new ViewSubCol(this, col_number).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the {@code row_number}th row.
   * 
   * @param col_number The column position
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   */
  abstract public AbstractMat row(int row_number) throws IndexOutOfBoundsException;

  /**
   * Performs an in-place unary operation on the {@code row_number}th row.
   * 
   * @param row_number The row position
   * @param unary_operator The unary operator
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   */
  public void row(int row_number, Op unary_operator) throws IndexOutOfBoundsException {
    if (!in_range(row_number)) {
      throw new IndexOutOfBoundsException("The row position (" + row_number + ") is out of bounds.");
    }

    new ViewSubRow(this, row_number).inPlace(unary_operator);
  }

  /**
   * Performs an in-place binary operation on the {@code row_number}th row with the specified right-hand side operand.
   * 
   * @param row_number The row position
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   */
  public void row(int row_number, Op binary_operator, double operand) throws IndexOutOfBoundsException {
    if (!in_range(row_number)) {
      throw new IndexOutOfBoundsException("The row position (" + row_number + ") is out of bounds.");
    }

    new ViewSubRow(this, row_number).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the {@code row_number}th row with the specified right-hand side operand.
   * 
   * @param row_number The row position
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   */
  public void row(int row_number, Op binary_operator, AbstractMat operand) throws IndexOutOfBoundsException {
    if (!in_range(row_number)) {
      throw new IndexOutOfBoundsException("The row position (" + row_number + ") is out of bounds.");
    }

    new ViewSubRow(this, row_number).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the {@code first_col}th to {@code last_col} column.
   * 
   * @param first_col The first column position
   * @param last_col The last column position
   * 
   * @throws IndexOutOfBoundsException The first column position ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code last_col}) is out of bounds.
   */
  abstract public AbstractMat cols(int first_col, int last_col) throws IndexOutOfBoundsException;

  /**
   * Performs an in-place unary operation on the {@code first_col}th to {@code last_col} column.
   * 
   * @param first_col The first column position
   * @param last_col The last column position
   * @param unary_operator The unary operator
   * 
   * @throws IndexOutOfBoundsException The first column position ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code last_col}) is out of bounds.
   */
  public void cols(int first_col, int last_col, Op unary_operator) throws IndexOutOfBoundsException {
    if (first_col != 0) {
      throw new IndexOutOfBoundsException("The first column position (" + first_col + ") is out of bounds.");
    }

    if (last_col != 0) {
      throw new IndexOutOfBoundsException("The last column position (" + last_col + ") is out of bounds.");
    }

    new ViewSubCols(this, first_col, last_col).inPlace(unary_operator);
  }

  /**
   * Performs an in-place binary operation on the {@code first_col}th to {@code last_col} column with the specified
   * right-hand side operand.
   * 
   * @param first_col The first column position
   * @param last_col The last column position
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The first column position ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code last_col}) is out of bounds.
   */
  public void cols(int first_col, int last_col, Op binary_operator, double operand) throws IndexOutOfBoundsException {
    if (first_col != 0) {
      throw new IndexOutOfBoundsException("The first column position (" + first_col + ") is out of bounds.");
    }

    if (last_col != 0) {
      throw new IndexOutOfBoundsException("The last column position (" + last_col + ") is out of bounds.");
    }

    new ViewSubCols(this, first_col, last_col).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the {@code first_col}th to {@code last_col} column with the specified
   * right-hand side operand.
   * 
   * @param first_col The first column position
   * @param last_col The last column position
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The first column position ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code last_col}) is out of bounds.
   */
  public void cols(int first_col, int last_col, Op binary_operator, AbstractMat operand) throws IndexOutOfBoundsException {
    if (first_col != 0) {
      throw new IndexOutOfBoundsException("The first column position (" + first_col + ") is out of bounds.");
    }

    if (last_col != 0) {
      throw new IndexOutOfBoundsException("The last column position (" + last_col + ") is out of bounds.");
    }

    new ViewSubCols(this, first_col, last_col).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the {@code first_row}th to {@code last_row} row.
   * 
   * @param first_row The first row position
   * @param last_row The last row position
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code last_row}) is out of bounds.
   */
  abstract public AbstractMat rows(int first_row, int last_row) throws IndexOutOfBoundsException;

  /**
   * Performs an in-place unary operation on the {@code first_row}th to {@code last_row} row.
   * 
   * @param first_row The first row position
   * @param last_row The last row position
   * @param unary_operator The unary operator
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code last_row}) is out of bounds.
   */
  public void rows(int first_row, int last_row, Op unary_operator) throws IndexOutOfBoundsException {
    if (!in_range(first_row)) {
      throw new IndexOutOfBoundsException("The first row position (" + first_row + ") is out of bounds.");
    }

    if (!in_range(last_row)) {
      throw new IndexOutOfBoundsException("The last row position (" + last_row + ") is out of bounds.");
    }

    new ViewSubRows(this, first_row, last_row).inPlace(unary_operator);
  }

  /**
   * Performs an in-place binary operation on the {@code first_row}th to {@code last_row} row with the specified
   * right-hand side operand.
   * 
   * @param first_row The first row position
   * @param last_row The last row position
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code last_row}) is out of bounds.
   */
  public void rows(int first_row, int last_row, Op binary_operator, double operand) throws IndexOutOfBoundsException {
    if (!in_range(first_row)) {
      throw new IndexOutOfBoundsException("The first row position (" + first_row + ") is out of bounds.");
    }

    if (!in_range(last_row)) {
      throw new IndexOutOfBoundsException("The last row position (" + last_row + ") is out of bounds.");
    }

    new ViewSubRows(this, first_row, last_row).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the {@code first_row}th to {@code last_row} row with the specified
   * right-hand side operand.
   * 
   * @param first_row The first row position
   * @param last_row The last row position
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code last_row}) is out of bounds.
   */
  public void rows(int first_row, int last_row, Op binary_operator, AbstractMat operand) throws IndexOutOfBoundsException {
    if (!in_range(first_row)) {
      throw new IndexOutOfBoundsException("The first row position (" + first_row + ") is out of bounds.");
    }

    if (!in_range(last_row)) {
      throw new IndexOutOfBoundsException("The last row position (" + last_row + ") is out of bounds.");
    }

    new ViewSubRows(this, first_row, last_row).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the {@code span._first}th to {@code span._last} row of the {@code col_number}th column.
   * 
   * @param span The span
   * @param col_number The column position
   * 
   * @throws IndexOutOfBoundsException The column position ({@code col_number}) is out of bounds.
   * @throws IndexOutOfBoundsException The first row position ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code span._last}) is out of bounds.
   */
  abstract public AbstractMat col(Span span, int col_number) throws IndexOutOfBoundsException;

  /**
   * Performs an in-place unary operation on the {@code span._first}th to {@code span._last} row of the
   * {@code col_number}th column.
   * 
   * @param span The span
   * @param col_number The column position
   * @param unary_operator The unary operator
   * 
   * @throws IndexOutOfBoundsException The column position ({@code col_number}) is out of bounds.
   * @throws IndexOutOfBoundsException The first row position ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code span._last}) is out of bounds.
   */
  public void col(Span span, int col_number, Op unary_operator) throws IndexOutOfBoundsException {
    if (col_number != 0) {
      throw new IndexOutOfBoundsException("The column position (" + col_number + ") is out of bounds.");
    }

    if (span._isEntireRange) {
      new ViewSubCol(this, col_number).inPlace(unary_operator);
    } else {
      if (span._first != 0) {
        throw new IndexOutOfBoundsException("The first row position (" + span._first + ") is out of bounds.");
      }

      if (span._last != 0) {
        throw new IndexOutOfBoundsException("The last row position (" + span._last + ") is out of bounds.");
      }
      
      new ViewSubCol(this, col_number, span._first, span._last).inPlace(unary_operator);
    }
  }

  /**
   * Performs an in-place binary operation on the {@code span._first}th to {@code span._last} row of the
   * {@code col_number}th column with the specified right-hand side operand.
   * 
   * @param span The span
   * @param col_number The column position
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The column position ({@code col_number}) is out of bounds.
   * @throws IndexOutOfBoundsException The first row position ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code span._last}) is out of bounds.
   */
  public void col(Span span, int col_number, Op binary_operator, double operand) throws IndexOutOfBoundsException {
    if (col_number != 0) {
      throw new IndexOutOfBoundsException("The column position (" + col_number + ") is out of bounds.");
    }

    if (span._isEntireRange) {
      new ViewSubCol(this, col_number).inPlace(binary_operator, operand);
    } else {
      if (span._first != 0) {
        throw new IndexOutOfBoundsException("The first row position (" + span._first + ") is out of bounds.");
      }

      if (span._last != 0) {
        throw new IndexOutOfBoundsException("The last row position (" + span._last + ") is out of bounds.");
      }
      
      new ViewSubCol(this, col_number, span._first, span._last).inPlace(binary_operator, operand);
    }
  }

  /**
   * Performs an in-place binary operation on the {@code span._first}th to {@code span._last} row of the
   * {@code col_number}th column with the specified right-hand side operand.
   * 
   * @param span The span
   * @param col_number The column position
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The column position ({@code col_number}) is out of bounds.
   * @throws IndexOutOfBoundsException The first row position ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code span._last}) is out of bounds.
   */
  public void col(Span span, int col_number, Op binary_operator, AbstractMat operand) throws IndexOutOfBoundsException {
    if (col_number != 0) {
      throw new IndexOutOfBoundsException("The column position (" + col_number + ") is out of bounds.");
    }

    if (span._isEntireRange) {
      new ViewSubCol(this, col_number).inPlace(binary_operator, operand);
    } else {
      if (span._first != 0) {
        throw new IndexOutOfBoundsException("The first row position (" + span._first + ") is out of bounds.");
      }

      if (span._last != 0) {
        throw new IndexOutOfBoundsException("The last row position (" + span._last + ") is out of bounds.");
      }
      
      new ViewSubCol(this, col_number, span._first, span._last).inPlace(binary_operator, operand);
    }
  }

  /**
   * Returns a deep copy of the {@code row_number}th row of the {@code span._first}th to {@code span._last} column.
   * 
   * @param row_number The row position
   * @param span The span
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code span._last}) is out of bounds.
   */
  abstract public AbstractMat row(int row_number, Span span) throws IndexOutOfBoundsException;

  /**
   * Performs an in-place unary operation on the {@code row_number}th row of the {@code span._first}th to
   * {@code span._last} column.
   * 
   * @param row_number The row position
   * @param span The span
   * @param unary_operator The unary operator
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code span._last}) is out of bounds.
   */
  public void row(int row_number, Span span, Op unary_operator) throws IndexOutOfBoundsException {
    if (row_number != 0) {
      throw new IndexOutOfBoundsException("The row position (" + row_number + ") is out of bounds.");
    }

    if (span._isEntireRange) {
      new ViewSubRow(this, row_number).inPlace(unary_operator);
    } else {
      if (span._first != 0) {
        throw new IndexOutOfBoundsException("The first column position (" + span._first + ") is out of bounds.");
      }

      if (span._last != 0) {
        throw new IndexOutOfBoundsException("The last column position (" + span._last + ") is out of bounds.");
      }
      
      new ViewSubRow(this, row_number, span._first, span._last).inPlace(unary_operator);
    }
  }

  /**
   * Performs an in-place binary operation on the {@code row_number}th row of the {@code span._first}th to
   * {@code span._last} column with the specified right-hand side operand.
   * 
   * @param row_number The row position
   * @param span The span
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code span._last}) is out of bounds.
   */
  public void row(int row_number, Span span, Op binary_operator, double operand) throws IndexOutOfBoundsException {
    if (row_number != 0) {
      throw new IndexOutOfBoundsException("The row position (" + row_number + ") is out of bounds.");
    }

    if (span._isEntireRange) {
      new ViewSubRow(this, row_number).inPlace(binary_operator, operand);
    } else {
      if (span._first != 0) {
        throw new IndexOutOfBoundsException("The first column position (" + span._first + ") is out of bounds.");
      }

      if (span._last != 0) {
        throw new IndexOutOfBoundsException("The last column position (" + span._last + ") is out of bounds.");
      }
      
      new ViewSubRow(this, row_number, span._first, span._last).inPlace(binary_operator, operand);
    }
  }

  /**
   * Performs an in-place binary operation on the {@code row_number}th row of the {@code span._first}th to
   * {@code span._last} column with the specified right-hand side operand.
   * 
   * @param row_number The row position
   * @param span The span
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The row position ({@code row_number}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code span._last}) is out of bounds.
   */
  public void row(int row_number, Span span, Op binary_operator, AbstractMat operand) throws IndexOutOfBoundsException {
    if (row_number != 0) {
      throw new IndexOutOfBoundsException("The row position (" + row_number + ") is out of bounds.");
    }

    if (span._isEntireRange) {
      new ViewSubRow(this, row_number).inPlace(binary_operator, operand);
    } else {
      if (span._first != 0) {
        throw new IndexOutOfBoundsException("The first column position (" + span._first + ") is out of bounds.");
      }

      if (span._last != 0) {
        throw new IndexOutOfBoundsException("The last column position (" + span._last + ") is out of bounds.");
      }
      
      new ViewSubRow(this, row_number, span._first, span._last).inPlace(binary_operator, operand);
    }
  }

  /**
   * Returns a deep copy of the {@code first_row}th to {@code last_row} row of the {@code first_col}th to
   * {@code last_col} column.
   * 
   * @param first_row The first row position
   * @param first_col The first column position
   * @param last_row The last row position
   * @param last_col The last column position
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code last_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code last_col}) is out of bounds.
   */
  abstract public AbstractMat submat(int first_row, int first_col, int last_row, int last_col) throws IndexOutOfBoundsException;

  /**
   * Performs an in-place unary operation on the {@code first_row}th to {@code last_row} row of the {@code first_col}th
   * to {@code last_col} column.
   * 
   * @param first_row The first row position
   * @param first_col The first column position
   * @param last_row The last row position
   * @param last_col The last column position
   * @param unary_operator The unary operator
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code last_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code last_col}) is out of bounds.
   */
  public void submat(int first_row, int first_col, int last_row, int last_col, Op unary_operator) throws IndexOutOfBoundsException {
    if (first_row != 0) {
      throw new IndexOutOfBoundsException("The first row position (" + first_col + ") is out of bounds.");
    }

    if (last_row != 0) {
      throw new IndexOutOfBoundsException("The last row position (" + last_col + ") is out of bounds.");
    }

    if (first_col != 0) {
      throw new IndexOutOfBoundsException("The first column position (" + first_col + ") is out of bounds.");
    }

    if (last_col != 0) {
      throw new IndexOutOfBoundsException("The last column position (" + last_col + ") is out of bounds.");
    }
    
    new ViewSubMat(this, first_row, last_row, first_col, last_col).inPlace(unary_operator);
  }

  /**
   * Performs an in-place binary operation on the {@code first_row}th to {@code last_row} row of the {@code first_col}th
   * to {@code last_col} column with the specified right-hand side operand.
   * 
   * @param first_row The first row position
   * @param first_col The first column position
   * @param last_row The last row position
   * @param last_col The last column position
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code last_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code last_col}) is out of bounds.
   */
  public void submat(int first_row, int first_col, int last_row, int last_col, Op binary_operator, double operand) throws IndexOutOfBoundsException {
    if (first_row != 0) {
      throw new IndexOutOfBoundsException("The first row position (" + first_col + ") is out of bounds.");
    }

    if (last_row != 0) {
      throw new IndexOutOfBoundsException("The last row position (" + last_col + ") is out of bounds.");
    }

    if (first_col != 0) {
      throw new IndexOutOfBoundsException("The first column position (" + first_col + ") is out of bounds.");
    }

    if (last_col != 0) {
      throw new IndexOutOfBoundsException("The last column position (" + last_col + ") is out of bounds.");
    }
    
    new ViewSubMat(this, first_row, last_row, first_col, last_col).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the {@code first_row}th to {@code last_row} row of the {@code first_col}th
   * to {@code last_col} column with the specified right-hand side operand.
   * 
   * @param first_row The first row position
   * @param first_col The first column position
   * @param last_row The last row position
   * @param last_col The last column position
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code last_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code last_col}) is out of bounds.
   */
  public void submat(int first_row, int first_col, int last_row, int last_col, Op binary_operator, AbstractMat operand) throws IndexOutOfBoundsException {
    if (first_row != 0) {
      throw new IndexOutOfBoundsException("The first row position (" + first_col + ") is out of bounds.");
    }

    if (last_row != 0) {
      throw new IndexOutOfBoundsException("The last row position (" + last_col + ") is out of bounds.");
    }

    if (first_col != 0) {
      throw new IndexOutOfBoundsException("The first column position (" + first_col + ") is out of bounds.");
    }

    if (last_col != 0) {
      throw new IndexOutOfBoundsException("The last column position (" + last_col + ") is out of bounds.");
    }
    
    new ViewSubMat(this, first_row, last_row, first_col, last_col).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the {@code row_span._first}th to {@code row_span._last} row of the {@code col_span._first}th
   * to {@code col_span._last} column.
   * 
   * @param row_span The row span
   * @param col_span The column span
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code row_span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code row_span._last}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code col_span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code col_span._last}) is out of bounds.
   */
  abstract public AbstractMat submat(Span row_span, Span col_span) throws IndexOutOfBoundsException;

  /**
   * Performs an in-place unary operation on the {@code row_span._first}th to {@code row_span._last} row of the
   * {@code col_span._first}th to {@code col_span._last} column.
   * 
   * @param row_span The row span
   * @param col_span The column span
   * @param unary_operator The unary operator
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code row_span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code row_span._last}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code col_span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code col_span._last}) is out of bounds.
   */
  public void submat(Span row_span, Span col_span, Op unary_operator) throws IndexOutOfBoundsException {
    submat(row_span._first, col_span._first, row_span._last, col_span._last, unary_operator);
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
   * @throws IndexOutOfBoundsException The first row position ({@code row_span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code row_span._last}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code col_span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code col_span._last}) is out of bounds.
   */
  public void submat(Span row_span, Span col_span, Op binary_operator, double operand) throws IndexOutOfBoundsException {
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
   * @throws IndexOutOfBoundsException The first row position ({@code row_span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code row_span._last}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code col_span._first}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code col_span._last}) is out of bounds.
   */
  public void submat(Span row_span, Span col_span, Op binary_operator, AbstractMat operand) throws IndexOutOfBoundsException {
    submat(row_span._first, col_span._first, row_span._last, col_span._last, binary_operator, operand);
  }

  /**
   * Returns a deep copy starting at position ({@code first_row}, {@code first_col}) of {@code size.n_rows} rows and
   * {@code size.n_cols} columns.
   * 
   * @param first_row The first row position
   * @param first_col The first column position
   * @param size The size
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code first_row + size.n_rows - 1}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code first_col + size.n_cols - 1}) is out of bounds.
   */
  abstract public AbstractMat submat(int first_row, int first_col, Size size) throws IndexOutOfBoundsException;

  /**
   * Performs an in-place unary operation on the position ({@code first_row}, {@code first_col}) of {@code size.n_rows}
   * rows and {@code size.n_cols} columns.
   * 
   * @param first_row The first row position
   * @param first_col The first column position
   * @param size The size
   * @param unary_operator The unary operator
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code first_row + size.n_rows - 1}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code first_col + size.n_cols - 1}) is out of bounds.
   */
  public void submat(int first_row, int first_col, Size size, Op unary_operator) throws IndexOutOfBoundsException {
    submat(first_row, first_col, first_row + size.n_rows - 1, first_col + size.n_cols - 1, unary_operator);
  }

  /**
   * Performs an in-place binary operation on the position ({@code first_row}, {@code first_col}) of {@code size.n_rows}
   * rows and {@code size.n_cols} columns with the specified right-hand side operand.
   * 
   * @param first_row The first row position
   * @param first_col The first column position
   * @param size The size
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code first_row + size.n_rows - 1}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code first_col + size.n_cols - 1}) is out of bounds.
   */
  public void submat(int first_row, int first_col, Size size, Op binary_operator, double operand) throws IndexOutOfBoundsException {
    submat(first_row, first_col, first_row + size.n_rows - 1, first_col + size.n_cols - 1, binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the position ({@code first_row}, {@code first_col}) of {@code size.n_rows}
   * rows and {@code size.n_cols} columns with the specified right-hand side operand.
   * 
   * @param first_row The first row position
   * @param first_col The first column position
   * @param size The size
   * @param binary_operator The binary operator
   * @param operand The operand
   * 
   * @throws IndexOutOfBoundsException The first row position ({@code first_row}) is out of bounds.
   * @throws IndexOutOfBoundsException The last row position ({@code first_row + size.n_rows - 1}) is out of bounds.
   * @throws IndexOutOfBoundsException The first column position ({@code first_col}) is out of bounds.
   * @throws IndexOutOfBoundsException The last column position ({@code first_col + size.n_cols - 1}) is out of bounds.
   */
  public void submat(int first_row, int first_col, Size size, Op binary_operator, AbstractMat operand) throws IndexOutOfBoundsException {
    submat(first_row, first_col, first_row + size.n_rows - 1, first_col + size.n_cols - 1, binary_operator, operand);
  }

  /**
   * Returns a deep copy of the specified elements.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_indices The positions
   */
  abstract public Col elem(AbstractMat vector_of_indices);

  /**
   * Performs an in-place unary operation on the specified elements.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_indices The positions
   * @param unary_operator The unary operator
   */
  public void elem(AbstractMat vector_of_indices, Op unary_operator) {
    new ViewElemMat(this, vector_of_indices).inPlace(unary_operator);
  }

  /**
   * Performs an in-place binary operation on the specified elements with the specified right-hand side operand.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_indices The positions
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void elem(AbstractMat vector_of_indices, Op binary_operator, double operand) {
    new ViewElemMat(this, vector_of_indices).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the specified elements with the specified right-hand side operand.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_indices The positions
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void elem(AbstractMat vector_of_indices, Op binary_operator, AbstractMat operand) {
    new ViewElemMat(this, vector_of_indices).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the specified columns.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_column_indices The column positions
   */
  abstract public AbstractMat cols(AbstractMat vector_of_column_indices);

  /**
   * Performs an in-place unary operation on the specified columns.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_column_indices The column positions
   * @param unary_operator The unary operator
   */
  public void cols(AbstractMat vector_of_column_indices, Op unary_operator) {
    new ViewElemCols(this, vector_of_column_indices).inPlace(unary_operator);
  }

  /**
   * Performs an in-place binary operation on the specified columns with the specified right-hand side operand.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_column_indices The column positions
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void cols(AbstractMat vector_of_column_indices, Op binary_operator, double operand) {
    new ViewElemCols(this, vector_of_column_indices).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the specified columns with the specified right-hand side operand.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_column_indices The column positions
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void cols(AbstractMat vector_of_column_indices, Op binary_operator, AbstractMat operand) {
    new ViewElemCols(this, vector_of_column_indices).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the specified rows.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_row_indices The row positions
   */
  abstract public AbstractMat rows(AbstractMat vector_of_row_indices);

  /**
   * Performs an in-place unary operation on the specified rows.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_row_indices The row positions
   * @param unary_operator The unary operator
   */
  public void rows(AbstractMat vector_of_row_indices, Op unary_operator) {
    new ViewElemCols(this, vector_of_row_indices).inPlace(unary_operator);
  }

  /**
   * Performs an in-place binary operation on the specified rows with the specified right-hand side operand.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_row_indices The row positions
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void rows(AbstractMat vector_of_row_indices, Op binary_operator, double operand) {
    new ViewElemCols(this, vector_of_row_indices).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the specified rows with the specified right-hand side operand.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_row_indices The row positions
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void rows(AbstractMat vector_of_row_indices, Op binary_operator, AbstractMat operand) {
    new ViewElemCols(this, vector_of_row_indices).inPlace(binary_operator, operand);
  }

  /**
   * Returns a deep copy of the specified rows of the specified columns.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_row_indices The row positions
   * @param vector_of_column_indices The column positions
   */
  abstract public AbstractMat submat(AbstractMat vector_of_row_indices, AbstractMat vector_of_column_indices);

  /**
   * Performs an in-place unary operation on the specified rows of the specified columns.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_row_indices The row positions
   * @param vector_of_column_indices The column positions
   * @param unary_operator The unary operator
   */
  public void submat(AbstractMat vector_of_row_indices, AbstractMat vector_of_column_indices, Op unary_operator) {
    new ViewElemSubMat(this, vector_of_row_indices, vector_of_column_indices).inPlace(unary_operator);
  }

  /**
   * Performs an in-place binary operation on the specified rows of the specified columns with the specified right-hand
   * side operand.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_row_indices The row positions
   * @param vector_of_column_indices The column positions
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void submat(AbstractMat vector_of_row_indices, AbstractMat vector_of_column_indices, Op binary_operator, double operand) {
    new ViewElemSubMat(this, vector_of_row_indices, vector_of_column_indices).inPlace(binary_operator, operand);
  }

  /**
   * Performs an in-place binary operation on the specified rows of the specified columns with the specified right-hand
   * side operand.
   * <p>
   * No explicit error handling. However, the JVM should throw IndexOutOfBoundsException exception upon errors.
   * 
   * @param vector_of_row_indices The row positions
   * @param vector_of_column_indices The column positions
   * @param binary_operator The binary operator
   * @param operand The operand
   */
  public void submat(AbstractMat vector_of_row_indices, AbstractMat vector_of_column_indices, Op binary_operator, AbstractMat operand) {
    new ViewElemSubMat(this, vector_of_row_indices, vector_of_column_indices).inPlace(binary_operator, operand);
  }

  /**
   * Swaps the content of this matrix with another one.
   * 
   * @param X The matrix
   */
  abstract public void swap(Mat X);

  /**
   * Swaps the content of this matrix with another one.
   * 
   * @param X The matrix
   */
  abstract public void swap(Col X);

  /**
   * Swaps the content of this matrix with another one.
   * 
   * @param X The matrix
   */
  abstract public void swap(Row X);

  /**
   * Swaps the content of the {@code row1}th row with the {@code row2}th.
   * 
   * @param row1 The first row position
   * @param row2 The second row position
   * 
   * @throws The first row position ({@code row1}) is out of bounds.
   * @throws The first row position ({@code row2}) is out of bounds.
   */
  public void swap_rows(int row1, int row2) throws IndexOutOfBoundsException {
    if (!in_range(row1)) {
      throw new IndexOutOfBoundsException("The first row position (" + row1 + ") is out of bounds.");
    }

    if (!in_range(row2)) {
      throw new IndexOutOfBoundsException("The second row position (" + row2 + ") is out of bounds.");
    }

    AbstractMat copyOfRow1 = row(row1);
    AbstractView viewOfRow2 = new ViewSubRow(this, row2);
    
    new ViewSubRow(this, row1).inPlace(Op.EQUAL, viewOfRow2);
    viewOfRow2.inPlace(Op.EQUAL, copyOfRow1);
  }

  /**
   * Swaps the content of the {@code col1}th column with the {@code col2}th.
   * 
   * @param col1 The first column position
   * @param col2 The second column position
   * 
   * @throws The first column position ({@code col1}) is out of bounds.
   * @throws The first column position ({@code col2}) is out of bounds.
   */
  public void swap_cols(int col1, int col2) throws IndexOutOfBoundsException {
    if (!in_range(col1)) {
      throw new IndexOutOfBoundsException("The first column position (" + col1 + ") is out of bounds.");
    }

    if (!in_range(col2)) {
      throw new IndexOutOfBoundsException("The second column position (" + col2 + ") is out of bounds.");
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
   * @throws RuntimeException The specified matrix must be empty or have exactly one column.
   */
  abstract public void copy_size(AbstractMat A);

  /**
   * Returns the out-of-place addition with the specified right-hand side addend.
   * 
   * @param X The addend
   */
  abstract public AbstractMat plus(double X);

  /**
   * Returns the out-of-place addition with the specified right-hand side addend.
   * 
   * @param X The addend
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  abstract public AbstractMat plus(AbstractMat X) throws RuntimeException;

  /**
   * Returns the out-of-place subtraction with the specified right-hand side subtrahend.
   * 
   * @param X The subtrahend
   */
  abstract public AbstractMat minus(double X);

  /**
   * Returns the out-of-place subtraction with the specified right-hand side subtrahend.
   * 
   * @param X The subtrahend
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  abstract public AbstractMat minus(AbstractMat X) throws RuntimeException;

  /**
   * Returns the out-of-place element-wise division with the specified right-hand side divisor.
   * 
   * @param operand The divisor
   */
  abstract public AbstractMat elemDivide(double X);

  /**
   * Returns the out-of-place element-wise division with the specified right-hand side divisor.
   * 
   * @param operand The divisor
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  abstract public AbstractMat elemDivide(AbstractMat X) throws RuntimeException;

  /**
   * Return the out-of-place matrix multiplication with the specified right-hand side multiplier.
   * 
   * @param X The multiplier
   * 
   * @throws RuntimeException The numbers of columns ({@code n_cols}) must be equal to the number of rows (
   *           {@code X.n_rows}) in the specified multiplier.
   */
  abstract public AbstractMat times(double X) throws RuntimeException;

  /**
   * Return the out-of-place matrix multiplication with the specified right-hand side multiplier.
   * 
   * @param X The multiplier
   * 
   * @throws RuntimeException The numbers of columns ({@code n_cols}) must be equal to the number of rows (
   *           {@code X.n_rows}) in the specified multiplier.
   */
  abstract public AbstractMat times(Col X) throws RuntimeException;

  /**
   * Return the out-of-place matrix multiplication with the specified right-hand side multiplier.
   * 
   * @param X The multiplier
   * 
   * @throws RuntimeException The numbers of columns ({@code n_cols}) must be equal to the number of rows (
   *           {@code X.n_rows}) in the specified multiplier.
   */
  abstract public AbstractMat times(Row X) throws RuntimeException;

  /**
   * Return the out-of-place matrix multiplication with the specified right-hand side multiplier.
   * 
   * @param X The multiplier
   * 
   * @throws RuntimeException The numbers of columns ({@code n_cols}) must be equal to the number of rows (
   *           {@code X.n_rows}) in the specified multiplier.
   */
  abstract public AbstractMat times(Mat X) throws RuntimeException;

  /**
   * Returns the out-of-place element-wise multiplication with the specified right-hand side multiplier.
   * 
   * @param operand The multiplier
   */
  abstract public AbstractMat elemTimes(double X);

  /**
   * Returns the out-of-place element-wise multiplication with the specified right-hand side multiplier.
   * 
   * @param operand The multiplier
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  abstract public AbstractMat elemTimes(AbstractMat X) throws RuntimeException;

  /**
   * Returns the out-of-place element-wise equality evaluation with the specified right-hand side operand.
   * <p>
   * The returned matrix will be set to 1 at positions where the two corresponding values are equal and 0 otherwise.
   * 
   * @param operand The operand
   */
  abstract public AbstractMat equal(double X);

  /**
   * Returns the out-of-place element-wise equality evaluation with the specified right-hand side operand.
   * <p>
   * The returned matrix will be set to 1 at positions where the two corresponding values are equal and 0 otherwise.
   * 
   * @param operand The operand
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  abstract public AbstractMat equal(AbstractMat X) throws RuntimeException;

  /**
   * Returns the out-of-place element-wise non-equality evaluation with the specified right-hand side operand.
   * <p>
   * The returned matrix will be set to 1 at positions where the two corresponding values are non-equal and 0 otherwise.
   * 
   * @param operand The operand
   */
  abstract public AbstractMat nonEqual(double X);

  /**
   * Returns the out-of-place element-wise non-equality evaluation with the specified right-hand side operand.
   * <p>
   * The returned matrix will be set to 1 at positions where the two corresponding values are non-equal and 0 otherwise.
   * 
   * @param operand The operand
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  abstract public AbstractMat nonEqual(AbstractMat X) throws RuntimeException;

  /**
   * Returns the out-of-place element-wise <em>greater than</em> evaluation with the specified right-hand side operand.
   * <p>
   * The returned matrix will be set to 1 at positions where the two corresponding left-hand side value is greater than
   * the right-hand side and 0 otherwise.
   * 
   * @param operand The operand
   */
  abstract public AbstractMat greaterThan(double X);

  /**
   * Returns the out-of-place element-wise <em>greater than</em> evaluation with the specified right-hand side operand.
   * <p>
   * The returned matrix will be set to 1 at positions where the two corresponding left-hand side value is greater than
   * the right-hand side and 0 otherwise.
   * 
   * @param operand The operand
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  abstract public AbstractMat greaterThan(AbstractMat X) throws RuntimeException;

  /**
   * Returns the out-of-place element-wise <em>less than</em> evaluation with the specified right-hand side operand.
   * <p>
   * The returned matrix will be set to 1 at positions where the two corresponding left-hand side value is less than the
   * right-hand side and 0 otherwise.
   * 
   * @param operand The operand
   */
  abstract public AbstractMat lessThan(double X);

  /**
   * Returns the out-of-place element-wise <em>less than</em> evaluation with the specified right-hand side operand.
   * <p>
   * The returned matrix will be set to 1 at positions where the two corresponding left-hand side value is less than the
   * right-hand side and 0 otherwise.
   * 
   * @param operand The operand
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  abstract public AbstractMat lessThan(AbstractMat X) throws RuntimeException;

  /**
   * Returns the out-of-place element-wise <em>strict greater than</em> evaluation with the specified right-hand side
   * operand.
   * <p>
   * The returned matrix will be set to 1 at positions where the two corresponding left-hand side value is strict
   * greater than the right-hand side and 0 otherwise.
   * 
   * @param operand The operand
   */
  abstract public AbstractMat strictGreaterThan(double X);

  /**
   * Returns the out-of-place element-wise <em>strict greater than</em> evaluation with the specified right-hand side
   * operand.
   * <p>
   * The returned matrix will be set to 1 at positions where the two corresponding left-hand side value is strict
   * greater than the right-hand side and 0 otherwise.
   * 
   * @param operand The operand
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  abstract public AbstractMat strictGreaterThan(AbstractMat X) throws RuntimeException;

  /**
   * Returns the out-of-place element-wise <em>strict less than</em> evaluation with the specified right-hand side
   * operand.
   * <p>
   * The returned matrix will be set to 1 at positions where the two corresponding left-hand side value is strict less
   * than the right-hand side and 0 otherwise.
   * 
   * @param operand The operand
   */
  abstract public AbstractMat strictLessThan(double X);

  /**
   * Returns the out-of-place element-wise <em>strict less than</em> evaluation with the specified right-hand side
   * operand.
   * <p>
   * The returned matrix will be set to 1 at positions where the two corresponding left-hand side value is strict less
   * than the right-hand side and 0 otherwise.
   * 
   * @param operand The operand
   * 
   * @throws RuntimeException Both operands must have the same size.
   */
  abstract public AbstractMat strictLessThan(AbstractMat X) throws RuntimeException;

  protected void inPlace(Op operator) {
    switch (operator) {
      case NEGATE:
        for (int n = 0; n < n_elem; n++) {
          _data[n] = -_data[n];
        }
        break;
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
        throw new UnsupportedOperationException("Internal Error: Unsupported operation.");
    }
  }

  protected void inPlace(Op operator, double rightHandOperand) {
    switch (operator) {
      case EQUAL:
        fill(rightHandOperand);
        break;
      case PLUS:
        inPlacePlus(rightHandOperand);
        break;
      case MINUS:
        inPlaceMinus(rightHandOperand);
        break;
      case TIMES:
      case ELEMTIMES:
        inPlaceElemTimes(rightHandOperand);
        break;
      case ELEMDIVIDE:
        inPlaceElemDivide(rightHandOperand);
        break;
      default:
        throw new UnsupportedOperationException("Internal Error: Unsupported operation.");
    }
  }

  protected void inPlace(Op operator, AbstractMat rightHandOperand) {
    switch (operator) {
      case EQUAL:
        copy_size(rightHandOperand);
        System.arraycopy(rightHandOperand._data, 0, _data, 0, n_elem);
        break;
      case PLUS:
        inPlacePlus(rightHandOperand);
        break;
      case MINUS:
        inPlaceMinus(rightHandOperand);
        break;
      case ELEMTIMES:
        inPlaceElemTimes(rightHandOperand);
        break;
      case ELEMDIVIDE:
        inPlaceElemDivide(rightHandOperand);
        break;
      default:
        throw new UnsupportedOperationException("Internal Error: Unsupported operation.");
    }
  }

  protected void inPlacePlus(double rightHandOperand) {
    for (int n = 0; n < n_elem; n++) {
      _data[n] += rightHandOperand;
    }
  }

  protected void inPlacePlus(AbstractMat rightHandOperand) {
    for (int n = 0; n < n_elem; n++) {
      _data[n] += rightHandOperand._data[n];
    }
  }

  protected void inPlaceMinus(double rightHandOperand) {
    for (int n = 0; n < n_elem; n++) {
      _data[n] -= rightHandOperand;
    }
  }

  protected void inPlaceMinus(AbstractMat rightHandOperand) {
    for (int n = 0; n < n_elem; n++) {
      _data[n] -= rightHandOperand._data[n];
    }
  }

  protected void inPlaceElemTimes(double rightHandOperand) {
    for (int n = 0; n < n_elem; n++) {
      _data[n] *= rightHandOperand;
    }
  }

  protected void inPlaceElemTimes(AbstractMat rightHandOperand) {
    for (int n = 0; n < n_elem; n++) {
      _data[n] *= rightHandOperand._data[n];
    }
  }

  protected void inPlaceElemDivide(double rightHandOperand) {
    for (int n = 0; n < n_elem; n++) {
      _data[n] /= rightHandOperand;
    }
  }

  protected void inPlaceElemDivide(AbstractMat rightHandOperand) {
    for (int n = 0; n < n_elem; n++) {
      _data[n] /= rightHandOperand._data[n];
    }
  }

  protected void outOfPlacePlus(AbstractMat leftHandOperand, double rightHandOperand) {
    for (int n = 0; n < n_elem; n++) {
      _data[n] = leftHandOperand._data[n] + rightHandOperand;
    }
  }

  protected void outOfPlacePlus(AbstractMat leftHandOperand, AbstractMat rightHandOperand) {
    for (int n = 0; n < n_elem; n++) {
      _data[n] = leftHandOperand._data[n] + rightHandOperand._data[n];
    }
  }

  protected void outOfPlaceMinus(AbstractMat leftHandOperand, double rightHandOperand) {
    for (int n = 0; n < n_elem; n++) {
      _data[n] = leftHandOperand._data[n] - rightHandOperand;
    }
  }

  protected void outOfPlaceMinus(AbstractMat leftHandOperand, AbstractMat rightHandOperand) {
    for (int n = 0; n < n_elem; n++) {
      _data[n] = leftHandOperand._data[n] - rightHandOperand._data[n];
    }
  }

  protected void outOfPlaceElemTimes(AbstractMat leftHandOperand, double rightHandOperand) {
    for (int n = 0; n < n_elem; n++) {
      _data[n] = leftHandOperand._data[n] * rightHandOperand;
    }
  }

  protected void outOfPlaceElemTimes(AbstractMat leftHandOperand, AbstractMat rightHandOperand) {
    for (int n = 0; n < n_elem; n++) {
      _data[n] = leftHandOperand._data[n] * rightHandOperand._data[n];
    }
  }

  protected void outOfPlaceElemDivide(AbstractMat leftHandOperand, double rightHandOperand) {
    for (int n = 0; n < n_elem; n++) {
      _data[n] = leftHandOperand._data[n] / rightHandOperand;
    }
  }

  protected void outOfPlaceElemDivide(AbstractMat leftHandOperand, AbstractMat rightHandOperand) {
    for (int n = 0; n < n_elem; n++) {
      _data[n] = leftHandOperand._data[n] / rightHandOperand._data[n];
    }
  }

  protected void outOfPlaceEqual(AbstractMat leftHandOperand, double rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (leftHandOperand._data[n] == rightHandOperand) {
        _data[n] = 1;
      }
    }
  }

  protected void outOfPlaceEqual(AbstractMat leftHandOperand, AbstractMat rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (leftHandOperand._data[n] == rightHandOperand._data[n]) {
        _data[n] = 1;
      }
    }
  }

  protected void outOfPlaceNonEqual(AbstractMat leftHandOperand, double rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (leftHandOperand._data[n] != rightHandOperand) {
        _data[n] = 1;
      }
    }
  }

  protected void outOfPlaceNonEqual(AbstractMat leftHandOperand, AbstractMat rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (leftHandOperand._data[n] != rightHandOperand._data[n]) {
        _data[n] = 1;
      }
    }
  }

  protected void outOfPlaceGreaterThan(AbstractMat leftHandOperand, double rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (leftHandOperand._data[n] >= rightHandOperand) {
        _data[n] = 1;
      }
    }
  }

  protected void outOfPlaceGreaterThan(AbstractMat leftHandOperand, AbstractMat rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (leftHandOperand._data[n] >= rightHandOperand._data[n]) {
        _data[n] = 1;
      }
    }
  }

  protected void outOfPlaceLessThan(AbstractMat leftHandOperand, double rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (leftHandOperand._data[n] <= rightHandOperand) {
        _data[n] = 1;
      }
    }
  }

  protected void outOfPlaceLessThan(AbstractMat leftHandOperand, AbstractMat rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (leftHandOperand._data[n] <= rightHandOperand._data[n]) {
        _data[n] = 1;
      }
    }
  }

  protected void outOfPlaceStrictGreaterThan(AbstractMat leftHandOperand, double rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (leftHandOperand._data[n] > rightHandOperand) {
        _data[n] = 1;
      }
    }
  }

  protected void outOfPlaceStrictGreaterThan(AbstractMat leftHandOperand, AbstractMat rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (leftHandOperand._data[n] >= rightHandOperand._data[n]) {
        _data[n] = 1;
      }
    }
  }

  protected void outOfPlaceStrictLessThan(AbstractMat leftHandOperand, double rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (leftHandOperand._data[n] < rightHandOperand) {
        _data[n] = 1;
      }
    }
  }

  protected void outOfPlaceStrictLessThan(AbstractMat leftHandOperand, AbstractMat rightHandOperand) {
    /*
     * All entries of an array are already set to 0 during creation.
     * 
     * See http://docs.oracle.com/javase/specs/jls/se7/html/jls-10.html#jls-10.3
     * and http://docs.oracle.com/javase/specs/jls/se7/html/jls-4.html#jls-4.12.5
     */
    for (int n = 0; n < n_elem; n++) {
      if (leftHandOperand._data[n] < rightHandOperand._data[n]) {
        _data[n] = 1;
      }
    }
  }

  @Override
  public String toString() {
    String output = "(" + n_rows + ", " + n_cols + ")-matrix: [\n";
    for (int i = 0; i < n_rows; i++) {
      output += " ";

      for (int j = 0; j < n_rows; j++) {
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

        output += "\n";
      }
    }
    output += "]";

    return output;
  }
}