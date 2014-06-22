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
public class TestInPlaceRandomGenRowVec extends TestClass {

  @Parameters(name = "{index}: Random = {0}, GenRowVec = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.Random);
    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _randomString;

  @Parameter(1)
  public int    _random;

  protected int _copyOfRandom;

  @Parameter(2)
  public String _genRowVecString;

  @Parameter(3)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _randomString + "," + _genRowVecString;

    _copyOfRandom = new Integer(_random);
    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    _random = new Integer(_copyOfRandom);
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
  }

  @Test
  public void testRowRandu() throws IOException {
    _genRowVec.randu();
    Row result = new Row(_genRowVec);

    for (int n = 2; n <= _random; n++) {
      _genRowVec.randu();
      result = (result.times(n)).plus(_genRowVec).divide(n + 1);
    }

    assertMatEquals(result.minus(load("row.randu")), Arma.zeros(result.n_rows, result.n_rows), 1);
  }

  @Test
  public void testRowRandn() throws IOException {
    _genRowVec.randn();
    Row result = new Row(_genRowVec);

    for (int n = 2; n <= _random; n++) {
      _genRowVec.randn();
      result = (result.times(n)).plus(_genRowVec).divide(n + 1);
    }

    assertMatEquals(result.minus(load("row.randn")), Arma.zeros(result.n_rows, result.n_rows), 1);
  }

}
