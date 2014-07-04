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

/**
 * Placeholder for the shape of a matrix
 * 
 * @author Sebastian Niemann
 */
public class Size {

  /**
   * The number of rows
   */
  public final int n_rows;

  /**
   * The number of columns
   */
  public final int n_cols;

  /**
   * Creates a deep copy of {@code size}. 
   * <p>
   * The number of rows and columns will be equal to those of {@code size}.
   * 
   * @param size any size object
   */
  protected Size(Size size) {
    this(size.n_rows, size.n_cols);
  }
  
  /**
   * Creates a size object based on the provided matrix.
   * <p>
   * The number of rows will be set to {@code matrix.n_rows} and the number of colums to {@code matrix.n_cols}.
   * 
   * @param matrix any matrix
   */
  public Size(final Mat matrix) {
    n_rows = matrix.n_rows;
    n_cols = matrix.n_cols;
  }
  
  /**
   * Creates a size object based on the provided number of rows and columns.
   * <p>
   * The number of rows will be set to {@code n_rows} and the number of colums to {@code n_cols}.
   * 
   * @param n_rows a non-negative number of rows
   * @param n_cols a non-negative number of columns
   * 
   * @throws IllegalArgumentException The provided number of rows ({@code n_rows}) must be non-negative.
   * @throws IllegalArgumentException The provided number of columns ({@code n_cols}) must be non-negative.
   */
  public Size(final int n_rows, final int n_cols) {
    if (n_rows < 0) {
      throw new IllegalArgumentException("The provided number of rows (" + n_rows + ") must be non-negative.");
    }
    
    if (n_cols < 0) {
      throw new IllegalArgumentException("The provided number of columns (" + n_cols + ") must be non-negative.");
    }
    
    this.n_rows = n_rows;
    this.n_cols = n_cols;
  }

}
