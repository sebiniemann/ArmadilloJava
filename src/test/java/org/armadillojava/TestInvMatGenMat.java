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
public class TestInvMatGenMat extends TestClass {

  @Parameters(name = "{index}: InvMat = {0}, GenMat = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.InvMat);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _invMatString;

  @Parameter(1)
  public Mat    _invMat;

  protected Mat _copyOfInvMat;

  @Parameter(2)
  public String _genMatString;

  @Parameter(3)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Before
  public void before() {
    _fileSuffix = _invMatString + "," + _genMatString;

    _copyOfInvMat = new Mat(_invMat);
    _copyOfGenMat = new Mat(_genMat);
  }

  @After
  public void after() {
    assertMatEquals(_invMat, _copyOfInvMat, 0);
    assertMatEquals(_genMat, _copyOfGenMat, 0);
  }

  @Test
  public void testArmaSolveA() throws IOException {
    assumeThat(_invMat.n_rows, is(_genMat.n_rows));

    assertMatEquals(Arma.solve(_invMat, _genMat), Arma.inv(_invMat).times(_genMat));
  }

  @Test
  public void testArmaSolveB() throws IOException {
    assumeThat(_invMat.n_rows, is(_genMat.n_rows));

    Mat X = new Mat();
    Arma.solve(X, _invMat, _genMat);

    assertMatEquals(X, Arma.inv(_invMat).times(_genMat));
  }

}
