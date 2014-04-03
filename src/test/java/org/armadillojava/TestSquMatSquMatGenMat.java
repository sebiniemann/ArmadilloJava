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
public class TestSquMatSquMatGenMat extends TestClass {

  @Parameters(name = "{index}: SquMatA = {0}, SquMatB = {2}, GenMat = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.SquMat);
    inputClasses.add(InputClass.SquMat);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _squMatAString;

  @Parameter(1)
  public Mat    _squMatA;

  protected Mat _copyOfSquMatA;

  @Parameter(2)
  public String _squMatBString;

  @Parameter(3)
  public Mat    _squMatB;

  protected Mat _copyOfsquMatB;

  @Parameter(4)
  public String _genMatString;

  @Parameter(5)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Before
  public void before() {
    _fileSuffix = _squMatAString + "," + _squMatBString + "," + _genMatString;

    _copyOfSquMatA = new Mat(_squMatA);
    _copyOfsquMatB = new Mat(_squMatB);
    _copyOfGenMat = new Mat(_genMat);
  }

  @After
  public void after() {
    assertMatEquals(_squMatA, _copyOfSquMatA, 0);
    assertMatEquals(_squMatB, _copyOfsquMatB, 0);
    assertMatEquals(_genMat, _copyOfGenMat, 0);
  }

  @Test
  public void testSylA() throws IOException {
    assumeThat(_genMat.n_rows, is(_squMatA.n_rows));
    assumeThat(_genMat.n_cols, is(_squMatB.n_cols));

    Mat X = Arma.syl(_squMatA, _squMatB, _genMat);

    assertMatEquals((_squMatA.times(X)).plus(X.times(_squMatB)).plus(_genMat), Arma.zeros(_genMat.n_rows, _genMat.n_cols), TestUtil.globalDelta(load("Arma.syl"), 1e12));
  }

  @Test
  public void testSylB() throws IOException {
    assumeThat(_genMat.n_rows, is(_squMatA.n_rows));
    assumeThat(_genMat.n_cols, is(_squMatB.n_cols));

    Mat X = new Mat();

    Arma.syl(X, _squMatA, _squMatB, _genMat);

    assertMatEquals((_squMatA.times(X)).plus(X.times(_squMatB)).plus(_genMat), Arma.zeros(_genMat.n_rows, _genMat.n_cols), TestUtil.globalDelta(load("Arma.syl"), 1e12));
  }

}
