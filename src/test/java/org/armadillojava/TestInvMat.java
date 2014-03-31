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
public class TestInvMat extends TestClass {

  @Parameters(name = "{index}: InvMat = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.InvMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _invMatString;

  @Parameter(1)
  public Mat    _invMat;

  protected Mat _copyOfInvMat;

  @Before
  public void before() {
    _fileSuffix = _invMatString;
    _copyOfInvMat = new Mat(_invMat);
  }

  @After
  public void after() {
    assertMatEquals(_invMat, _copyOfInvMat, 0);
  }

  @Test
  public void testInvA() throws IOException {
    assertMatEquals(Arma.inv(_invMat), load("inv"));
  }

  @Test
  public void testInvB() throws IOException {
    Mat inv = new Mat();
    
    Arma.inv(inv, _invMat);

    assertMatEquals(inv, load("inv"));
  }

}
