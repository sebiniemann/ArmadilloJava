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
public class TestGenColVecNumRowsNumCols extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, NumRows = {2}, NumCols = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.NumRows);
    inputClasses.add(InputClass.NumCols);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genColVecString;

  @Parameter(1)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;

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
    _fileSuffix = _genColVecString + "," + _numRowsString + "," + _numColsString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfNumRows = new Integer(_numRows);
    _copyOfNumCols = new Integer(_numCols);
  }

  @After
  public void after() {
    assertMatEquals(_genColVec, _copyOfGenColVec, 0);
    assertThat(_numRows, is(_copyOfNumRows));
    assertThat(_numCols, is(_copyOfNumCols));
  }

  @Test
  public void testRepmat() throws IOException {
    assertMatEquals(Arma.repmat(_genColVec, _numRows, _numCols), load("Arma.repmat"));
  }

}
