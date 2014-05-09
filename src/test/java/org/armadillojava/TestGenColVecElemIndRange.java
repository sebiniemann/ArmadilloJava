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
public class TestGenColVecElemIndRange extends TestClass {

  @Parameters(name = "{index}: GenCol = {0}, ElemIndRange = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.ElemIndRange);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String  _genColString;

  @Parameter(1)
  public Col     _genColVec;

  protected Col  _copyOfGenCol;

  @Parameter(2)
  public String  _elemIndRangeString;

  @Parameter(3)
  public Span    _elemIndRange;

  protected Span _copyOfElemIndRange;

  @Before
  public void before() {
    _fileSuffix = _genColString + "," + _elemIndRangeString;

    _copyOfGenCol = new Col(_genColVec);
    _copyOfElemIndRange = new Span(_elemIndRange._first, _elemIndRange._last);
  }

  @After
  public void after() {
    assertMatEquals(_genColVec, _copyOfGenCol, 0);
    assertThat(_elemIndRange._first, is(_copyOfElemIndRange._first));
    assertThat(_elemIndRange._last, is(_copyOfElemIndRange._last));
  }

  @Test
  public void testColIn_range() throws IOException {
    double expected = load("Col.in_range")._data[0];

    if (_genColVec.in_range(_elemIndRange)) {
      assertThat(1.0, is(expected));
    } else {
      assertThat(0.0, is(expected));
    }
  }

}
