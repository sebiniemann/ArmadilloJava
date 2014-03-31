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
public class TestGenColVecVecNormInt extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, VecNormInt = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.VecNormInt);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genColVecString;

  @Parameter(1)
  public Col       _genColVec;

  protected Col    _copyOfGenColVec;

  @Parameter(2)
  public String    _vecNormIntString;

  @Parameter(3)
  public int    _vecNormInt;

  protected int _copyOfVecNormInt;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _vecNormIntString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfVecNormInt = new Integer(_vecNormInt);
  }

  @After
  public void after() {
    assertMatEquals(_genColVec, _copyOfGenColVec, 0);
    assertThat(_vecNormInt, is(_copyOfVecNormInt));
  }

  @Test
  public void testNorm() throws IOException {
    double expected = load("norm")._data[0];
    double actual = Arma.norm(_genColVec, _vecNormInt);
    
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

}
