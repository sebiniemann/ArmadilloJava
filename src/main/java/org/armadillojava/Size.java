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
 *   Sebastian Niemann - Lead developer
 *   Daniel Kiechle - Unit testing
 ******************************************************************************/
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
