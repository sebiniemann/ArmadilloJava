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
public class TestInPlaceGenMatRowIndGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowInd = {2}, GenRowVec = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowInd);
    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _rowIndString;

  @Parameter(3)
  public int    _rowInd;

  protected int _copyOfRowInd;

  @Parameter(4)
  public String _genRowVecString;

  @Parameter(5)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _rowIndString + "," + _genRowVecString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRowInd = new Integer(_rowInd);
    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _rowInd = new Integer(_copyOfRowInd);
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
  }

  @Test
  public void testMatDiagEqual() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));
    assumeThat(_genRowVec.is_vec(), is(true));
    assumeThat(_genRowVec.n_elem, is(Math.min(_genMat.n_rows - _rowInd, _genMat.n_cols)));

    _genMat.diag(-_rowInd, Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.diagSubEqual"));
  }

  @Test
  public void testMatDiagPlus() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));
    assumeThat(_genRowVec.is_vec(), is(true));
    assumeThat(_genRowVec.n_elem, is(Math.min(_genMat.n_rows - _rowInd, _genMat.n_cols)));

    _genMat.diag(-_rowInd, Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.diagSubPlus"));
  }

  @Test
  public void testMatDiagMinus() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));
    assumeThat(_genRowVec.is_vec(), is(true));
    assumeThat(_genRowVec.n_elem, is(Math.min(_genMat.n_rows - _rowInd, _genMat.n_cols)));

    _genMat.diag(-_rowInd, Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.diagSubMinus"));
  }

  @Test
  public void testMatDiagElemTimes() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));
    assumeThat(_genRowVec.is_vec(), is(true));
    assumeThat(_genRowVec.n_elem, is(Math.min(_genMat.n_rows - _rowInd, _genMat.n_cols)));

    _genMat.diag(-_rowInd, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.diagSubElemTimes"));
  }

  @Test
  public void testMatDiagElemDivide() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));
    assumeThat(_genRowVec.is_vec(), is(true));
    assumeThat(_genRowVec.n_elem, is(Math.min(_genMat.n_rows - _rowInd, _genMat.n_cols)));

    _genMat.diag(-_rowInd, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.diagSubElemDivide"));
  }

  @Test
  public void testMatRowEqual() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    _genMat.row(_rowInd, Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowEqual"));
  }

  @Test
  public void testMatRowPlus() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    _genMat.row(_rowInd, Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowPlus"));
  }

  @Test
  public void testMatRowMinus() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    _genMat.row(_rowInd, Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowMinus"));
  }

  @Test
  public void testMatRowElemTimes() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    _genMat.row(_rowInd, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowElemTimes"));
  }

  @Test
  public void testMatRowElemDivide() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    _genMat.row(_rowInd, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowElemDivide"));
  }

}
