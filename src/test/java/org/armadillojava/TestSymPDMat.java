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
public class TestSymPDMat extends TestClass {

  @Parameters(name = "{index}: SymPDMat = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.SymPDMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _symPDMatString;

  @Parameter(1)
  public Mat    _symPDMat;

  protected Mat _copyOfSymPDMat;

  @Before
  public void before() {
    _fileSuffix = _symPDMatString;

    _copyOfSymPDMat = new Mat(_symPDMat);
  }

  @After
  public void after() {
    assertMatEquals(_symPDMat, _copyOfSymPDMat, 0);
  }

  @Test
  public void testArmaCholA() throws IOException {
    Mat R = Arma.chol(_symPDMat);

    assertMatEquals((R.t()).times(R), _symPDMat);
  }

  @Test
  public void testArmaCholB() throws IOException {
    Mat R = new Mat();

    Arma.chol(R, _symPDMat);

    assertMatEquals((R.t()).times(R), _symPDMat);
  }

  @Test
  public void testArmaInv_sympdA() throws IOException {

    assertMatEquals(Arma.inv_sympd(_symPDMat), load("Arma.inv_sympd"));
  }

  @Test
  public void testArmaInv_sympdB() throws IOException {
    Mat B = new Mat();

    Arma.inv_sympd(B, _symPDMat);

    assertMatEquals(B, load("Arma.inv_sympd"));
  }

}
