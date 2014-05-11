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
public class TestGenMatRowIndColIndMatSize extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowInd = {2}, ColInd = {4}, MatSize = {6}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowInd);
    inputClasses.add(InputClass.ColInd);
    inputClasses.add(InputClass.MatSize);

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

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _rowIndString + "," + _colIndString + "," + _matSizeString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRowInd = new Integer(_rowInd);
    _copyOfColInd = new Integer(_colInd);
    _copyOfMatSize = new Size(_matSize);
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertThat(_rowInd, is(_copyOfRowInd));
    assertThat(_colInd, is(_copyOfColInd));
    assertThat(_matSize.n_rows, is(_copyOfMatSize.n_rows));
    assertThat(_matSize.n_cols, is(_copyOfMatSize.n_cols));
  }

  @Test
  public void testMatIn_range() throws IOException {
    double expected = load("Mat.in_range")._data[0];

    try {
    
    if (_genMat.in_range(_rowInd, _colInd, _matSize)) {
      assertThat(1.0, is(expected));
    } else {
      assertThat(0.0, is(expected));
    }
    } catch (AssertionError e) {
      System.out.println(_fileSuffix);
      System.out.println(_rowInd + _matSize.n_rows - 1 < _genMat.n_rows);
      System.out.println(_colInd + _matSize.n_cols - 1 < _genMat.n_cols);
      throw e;
    }
  }

  @Test
  public void testMatSubmat() throws IOException {
    assumeThat(_genMat.in_range(_rowInd, _colInd, _matSize), is(true));
    
    assertMatEquals(_genMat.submat(_rowInd, _colInd, _matSize), load("Mat.submat"));
  }

}
