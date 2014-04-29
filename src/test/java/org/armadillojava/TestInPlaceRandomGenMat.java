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
public class TestInPlaceRandomGenMat extends TestClass {

  @Parameters(name = "{index}: Random = {0}, GenMat = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.Random);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _randomString;

  @Parameter(1)
  public int    _random;

  protected int _copyOfRandom;

  @Parameter(2)
  public String _genMatString;

  @Parameter(3)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Before
  public void before() {
    _fileSuffix = _randomString + "," + _genMatString;

    _copyOfRandom = new Integer(_random);
    _copyOfGenMat = new Mat(_genMat);
  }

  @After
  public void after() {
    _random = new Integer(_copyOfRandom);
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
  }

  @Test
  public void testArmaRandu() throws IOException {
    _genMat.randu();
    Mat result = new Mat(_genMat);
    
    for (int n = 2; n <= _random; n++) {
      _genMat.randu();
      result = (result.times(n)).plus(_genMat).divide(n + 1);
    }
    
    assertMatEquals(result.minus(load("Mat.randu")), Arma.zeros(result.n_rows, result.n_cols), 1);
  }

  @Test
  public void testArmaRandn() throws IOException {
    _genMat.randn();
    Mat result = new Mat(_genMat);
    
    for (int n = 2; n <= _random; n++) {
      _genMat.randn();
      result = (result.times(n)).plus(_genMat).divide(n + 1);
    }
    
    assertMatEquals(result.minus(load("Mat.randn")), Arma.zeros(result.n_rows, result.n_cols), 1);
  }

}
