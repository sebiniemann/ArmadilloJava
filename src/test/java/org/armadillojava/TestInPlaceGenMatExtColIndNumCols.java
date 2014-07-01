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
public class TestInPlaceGenMatExtColIndNumCols extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, ExtColInd = {2}, NumCols = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.ExtColInd);
    inputClasses.add(InputClass.NumCols);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _extColIndString;

  @Parameter(3)
  public int    _extColInd;

  protected int _copyOfExtColInd;

  @Parameter(4)
  public String _numColsString;

  @Parameter(5)
  public int    _numCols;

  protected int _copyOfNumCols;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _extColIndString + "," + _numColsString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfExtColInd = new Integer(_extColInd);
    _copyOfNumCols = new Integer(_numCols);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _extColInd = new Integer(_copyOfExtColInd);
    _numCols = new Integer(_copyOfNumCols);
  }

  @Test
  public void testMatInsert_colsA() throws IOException {
    assumeThat(_extColInd, is(lessThanOrEqualTo(_genMat.n_cols)));

    _genMat.insert_cols(_extColInd, _numCols, true);

    assertMatEquals(_genMat, load("Mat.insert_colsTrue"));
  }

  @Test
  public void testMatInsert_colsB() throws IOException {
    assumeThat(_extColInd, is(lessThanOrEqualTo(_genMat.n_cols)));

    _genMat.insert_cols(_extColInd, _numCols, false);

    assertThat(_genMat.n_rows, is(_copyOfGenMat.n_rows));
    assertThat(_genMat.n_cols, is(_copyOfGenMat.n_cols + _copyOfNumCols));

    _genMat.shed_cols(_extColInd, _extColInd + _numCols - 1);
    
    assertMatEquals(_genMat, _copyOfGenMat);
  }

}
