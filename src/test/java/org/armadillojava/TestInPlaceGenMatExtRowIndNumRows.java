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
import static org.hamcrest.Matchers.lessThanOrEqualTo;

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
public class TestInPlaceGenMatExtRowIndNumRows extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, ExtRowInd = {2}, NumRows = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.ExtRowInd);
    inputClasses.add(InputClass.NumRows);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _extRowIndString;

  @Parameter(3)
  public int    _extRowInd;

  protected int _copyOfExtRowInd;

  @Parameter(4)
  public String _numRowsString;

  @Parameter(5)
  public int    _numRows;

  protected int _copyOfNumRows;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _extRowIndString + "," + _numRowsString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfExtRowInd = new Integer(_extRowInd);
    _copyOfNumRows = new Integer(_numRows);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _extRowInd = new Integer(_copyOfExtRowInd);
    _numRows = new Integer(_copyOfNumRows);
  }

  @Test
  public void testMatInsert_rowsA() throws IOException {
    assumeThat(_extRowInd, is(lessThanOrEqualTo(_genMat.n_rows)));

    _genMat.insert_rows(_extRowInd, _numRows, true);

    assertMatEquals(_genMat, load("Mat.insert_rowsTrue"));
  }

  @Test
  public void testMatInsert_rowsB() throws IOException {
    assumeThat(_extRowInd, is(lessThanOrEqualTo(_genMat.n_rows)));

    _genMat.insert_rows(_extRowInd, _numRows, false);

    assertThat(_genMat.n_rows, is(_copyOfGenMat.n_rows + _copyOfNumRows));
    assertThat(_genMat.n_cols, is(_copyOfGenMat.n_cols));

    _genMat.shed_rows(_extRowInd, _extRowInd + _numRows - 1);

    assertMatEquals(_genMat, _copyOfGenMat);
  }

}
