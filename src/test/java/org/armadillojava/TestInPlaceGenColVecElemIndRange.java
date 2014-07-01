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
public class TestInPlaceGenColVecElemIndRange extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, ElemIndRange = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.ElemIndRange);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genColVecString;

  @Parameter(1)
  public Col       _genColVec;

  protected Col    _copyOfGenColVec;

  @Parameter(2)
  public String    _elemIndRangeString;

  @Parameter(3)
  public Span      _elemIndRange;

  protected Span   _copyOfElemIndRange;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _elemIndRangeString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfElemIndRange = new Span(_elemIndRange);
  }

  @After
  public void after() {
    _genColVec.inPlace(Op.EQUAL, _copyOfGenColVec);
      _elemIndRange = new Span(_copyOfElemIndRange);
  }

  @Test
  public void testColSwapRows() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));

    _genColVec.swap_rows(_elemIndRange._first, _elemIndRange._last);

    assertMatEquals(_genColVec, load("Col.swap_rows"));
  }

  @Test
  public void testColVecShedRows() throws IOException {
    assumeThat(_elemIndRange._isEntireRange, is(false));
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));

    _genColVec.shed_rows(_elemIndRange._first, _elemIndRange._last);

    assertMatEquals(_genColVec, load("Col.shed_rows"));
  }

}
