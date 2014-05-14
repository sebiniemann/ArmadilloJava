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
public class TestGenMatRowIndColIndRange extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowInd = {2}, ColIndRange = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowInd);
    inputClasses.add(InputClass.ColIndRange);

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
  public String _colIndRangeString;

  @Parameter(5)
  public Span    _colIndRange;

  protected Span _copyOfColIndRange;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _rowIndString + "," + _colIndRangeString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRowInd = new Integer(_rowInd);
    _copyOfColIndRange = new Span(_colIndRange);
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertThat(_rowInd, is(_copyOfRowInd));
    assertThat(_colIndRange._first, is(_copyOfColIndRange._first));
    assertThat(_colIndRange._last, is(_copyOfColIndRange._last));
  }

  @Test
  public void testMatRow() throws IOException {
    assumeThat(_rowInd, is(lessThan(_genMat.n_rows)));
    assumeThat(_colIndRange._last, is(lessThan(_genMat.n_cols)));
    
    assertMatEquals(_genMat.row(_rowInd, _colIndRange), load("Mat.row"));
  }

}
