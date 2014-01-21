package org.armadillojava;

public class Size {

  /**
   * The number of rows
   */
  public int         n_rows;

  /**
   * The number of columns
   */
  public int         n_cols;
  
  Size(AbstractMat matrix) {
    n_rows = matrix.n_rows;
    n_cols = matrix.n_cols;
  }
  
}
