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
public class TestInPlaceGenMatColIndGenColVec extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, ColInd = {2}, GenColVec = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.ColInd);
    inputClasses.add(InputClass.GenColVec);

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
  public String _genColVecString;

  @Parameter(5)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _colIndString + "," + _genColVecString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfColInd = new Integer(_colInd);
    _copyOfGenColVec = new Col(_genColVec);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _colInd = new Integer(_copyOfColInd);
    _genColVec.inPlace(Op.EQUAL, _copyOfGenColVec);
  }

  @Test
  public void testMatColEqual() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));
    assumeThat(_genColVec.n_rows, is(_genMat.n_rows));

    _genMat.col(_colInd, Op.EQUAL, _genColVec);

    assertMatEquals(_genMat, load("Mat.colEqual"));
  }

  @Test
  public void testMatColPlus() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));
    assumeThat(_genColVec.n_rows, is(_genMat.n_rows));

    _genMat.col(_colInd, Op.PLUS, _genColVec);

    assertMatEquals(_genMat, load("Mat.colPlus"));
  }

  @Test
  public void testMatColMinus() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));
    assumeThat(_genColVec.n_rows, is(_genMat.n_rows));

    _genMat.col(_colInd, Op.MINUS, _genColVec);

    assertMatEquals(_genMat, load("Mat.colMinus"));
  }

  @Test
  public void testMatColTimes() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));
    assumeThat(_genColVec.n_rows, is(_genMat.n_rows));

    _genMat.col(_colInd, Op.ELEMTIMES, _genColVec);

    assertMatEquals(_genMat, load("Mat.colElemTimes"));
  }

  @Test
  public void testMatColDivide() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));
    assumeThat(_genColVec.n_rows, is(_genMat.n_rows));

    _genMat.col(_colInd, Op.ELEMDIVIDE, _genColVec);

    assertMatEquals(_genMat, load("Mat.colElemDivide"));
  }

}
