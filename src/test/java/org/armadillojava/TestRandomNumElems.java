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
public class TestRandomNumElems extends TestClass {

  @Parameters(name = "{index}: Random = {0}, NumElems = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.Random);
    inputClasses.add(InputClass.NumElems);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _randomString;

  @Parameter(1)
  public int    _random;

  protected int _copyOfRandom;

  @Parameter(2)
  public String _numElemsString;

  @Parameter(3)
  public int    _numElems;

  protected int _copyOfNumElems;

  @Before
  public void before() {
    _fileSuffix = _randomString + "," + _numElemsString;

    _copyOfRandom = new Integer(_random);
    _copyOfNumElems = new Integer(_numElems);
  }

  @After
  public void after() {
    assertThat(_random, is(_copyOfRandom));
    assertThat(_numElems, is(_copyOfNumElems));
  }

  @Test
  public void testArmaRandiA() throws IOException {
    Col result = Arma.randi(Col.class, _numElems).divide(Integer.MAX_VALUE);
    for (int n = 2; n <= _random; n++) {
      result = (result.times(n)).plus((Arma.randi(Col.class, _numElems).divide(Integer.MAX_VALUE))).divide(n + 1);
    }
    assertMatEquals(result.minus(load("Arma.randi")), Arma.zeros(result.n_rows, result.n_cols), 0.1);
  }

  @Test
  public void testArmaRandiB() throws IOException {
    Row result = Arma.randi(Row.class, _numElems).divide(Integer.MAX_VALUE);
    for (int n = 2; n <= _random; n++) {
      result = (result.times(n)).plus((Arma.randi(Row.class, _numElems).divide(Integer.MAX_VALUE))).divide(n + 1);
    }
    assertMatEquals(result.minus(load("Arma.randi").t()), Arma.zeros(result.n_rows, result.n_cols), 0.1);
  }

  @Test
  public void testArmaRanduA() throws IOException {
    Col result = Arma.randu(Col.class, _numElems);
    for (int n = 2; n <= _random; n++) {
      result = (result.times(n)).plus(Arma.randu(Col.class, _numElems)).divide(n + 1);
    }
    assertMatEquals(result.minus(load("Arma.randu")), Arma.zeros(result.n_rows, result.n_cols), 1);
  }

  @Test
  public void testArmaRanduB() throws IOException {
    Row result = Arma.randu(Row.class, _numElems);
    for (int n = 2; n <= _random; n++) {
      result = (result.times(n)).plus(Arma.randu(Row.class, _numElems)).divide(n + 1);
    }
    assertMatEquals(result.minus(load("Arma.randu").t()), Arma.zeros(result.n_rows, result.n_cols), 1);
  }

  @Test
  public void testArmaRandnA() throws IOException {
    Col result = Arma.randn(Col.class, _numElems);
    for (int n = 2; n <= _random; n++) {
      result = (result.times(n)).plus(Arma.randn(Col.class, _numElems)).divide(n + 1);
    }
    assertMatEquals(result.minus(load("Arma.randn")), Arma.zeros(result.n_rows, result.n_cols), 1);
  }

  @Test
  public void testArmaRandnB() throws IOException {
    Row result = Arma.randn(Row.class, _numElems);
    for (int n = 2; n <= _random; n++) {
      result = (result.times(n)).plus(Arma.randn(Row.class, _numElems)).divide(n + 1);
    }
    assertMatEquals(result.minus(load("Arma.randn").t()), Arma.zeros(result.n_rows, result.n_cols), 1);
  }

}
