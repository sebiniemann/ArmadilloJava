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
public class TestInPlaceGenMatNumRowsNumCols extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, NumRows = {2}, NumCols = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.NumRows);
    inputClasses.add(InputClass.NumCols);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _numRowsString;

  @Parameter(3)
  public int    _numRows;

  protected int _copyOfNumRows;

  @Parameter(4)
  public String _numColsString;

  @Parameter(5)
  public int    _numCols;

  protected int _copyOfNumCols;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _numRowsString + "," + _numColsString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfNumRows = new Integer(_numRows);
    _copyOfNumCols = new Integer(_numCols);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _numRows = new Integer(_copyOfNumRows);
    _numCols = new Integer(_copyOfNumCols);
  }

  @Test
  public void testMatOnes() throws IOException {
    _genMat.ones(_numRows, _numCols);

    assertMatEquals(_genMat, load("Mat.ones"));
  }

  @Test
  public void testMatZeros() throws IOException {
    _genMat.zeros(_numRows, _numCols);

    assertMatEquals(_genMat, load("Mat.zeros"));
  }

  @Test
  public void testMatEye() throws IOException {
    _genMat.eye(_numRows, _numCols);

    assertMatEquals(_genMat, load("Mat.eye"));
  }

  @Test
  public void testMatResize() throws IOException {
    _genMat.resize(_numRows, _numCols);

    assertMatEquals(_genMat, load("Mat.resize"));
  }

  @Test
  public void testMatReshape() throws IOException {
    _genMat.reshape(_numRows, _numCols);

    assertMatEquals(_genMat, load("Mat.reshape"));
  }

}
