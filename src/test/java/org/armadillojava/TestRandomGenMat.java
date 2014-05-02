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
import static org.junit.Assume.assumeThat;
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
public class TestRandomGenMat extends TestClass {

  @Parameters(name = "{index}: Random = {0}, GenMat = {2}, Dim = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.Random);
    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.Dim);

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

  @Parameter(4)
  public String _dimString;

  @Parameter(5)
  public int    _dim;

  protected int _copyOfDim;

  @Before
  public void before() {
    _fileSuffix = _randomString + "," + _genMatString + "," + _dimString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRandom = new Integer(_random);
    _copyOfDim = new Integer(_dim);
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertThat(_random, is(_copyOfRandom));
    assertThat(_dim, is(_copyOfDim));
  }

  @Test
  public void testArmaShuffle() throws IOException {
    assumeThat(_genMat.is_finite(), is(true));
    
    Mat result = Arma.shuffle(_genMat, _dim);
    for(int n = 2; n <= _random; n++) {
      result = (result.times(n)).plus(Arma.shuffle(_genMat, _dim)).divide(n + 1);
    }
    assertMatEquals(result.minus(load("Arma.shuffle")), Arma.zeros(result.n_rows, result.n_cols), 1);
  }

}
