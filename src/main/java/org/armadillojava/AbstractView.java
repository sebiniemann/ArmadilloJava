package org.armadillojava;

import java.io.IOException;
import java.io.InputStream;

abstract class AbstractView extends AbstractMat {

  /**
   * The underlying matrix
   */
  protected AbstractMat _matrix;

  /**
   * The current iterator
   */
  protected int         _iterator;

  public AbstractView(AbstractMat matrix) {
    _matrix = matrix;
    _data = matrix._data;
  }

  /**
   * Resets the iterator
   */
  abstract protected void iteratorReset();

  /**
   * Returns true if iterator has yet to reach the last position.
   */
  protected final boolean iteratorHasNext() {
    return (_iterator < n_elem - 1);
  }

  /**
   * Returns the next iterator
   */
  abstract protected int iteratorNext();

  @Override
  public boolean load(InputStream stream, FileType file_type) throws AssertionError, IllegalArgumentException, IOException {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public void reset() {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public void copy_size(AbstractMat A) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat plus(double X) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat plus(AbstractMat X) throws RuntimeException {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat minus(double X) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat minus(AbstractMat X) throws RuntimeException {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat elemDivide(double X) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat elemDivide(AbstractMat X) throws RuntimeException {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat times(Col X) throws RuntimeException {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat times(Row X) throws RuntimeException {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat times(Mat X) throws RuntimeException {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat elemTimes(double X) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat elemTimes(AbstractMat X) throws RuntimeException {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat equal(double X) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat equal(AbstractMat X) throws RuntimeException {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat nonEqual(double X) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat nonEqual(AbstractMat X) throws RuntimeException {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat greaterThan(double X) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat greaterThan(AbstractMat X) throws RuntimeException {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat lessThan(double X) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat lessThan(AbstractMat X) throws RuntimeException {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat strictGreaterThan(double X) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat strictGreaterThan(AbstractMat X) throws RuntimeException {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat strictLessThan(double X) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat strictLessThan(AbstractMat X) throws RuntimeException {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat col(int col_number) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat row(int row_number) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat cols(int first_col, int last_col) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat rows(int first_row, int last_row) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat col(Span span, int col_number) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat row(int row_number, Span span) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat submat(int first_row, int first_col, int last_row, int last_col) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat submat(Span row_span, Span col_span) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat submat(int first_row, int first_col, Size size) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public Col elem(AbstractMat vector_of_indices) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat cols(AbstractMat vector_of_column_indices) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat rows(AbstractMat vector_of_row_indices) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat submat(AbstractMat vector_of_row_indices, AbstractMat vector_of_column_indices) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public void swap(Mat X) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public void swap(Col X) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public void swap(Row X) {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat t() {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

  @Override
  public AbstractMat times(double X) throws RuntimeException {
    throw new UnsupportedOperationException("Internal error: Call to unsupported method.");
  }

}
