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

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.IsCloseTo.*;

/**
 * Provides methods for often repeated tasks within test cases.
 * 
 * @author Sebastian Niemann <niemann@sra.uni-hannover.de>
 */
public class TestUtil {

  public static void assertMatEquals(AbstractMat A, AbstractMat B) {
    assertMatEquals(A, B, 1e-11);
  }

  public static void assertMatEquals(AbstractMat A, AbstractMat B, double delta) {
    assertThat("The numbers of rows did not match.", A.n_rows, is(B.n_rows));
    assertThat("The numbers of columns did not match.", A.n_cols, is(B.n_cols));
    
    for (int n = 0; n < A.n_elem; n++) {
      double a = A.at(n);
      double b = B.at(n);

      if (Double.isInfinite(a)) {
        assertThat("The values as position " + n + "did not match.", a, is(b));
      } else {
        assertThat("The values as position " + n + "did not match.", a, is(closeTo(b, delta)));
      }
    }
  }
  
  public static double globalDelta(AbstractMat A, double delta) {
    return Math.max(1, A.max()) * delta;
  }
}

