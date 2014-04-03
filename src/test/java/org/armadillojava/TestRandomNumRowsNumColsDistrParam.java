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
public class TestRandomNumRowsNumColsDistrParam extends TestClass {

  @Parameters(name = "{index}: Random = {0}, NumRows = {2}, NumCols = {4}, DistrParam = {6}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.Random);
    inputClasses.add(InputClass.NumRows);
    inputClasses.add(InputClass.NumCols);
    inputClasses.add(InputClass.DistrParam);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String        _randomString;

  @Parameter(1)
  public int           _random;

  protected int        _copyOfRandom;

  @Parameter(2)
  public String        _numRowsString;

  @Parameter(3)
  public int           _numRows;

  protected int        _copyOfNumRows;

  @Parameter(4)
  public String        _numColsString;

  @Parameter(5)
  public int           _numCols;

  protected int        _copyOfNumCols;

  @Parameter(6)
  public String        _distrParamString;

  @Parameter(7)
  public DistrParam    _distrParam;

  protected DistrParam _copyOfDistrParam;

  @Before
  public void before() {
    _fileSuffix = _randomString + "," + _numRowsString + "," + _numColsString + "," + _distrParamString;

    _copyOfRandom = new Integer(_random);
    _copyOfNumRows = new Integer(_numRows);
    _copyOfNumCols = new Integer(_numCols);
    _copyOfDistrParam = new DistrParam(_distrParam._a, _distrParam._b);
  }

  @After
  public void after() {
    assertThat(_random, is(_copyOfRandom));
    assertThat(_numRows, is(_copyOfNumRows));
    assertThat(_numCols, is(_copyOfNumCols));
    assertThat(_distrParam._a, is(_copyOfDistrParam._a));
    assertThat(_distrParam._b, is(_copyOfDistrParam._b));
  }

  @Test
  public void testRandiA() throws IOException {
    Mat result = Arma.randi(_numRows, _numCols, _distrParam);
    for (int n = 2; n <= _random; n++) {
      result = (result.times(n)).plus(Arma.randi(_numRows, _numCols, _distrParam)).elemDivide(n + 1);
    }
    assertMatEquals(result.minus(load("Arma.randi")), Arma.zeros(result.n_rows, result.n_cols), 1);
  }

}
