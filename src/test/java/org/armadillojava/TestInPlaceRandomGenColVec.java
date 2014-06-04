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
public class TestInPlaceRandomGenColVec extends TestClass {

  @Parameters(name = "{index}: Random = {0}, GenColVec = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.Random);
    inputClasses.add(InputClass.GenColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _randomString;

  @Parameter(1)
  public int    _random;

  protected int _copyOfRandom;

  @Parameter(2)
  public String _genColVecString;

  @Parameter(3)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;

  @Before
  public void before() {
    _fileSuffix = _randomString + "," + _genColVecString;

    _copyOfRandom = new Integer(_random);
    _copyOfGenColVec = new Col(_genColVec);
  }

  @After
  public void after() {
    _random = new Integer(_copyOfRandom);
    _genColVec.inPlace(Op.EQUAL, _copyOfGenColVec);
  }

  @Test
  public void testColRandu() throws IOException {
    _genColVec.randu();
    Col result = new Col(_genColVec);

    for (int n = 2; n <= _random; n++) {
      _genColVec.randu();
      result = (result.times(n)).plus(_genColVec).divide(n + 1);
    }

    assertMatEquals(result.minus(load("Col.randu")), Arma.zeros(result.n_rows, result.n_cols), 1);
  }

  @Test
  public void testColRandn() throws IOException {
    _genColVec.randn();
    Col result = new Col(_genColVec);

    for (int n = 2; n <= _random; n++) {
      _genColVec.randn();
      result = (result.times(n)).plus(_genColVec).divide(n + 1);
    }

    assertMatEquals(result.minus(load("Col.randn")), Arma.zeros(result.n_rows, result.n_cols), 1);
  }

}
