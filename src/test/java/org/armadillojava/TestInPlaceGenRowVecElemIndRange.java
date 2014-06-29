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
public class TestInPlaceGenRowVecElemIndRange extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, RowIndRange = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.ElemIndRange);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genRowVecString;

  @Parameter(1)
  public Row       _genRowVec;

  protected Row    _copyOfGenRowVec;

  @Parameter(2)
  public String    _IndRangeString;

  @Parameter(3)
  public Span      _IndRange;

  protected Span   _copyOfIndRange;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _IndRangeString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfIndRange = new Span(_IndRange);
  }

  @After
  public void after() {
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
    _IndRange = new Span(_copyOfIndRange);
  }

  @Test
  public void testRowSwapCols() throws IOException {
    assumeThat(_genRowVec.in_range(_IndRange), is(true));
 

    _genRowVec.swap_cols(_IndRange._first,_IndRange._last);

    assertMatEquals(_genRowVec, load("Row.swap_cols"));
  }

}
