package org.armadillojava;

public class Size {

  /**
   * The number of rows
   */
  public final int n_rows;

  /**
   * The number of columns
   */
  public final int n_cols;

  Size(final AbstractMat matrix) {
    n_rows = matrix.n_rows;
    n_cols = matrix.n_cols;
  }

}
