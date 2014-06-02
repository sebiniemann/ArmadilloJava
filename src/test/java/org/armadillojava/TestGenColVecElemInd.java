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
public class TestGenColVecElemInd extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, ElemInd = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.ElemInd);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genColVecString;

  @Parameter(1)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;

  @Parameter(2)
  public String _elemIndString;

  @Parameter(3)
  public int    _elemInd;

  protected int _copyOfElemInd;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _elemIndString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfElemInd = new Integer(_elemInd);
  }

  @After
  public void after() {
    assertMatEquals(_genColVec, _copyOfGenColVec, 0);
    assertThat(_elemInd, is(_copyOfElemInd));
  }

  @Test
  public void testColVecAt() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genColVec.n_elem)));

    double expected = load("Col.at")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(_genColVec.at(_elemInd), is(expected));
    } else {
      assertThat(_genColVec.at(_elemInd), is(closeTo(expected, Math.abs(expected) * 1e-10)));
    }
  }

  @Test
  public void testColVecIn_range() throws IOException {
    double expected = load("Col.in_range")._data[0];

    if (_genColVec.in_range(_elemInd)) {
      assertThat(1.0, is(expected));
    } else {
      assertThat(0.0, is(expected));;
    }
  }
  
  @Test
  public void testColVecRow() throws IOException {
	assumeThat(_elemInd, is(lessThan(_genColVec.n_rows)));
	
	assertMatEquals(_genColVec.row(_elemInd),load("Col.row"));
  }

}
