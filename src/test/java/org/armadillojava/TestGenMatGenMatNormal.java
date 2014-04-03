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
public class TestGenMatGenMatNormal extends TestClass {

  @Parameters(name = "{index}: GenMatA = {0}, GenMatB = {2}, Normal = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.Normal);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatAString;

  @Parameter(1)
  public Mat    _genMatA;

  protected Mat _copyOfGenMatA;

  @Parameter(2)
  public String _genMatBString;

  @Parameter(3)
  public Mat    _genMatB;

  protected Mat _copyOfGenMatB;

  @Parameter(4)
  public String _normalString;

  @Parameter(5)
  public int    _normal;

  protected int _copyOfNormal;

  @Before
  public void before() {
    _fileSuffix = _genMatAString + "," + _genMatBString + "," + _normalString;

    _copyOfGenMatA = new Mat(_genMatA);
    _copyOfGenMatB = new Mat(_genMatB);
    _copyOfNormal = new Integer(_normal);
  }

  @After
  public void after() {
    assertMatEquals(_genMatA, _copyOfGenMatA, 0);
    assertMatEquals(_genMatB, _copyOfGenMatB, 0);
    assertThat(_normal, is(_copyOfNormal));
  }

  @Test
  public void testCor() throws IOException {
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    assertMatEquals(Arma.cor(_genMatA, _genMatB, _normal), load("Arma.cor"));
  }

  @Test
  public void testCov() throws IOException {
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    assertMatEquals(Arma.cov(_genMatA, _genMatB, _normal), load("Arma.cov"));
  }

}
