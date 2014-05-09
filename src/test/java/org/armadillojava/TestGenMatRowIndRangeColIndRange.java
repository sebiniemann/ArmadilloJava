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
public class TestGenMatRowIndRangeColIndRange extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowIndRange = {2}, ColIndRange = {3}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowIndRange);
    inputClasses.add(InputClass.ColIndRange);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String  _genMatString;

  @Parameter(1)
  public Mat     _genMat;

  protected Mat  _copyOfGenMat;

  @Parameter(2)
  public String  _rowIndRangeString;

  @Parameter(3)
  public Span    _rowIndRange;

  protected Span _copyOfRowIndRange;

  @Parameter(4)
  public String  _colIndRangeString;

  @Parameter(5)
  public Span    _colIndRange;

  protected Span _copyOfColIndRange;

  @Before
  public void beforeClass() {
    _fileSuffix = _genMatString + "," + _rowIndRangeString + "," + _colIndRangeString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRowIndRange = new Span(_rowIndRange);
    _copyOfColIndRange = new Span(_colIndRange);
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertThat(_rowIndRange._first, is(_copyOfRowIndRange._first));
    assertThat(_rowIndRange._last, is(_copyOfRowIndRange._last));
    assertThat(_colIndRange._first, is(_copyOfColIndRange._first));
    assertThat(_colIndRange._last, is(_copyOfColIndRange._last));
  }

  @Test
  public void testMatIn_range() throws IOException {
    double expected = load("Mat.in_range")._data[0];

    if (_genMat.in_range(_rowIndRange, _colIndRange)) {
      assertThat(1.0, is(expected));
    } else {
      assertThat(0.0, is(expected));
    }
  }

  @Test
  public void testMatSubmatA() throws IOException {
    assumeThat(_genMat.in_range(_rowIndRange, _colIndRange), is(true));

    assertMatEquals(_genMat.submat(_rowIndRange, _colIndRange), load("Mat.submat"));
  }

  @Test
  public void testMatSubmatB() throws IOException {
    assumeThat(_rowIndRange._isEntireRange, is(false));
    assumeThat(_colIndRange._isEntireRange, is(false));
    assumeThat(_genMat.in_range(_rowIndRange, _colIndRange), is(true));

    assertMatEquals(_genMat.submat(_rowIndRange._first, _colIndRange._first, _rowIndRange._last, _colIndRange._last), load("Mat.submat"));
  }

}
