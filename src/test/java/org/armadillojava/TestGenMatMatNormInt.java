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
import static org.hamcrest.CoreMatchers.is;
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
public class TestGenMatMatNormInt extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, MatNormInt = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.MatNormInt);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genMatString;

  @Parameter(1)
  public Mat       _genMat;

  protected Mat    _copyOfGenMat;

  @Parameter(2)
  public String    _matNormIntString;

  @Parameter(3)
  public int    _matNormInt;

  protected int _copyOfMatNormInt;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _matNormIntString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfMatNormInt = new Integer(_matNormInt);
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertThat(_matNormInt, is(_copyOfMatNormInt));
  }

  @Test
  public void testNorm() throws IOException {
    double expected = load("norm")._data[0];
    double actual = Arma.norm(_genMat, _matNormInt);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

}
