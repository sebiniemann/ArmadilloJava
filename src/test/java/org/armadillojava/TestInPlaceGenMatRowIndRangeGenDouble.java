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
import static org.hamcrest.Matchers.lessThan;

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
public class TestInPlaceGenMatRowIndRangeGenDouble extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowIndRange = {2}, GenDouble = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowIndRange);
    inputClasses.add(InputClass.GenDouble);

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

  @Parameter(4)
  public String    _genDoubleString;

  @Parameter(5)
  public double    _genDouble;

  protected double _copyOfGenDouble;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _rowIndRangeString + "," + _genDoubleString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRowIndRange = new Span(_rowIndRange._first, _rowIndRange._last);
    _copyOfGenDouble = new Double(_genDouble);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _rowIndRange = new Span(_copyOfRowIndRange._first, _copyOfRowIndRange._last);
    _genDouble = new Double(_copyOfGenDouble);
  }

  @Test
  public void testMatRowsPlus() throws IOException {
    assumeThat(_rowIndRange._last, is(lessThan(_genMat.n_rows)));

    _genMat.rows(_rowIndRange._first, _rowIndRange._last, Op.PLUS, _genDouble);

    assertMatEquals(_genMat, load("Mat.rowsPlus"));
  }

  @Test
  public void testMatRowsMinus() throws IOException {
    assumeThat(_rowIndRange._last, is(lessThan(_genMat.n_rows)));

    _genMat.rows(_rowIndRange._first, _rowIndRange._last, Op.MINUS, _genDouble);

    assertMatEquals(_genMat, load("Mat.rowsMinus"));
  }

  @Test
  public void testMatRowsTimes() throws IOException {
    assumeThat(_rowIndRange._last, is(lessThan(_genMat.n_rows)));

    _genMat.rows(_rowIndRange._first, _rowIndRange._last, Op.TIMES, _genDouble);

    assertMatEquals(_genMat, load("Mat.rowsTimes"));
  }

  @Test
  public void testMatRowsDivide() throws IOException {
    assumeThat(_rowIndRange._last, is(lessThan(_genMat.n_rows)));

    _genMat.rows(_rowIndRange._first, _rowIndRange._last, Op.DIVIDE, _genDouble);

    assertMatEquals(_genMat, load("Mat.rowsDivide"));
  }

}
