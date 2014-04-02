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
public class TestRandomNumElemsDistrParam extends TestClass {

  @Parameters(name = "{index}: Random = {0}, NumElems = {2}, DistrParam = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.Random);
    inputClasses.add(InputClass.NumElems);
    inputClasses.add(InputClass.DistrParam);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String        _randomString;

  @Parameter(1)
  public int           _random;

  protected int        _copyOfRandom;

  @Parameter(2)
  public String        _numElemsString;

  @Parameter(3)
  public int           _numElems;

  protected int        _copyOfNumElems;

  @Parameter(4)
  public String        _distrParamString;

  @Parameter(5)
  public DistrParam    _distrParam;

  protected DistrParam _copyOfDistrParam;

  @Before
  public void before() {
    _fileSuffix = _randomString + "," + _numElemsString + "," + _distrParamString;

    _copyOfRandom = new Integer(_random);
    _copyOfNumElems = new Integer(_numElems);
    _copyOfDistrParam = new DistrParam(_distrParam._a, _distrParam._b);
  }

  @After
  public void after() {
    assertThat(_random, is(_copyOfRandom));
    assertThat(_numElems, is(_copyOfNumElems));
    assertThat(_distrParam._a, is(_copyOfDistrParam._a));
    assertThat(_distrParam._b, is(_copyOfDistrParam._b));
  }

  @Test
  public void testRandiA() throws IOException {
    Col result = Arma.randi(Col.class, _numElems, _distrParam);
    for (int n = 2; n <= _random; n++) {
      result = (result.times(n)).plus(Arma.randi(Col.class, _numElems, _distrParam)).elemDivide(n + 1);
    }
    assertMatEquals(result.minus(load("randi")), Arma.zeros(result.n_rows, result.n_cols), 1);
  }

  @Test
  public void testRandiB() throws IOException {
    Row result = Arma.randi(Row.class, _numElems, _distrParam);
    for (int n = 2; n <= _random; n++) {
      result = (result.times(n)).plus(Arma.randi(Row.class, _numElems, _distrParam)).elemDivide(n + 1);
    }
    assertMatEquals(result.minus(load("randi").t()), Arma.zeros(result.n_rows, result.n_cols), 1);
  }

}
