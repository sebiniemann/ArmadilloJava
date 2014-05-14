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
public class TestInPlaceGenMatRowIndColIndMatSizeGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowInd = {2}, ColInd = {4}, MatSize = {6}, GenRowVec = {8}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowInd);
    inputClasses.add(InputClass.ColInd);
    inputClasses.add(InputClass.MatSize);
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
  public String _colIndString;

  @Parameter(5)
  public int    _colInd;

  protected int _copyOfColInd;

  @Parameter(6)
  public String _matSizeString;

  @Parameter(7)
  public Size    _matSize;

  protected Size _copyOfMatSize;

  @Parameter(8)
  public String _genRowVecString;

  @Parameter(9)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _rowIndString + "," + _colIndString + "," + _matSizeString + "," + _genRowVecString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRowInd = new Integer(_rowInd);
    _copyOfColInd = new Integer(_colInd);
    _copyOfMatSize = new Size(_matSize);
    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _rowInd = new Integer(_copyOfRowInd);
    _colInd = new Integer(_copyOfColInd);
    _matSize = new Size(_copyOfMatSize);
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
  }
  
  @Test
  public void testMatSubmatEqual() throws IOException {
    assumeThat(_genMat.in_range(_rowInd, _colInd, _matSize), is(true));
    assumeThat(_matSize.n_rows, is(1));
    assumeThat(_genRowVec.n_cols, is(_matSize.n_cols));

    _genMat.submat(_rowInd, _colInd, _matSize, Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatEqual"));
  }
  
  @Test
  public void testMatSubmatPlus() throws IOException {
    assumeThat(_genMat.in_range(_rowInd, _colInd, _matSize), is(true));
    assumeThat(_matSize.n_rows, is(1));
    assumeThat(_genRowVec.n_cols, is(_matSize.n_cols));

    _genMat.submat(_rowInd, _colInd, _matSize, Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatPlus"));
  }

  @Test
  public void testMatSubmatMinus() throws IOException {
    assumeThat(_genMat.in_range(_rowInd, _colInd, _matSize), is(true));
    assumeThat(_matSize.n_rows, is(1));
    assumeThat(_genRowVec.n_cols, is(_matSize.n_cols));

    _genMat.submat(_rowInd, _colInd, _matSize, Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatMinus"));
  }

  @Test
  public void testMatSubmatElemTimes() throws IOException {
    assumeThat(_genMat.in_range(_rowInd, _colInd, _matSize), is(true));
    assumeThat(_matSize.n_rows, is(1));
    assumeThat(_genRowVec.n_cols, is(_matSize.n_cols));

    _genMat.submat(_rowInd, _colInd, _matSize, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatElemTimes"));
  }

  @Test
  public void testMatSubmatElemDivide() throws IOException {
    assumeThat(_genMat.in_range(_rowInd, _colInd, _matSize), is(true));
    assumeThat(_matSize.n_rows, is(1));
    assumeThat(_genRowVec.n_cols, is(_matSize.n_cols));

    _genMat.submat(_rowInd, _colInd, _matSize, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatElemDivide"));
  }

}
