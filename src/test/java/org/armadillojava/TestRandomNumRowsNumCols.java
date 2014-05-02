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
public class TestRandomNumRowsNumCols extends TestClass {

  @Parameters(name = "{index}: Random = {0}, NumRows = {2}, NumCols = {4}")
  public static Collection<Object[]> getParameters() {
    RNG.set_seed(123456789);
    
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.Random);
    inputClasses.add(InputClass.NumRows);
    inputClasses.add(InputClass.NumCols);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _randomString;

  @Parameter(1)
  public int    _random;

  protected int _copyOfRandom;

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
    _fileSuffix = _randomString + "," + _numRowsString + "," + _numColsString;

    _copyOfRandom = new Integer(_random);
    _copyOfNumRows = new Integer(_numRows);
    _copyOfNumCols = new Integer(_numCols);
  }

  @After
  public void after() {
    assertThat(_random, is(_copyOfRandom));
    assertThat(_numRows, is(_copyOfNumRows));
    assertThat(_numCols, is(_copyOfNumCols));
  }

  @Test
  public void testArmaRandi() throws IOException {
    Mat result = Arma.randi(_numRows, _numCols).divide(Integer.MAX_VALUE);
    for (int n = 2; n <= _random; n++) {
      result = (result.times(n)).plus((Arma.randi(_numRows, _numCols).divide(Integer.MAX_VALUE))).divide(n + 1);
    }
    assertMatEquals(result.minus(load("Arma.randi")), Arma.zeros(result.n_rows, result.n_cols), 0.1);
  }

  @Test
  public void testArmaRandu() throws IOException {
    Mat result = Arma.randu(_numRows, _numCols);
    for (int n = 2; n <= _random; n++) {
      result = (result.times(n)).plus(Arma.randu(_numRows, _numCols)).divide(n + 1);
    }
    assertMatEquals(result.minus(load("Arma.randu")), Arma.zeros(result.n_rows, result.n_cols), 1);
  }

  @Test
  public void testArmaRandn() throws IOException {
    Mat result = Arma.randn(_numRows, _numCols);
    for (int n = 2; n <= _random; n++) {
      result = (result.times(n)).plus(Arma.randn(_numRows, _numCols)).divide(n + 1);
    }
    assertMatEquals(result.minus(load("Arma.randn")), Arma.zeros(result.n_rows, result.n_cols), 1);
  }

}
