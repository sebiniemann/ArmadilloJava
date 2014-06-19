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
public class TestGenRowVecElemInd extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, ElemInd = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.ElemInd);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Parameter(2)
  public String _elemIndString;

  @Parameter(3)
  public int    _elemInd;

  protected int _copyOfElemInd;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _elemIndString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfElemInd = new Integer(_elemInd);
  }

  @After
  public void after() {
    assertMatEquals(_genRowVec, _copyOfGenRowVec, 0);
    assertThat(_elemInd, is(_copyOfElemInd));
  }

  @Test
  public void testRowVecAt() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));

    double expected = load("Row.at")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(_genRowVec.at(_elemInd), is(expected));
    } else {
      assertThat(_genRowVec.at(_elemInd), is(closeTo(expected, Math.abs(expected) * 1e-10)));
    }
  }

  @Test
  public void testRowVecIn_range() throws IOException {
    double expected = load("Row.in_range")._data[0];

    if (_genRowVec.in_range(_elemInd)) {
      assertThat(1.0, is(expected));
    } else {
      assertThat(0.0, is(expected));;
    }
  }
  
  @Test
  public void testRowVecCol() throws IOException {
	assumeThat(_elemInd, is(lessThan(_genRowVec.n_cols)));
	
	assertMatEquals(_genRowVec.col(_elemInd),load("Row.col"));
  }

}
