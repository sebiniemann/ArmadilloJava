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

import static org.armadillojava.TestUtil.assertMatEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.number.IsCloseTo.closeTo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestGenMatElemInd extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, ElemInd = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.ElemInd);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _elemIndString;

  @Parameter(3)
  public int    _elemInd;

  protected int _copyOfElemInd;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _elemIndString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfElemInd = new Integer(_elemInd);
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertThat(_elemInd, is(_copyOfElemInd));
  }

  @Test
  public void testMatAt() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genMat.n_elem)));
    
    double expected = load("Mat.at")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(_genMat.at(_elemInd), is(expected));
    } else {
      assertThat(_genMat.at(_elemInd), is(closeTo(expected, Math.abs(expected) * 1e-10)));
    }
  }

  @Test
  public void testMatIn_range() throws IOException {
    double expected = load("Mat.in_range")._data[0];
    
    if (_genMat.in_range(_elemInd)) {
      assertThat(1.0, is(expected));
    } else {
      assertThat(0.0, is(expected));
    }
  }

}
