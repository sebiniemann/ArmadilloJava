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
public class TestInPlaceGenMatRowIndGenDouble extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowInd = {2}, GenDouble = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowInd);
    inputClasses.add(InputClass.GenDouble);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genMatString;

  @Parameter(1)
  public Mat       _genMat;

  protected Mat    _copyOfGenMat;

  @Parameter(2)
  public String    _rowIndString;

  @Parameter(3)
  public int       _rowInd;

  protected int    _copyOfRowInd;

  @Parameter(4)
  public String    _genDoubleString;

  @Parameter(5)
  public double    _genDouble;

  protected double _copyOfGenDouble;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _rowIndString + "," + _genDoubleString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRowInd = new Integer(_rowInd);
    _copyOfGenDouble = new Double(_genDouble);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _rowInd = new Integer(_copyOfRowInd);
    _genDouble = new Double(_copyOfGenDouble);
  }

  @Test
  public void testMatDiagPlus() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));

    _genMat.diag(-_rowInd, Op.PLUS, _genDouble);

    assertMatEquals(_genMat, load("Mat.diagSubPlus"));
  }

  @Test
  public void testMatDiagMinus() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));

    _genMat.diag(-_rowInd, Op.MINUS, _genDouble);

    assertMatEquals(_genMat, load("Mat.diagSubMinus"));
  }

  @Test
  public void testMatDiagTimes() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));

    _genMat.diag(-_rowInd, Op.TIMES, _genDouble);

    assertMatEquals(_genMat, load("Mat.diagSubTimes"));
  }

  @Test
  public void testMatDiagDivide() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));

    _genMat.diag(-_rowInd, Op.DIVIDE, _genDouble);

    assertMatEquals(_genMat, load("Mat.diagSubDivide"));
  }

  @Test
  public void testMatRowPlus() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));

    _genMat.row(_rowInd, Op.PLUS, _genDouble);

    assertMatEquals(_genMat, load("Mat.rowPlus"));
  }

  @Test
  public void testMatRowMinus() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));

    _genMat.row(_rowInd, Op.MINUS, _genDouble);

    assertMatEquals(_genMat, load("Mat.rowMinus"));
  }

  @Test
  public void testMatRowTimes() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));

    _genMat.row(_rowInd, Op.TIMES, _genDouble);

    assertMatEquals(_genMat, load("Mat.rowTimes"));
  }

  @Test
  public void testMatRowDivide() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));

    _genMat.row(_rowInd, Op.DIVIDE, _genDouble);

    assertMatEquals(_genMat, load("Mat.rowDivide"));
  }

}
