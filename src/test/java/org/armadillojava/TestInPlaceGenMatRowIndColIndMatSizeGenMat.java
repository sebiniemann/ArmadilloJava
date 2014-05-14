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
public class TestInPlaceGenMatRowIndColIndMatSizeGenMat extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowInd = {2}, ColInd = {4}, MatSize = {6}, GenMat = {8}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowInd);
    inputClasses.add(InputClass.ColInd);
    inputClasses.add(InputClass.MatSize);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatAString;

  @Parameter(1)
  public Mat    _genMatA;

  protected Mat _copyOfGenMatA;

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
  public String _genMatBString;

  @Parameter(9)
  public Mat    _genMatB;

  protected Mat _copyOfGenMatB;

  @Before
  public void before() {
    _fileSuffix = _genMatAString + "," + _rowIndString + "," + _colIndString + "," + _matSizeString + "," + _genMatBString;

    _copyOfGenMatA = new Mat(_genMatA);
    _copyOfRowInd = new Integer(_rowInd);
    _copyOfColInd = new Integer(_colInd);
    _copyOfMatSize = new Size(_matSize);
    _copyOfGenMatB = new Mat(_genMatB);
  }

  @After
  public void after() {
    _genMatA.inPlace(Op.EQUAL, _copyOfGenMatA);
    _rowInd = new Integer(_copyOfRowInd);
    _colInd = new Integer(_copyOfColInd);
    _matSize = new Size(_copyOfMatSize);
    _genMatB.inPlace(Op.EQUAL, _copyOfGenMatB);
  }
  
  @Test
  public void testMatSubmatEqual() throws IOException {
    assumeThat(_genMatA.in_range(_rowInd, _colInd, _matSize), is(true));
    assumeThat(_genMatB.n_rows, is(_matSize.n_rows));
    assumeThat(_genMatB.n_cols, is(_matSize.n_cols));

    _genMatA.submat(_rowInd, _colInd, _matSize, Op.EQUAL, _genMatB);

    assertMatEquals(_genMatA, load("Mat.submatEqual"));
  }
  
  @Test
  public void testMatSubmatPlus() throws IOException {
    assumeThat(_genMatA.in_range(_rowInd, _colInd, _matSize), is(true));
    assumeThat(_genMatB.n_rows, is(_matSize.n_rows));
    assumeThat(_genMatB.n_cols, is(_matSize.n_cols));

    _genMatA.submat(_rowInd, _colInd, _matSize, Op.PLUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.submatPlus"));
  }

  @Test
  public void testMatSubmatMinus() throws IOException {
    assumeThat(_genMatA.in_range(_rowInd, _colInd, _matSize), is(true));
    assumeThat(_genMatB.n_rows, is(_matSize.n_rows));
    assumeThat(_genMatB.n_cols, is(_matSize.n_cols));

    _genMatA.submat(_rowInd, _colInd, _matSize, Op.MINUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.submatMinus"));
  }

  @Test
  public void testMatSubmatElemTimes() throws IOException {
    assumeThat(_genMatA.in_range(_rowInd, _colInd, _matSize), is(true));
    assumeThat(_genMatB.n_rows, is(_matSize.n_rows));
    assumeThat(_genMatB.n_cols, is(_matSize.n_cols));

    _genMatA.submat(_rowInd, _colInd, _matSize, Op.ELEMTIMES, _genMatB);

    assertMatEquals(_genMatA, load("Mat.submatElemTimes"));
  }

  @Test
  public void testMatSubmatElemDivide() throws IOException {
    assumeThat(_genMatA.in_range(_rowInd, _colInd, _matSize), is(true));
    assumeThat(_genMatB.n_rows, is(_matSize.n_rows));
    assumeThat(_genMatB.n_cols, is(_matSize.n_cols));

    _genMatA.submat(_rowInd, _colInd, _matSize, Op.ELEMDIVIDE, _genMatB);

    assertMatEquals(_genMatA, load("Mat.submatElemDivide"));
  }

}
