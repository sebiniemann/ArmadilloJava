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
public class TestInPlaceGenMatColIndGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, ColInd = {2}, GenRowVec = {4}")
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
  public String _colIndString;

  @Parameter(3)
  public int    _colInd;

  protected int _copyOfColInd;

  @Parameter(4)
  public String _genRowVecString;

  @Parameter(5)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _colIndString + "," + _genRowVecString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfColInd = new Integer(_colInd);
    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _colInd = new Integer(_copyOfColInd);
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
  }

  @Test
  public void testDiagRowEqual() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));
    assumeThat(_genRowVec.n_elem, is(Math.min(_genMat.n_rows, _genMat.n_cols - _colInd)));

    _genMat.diag(_colInd, Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.diagSuperEqual"));
  }

  @Test
  public void testMatDiagPlus() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));
    assumeThat(_genRowVec.n_elem, is(Math.min(_genMat.n_rows, _genMat.n_cols - _colInd)));

    _genMat.diag(_colInd, Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.diagSuperPlus"));
  }

  @Test
  public void testMatDiagMinus() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));
    assumeThat(_genRowVec.n_elem, is(Math.min(_genMat.n_rows, _genMat.n_cols - _colInd)));

    _genMat.diag(_colInd, Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.diagSuperMinus"));
  }

  @Test
  public void testMatDiagElemTimes() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));
    assumeThat(_genRowVec.n_elem, is(Math.min(_genMat.n_rows, _genMat.n_cols - _colInd)));

    _genMat.diag(_colInd, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.diagSuperElemTimes"));
  }

  @Test
  public void testMatDiagElemDivide() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));
    assumeThat(_genRowVec.n_elem, is(Math.min(_genMat.n_rows, _genMat.n_cols - _colInd)));

    _genMat.diag(_colInd, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.diagSuperElemDivide"));
  }

  @Test
  public void testMatRowEqual() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));

    _genMat.col(_colInd, Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.colEqual"));
  }

  @Test
  public void testMatRowPlus() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));

    _genMat.col(_colInd, Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.colPlus"));
  }

  @Test
  public void testMatRowMinus() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));

    _genMat.col(_colInd, Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.colMinus"));
  }

  @Test
  public void testMatRowElemTimes() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));

    _genMat.col(_colInd, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.colElemTimes"));
  }

  @Test
  public void testMatRowElemDivide() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));

    _genMat.col(_colInd, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.colElemDivide"));
  }

}
