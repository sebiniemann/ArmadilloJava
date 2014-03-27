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
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
public class TestNumRowsNumCols extends TestClass {

  @Parameters(name = "{index}: _genColVec = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.NumRows);
    inputClasses.add(InputClass.NumCols);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _numRowsString;

  @Parameter(1)
  public int    _numRows;

  protected int _copyOfNumRows;

  @Parameter(2)
  public String _numColsString;

  @Parameter(3)
  public int    _numCols;

  protected int _copyOfNumCols;

  @Before
  public void before() {
    _fileSuffix = _numRowsString + _numColsString;

    _copyOfNumRows = _numRows;
    _copyOfNumCols = _numCols;
  }

  @After
  public void after() {
    assertThat(_numRows, is(_copyOfNumRows));
    assertThat(_numCols, is(_copyOfNumCols));
  }

  @Test
  public void testEye() throws IOException {
    assertMatEquals(Arma.eye(_numRows, _numCols), load("eye"));
  }

  @Test
  public void testOnes() throws IOException {
    assertMatEquals(Arma.ones(_numRows, _numCols), load("ones"));
  }

  @Test
  public void testZeros() throws IOException {
    assertMatEquals(Arma.zeros(_numRows, _numCols), load("zeros"));
  }

}
