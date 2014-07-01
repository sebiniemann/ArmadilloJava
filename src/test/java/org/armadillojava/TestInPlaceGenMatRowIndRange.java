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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.armadillojava.TestUtil.assertMatEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assume.assumeThat;

@RunWith(Parameterized.class)
public class TestInPlaceGenMatRowIndRange extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowIndRange = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowIndRange);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genMatString;

  @Parameter(1)
  public Mat       _genMat;

  protected Mat    _copyOfGenMat;

  @Parameter(2)
  public String    _rowIndRangeString;

  @Parameter(3)
  public Span      _rowIndRange;

  protected Span   _copyOfRowIndRange;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _rowIndRangeString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRowIndRange = new Span(_rowIndRange);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _rowIndRange = new Span(_copyOfRowIndRange);
  }

  @Test
  public void testMatSwapRows() throws IOException {
    assumeThat(_rowIndRange._isEntireRange, is(false));
    assumeThat(_genMat.in_range(_rowIndRange, Span.all()), is(true));

    _genMat.swap_rows(_rowIndRange._first, _rowIndRange._last);

    assertMatEquals(_genMat, load("Mat.swap_rows"));
  }

  @Test
  public void testMatShedRows() throws IOException {
    assumeThat(_rowIndRange._isEntireRange, is(false));
    assumeThat(_genMat.in_range(_rowIndRange, Span.all()), is(true));

    _genMat.shed_rows(_rowIndRange._first, _rowIndRange._last);

    assertMatEquals(_genMat, load("Mat.shed_rows"));
  }

}
