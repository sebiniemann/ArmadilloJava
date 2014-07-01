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
public class TestGenRowVecElemIndRange extends TestClass {

  @Parameters(name = "{index}: GenRow = {0}, ElemIndRange = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.ElemIndRange);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String  _genRowString;

  @Parameter(1)
  public Row     _genRowVec;

  protected Row  _copyOfGenRow;

  @Parameter(2)
  public String  _elemIndRangeString;

  @Parameter(3)
  public Span    _elemIndRange;

  protected Span _copyOfElemIndRange;

  @Before
  public void before() {
    _fileSuffix = _genRowString + "," + _elemIndRangeString;

    _copyOfGenRow = new Row(_genRowVec);
    _copyOfElemIndRange = new Span(_elemIndRange);
  }

  @After
  public void after() {
    assertMatEquals(_genRowVec, _copyOfGenRow, 0);
    assertThat(_elemIndRange._first, is(_copyOfElemIndRange._first));
    assertThat(_elemIndRange._last, is(_copyOfElemIndRange._last));
  }

  @Test
  public void testRowIn_range() throws IOException {
    double expected = load("Row.in_range")._data[0];

    if (_genRowVec.in_range(_elemIndRange)) {
      assertThat(1.0, is(expected));
    } else {
      assertThat(0.0, is(expected));
    }
  }

  @Test
  public void testRowCols() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));

    assertMatEquals(_genRowVec.cols(_elemIndRange._first, _elemIndRange._last), load("Row.cols"));
  }

  @Test
  public void testRowSubvecA() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));

    assertMatEquals(_genRowVec.subvec(_elemIndRange._first, _elemIndRange._last), load("Row.subvec"));
  }

  @Test
  public void testRowSubvecB() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genRowVec.in_range(_elemIndRange), is(true));

    assertMatEquals(_genRowVec.subvec(_elemIndRange), load("Row.subvec"));
  }
}
