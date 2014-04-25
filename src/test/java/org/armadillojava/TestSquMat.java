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
import static org.hamcrest.number.IsCloseTo.closeTo;

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
public class TestSquMat extends TestClass {

  @Parameters(name = "{index}: SquMat = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.SquMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _squMatString;

  @Parameter(1)
  public Mat    _squMat;

  protected Mat _copyOfSquMat;

  @Before
  public void before() {
    _fileSuffix = _squMatString;
    
    _copyOfSquMat = new Mat(_squMat);
  }

  @After
  public void after() {
    assertMatEquals(_squMat, _copyOfSquMat, 0);
  }

  @Test
  public void testArmaDet() throws IOException {
    double expected = load("Arma.det")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.det(_squMat), is(expected));
    } else {
      assertThat(Arma.det(_squMat), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaLog_det() throws IOException {
    double expectedVal = load("Arma.log_detVal")._data[0];
    double expectedSign = load("Arma.log_detSign")._data[0];

    double[] val = new double[1];
    int[] sign = new int[1];

    Arma.log_det(val, sign, _squMat);

    if (Double.isInfinite(expectedVal) || Double.isNaN(expectedVal)) {
      assertThat(val[0], is(expectedVal));
    } else {
      assertThat(val[0], is(closeTo(expectedVal, Math.abs(expectedVal) * 1e-12)));
    }

    assertThat(sign[0], is((int) expectedSign));
  }

  @Test
  public void testArmaTrace() throws IOException {
    double expected = load("Arma.trace")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.trace(_squMat), is(expected));
    } else {
      assertThat(Arma.trace(_squMat), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaDiagmat() throws IOException {
    assertMatEquals(Arma.diagmat(_squMat), load("Arma.diagmat"));
  }

  @Test
  public void testArmaSymmatu() throws IOException {
    assertMatEquals(Arma.symmatu(_squMat), load("Arma.symmatu"));
  }

  @Test
  public void testArmaSymmatl() throws IOException {
    assertMatEquals(Arma.symmatl(_squMat), load("Arma.symmatl"));
  }

  @Test
  public void testArmaTrimatu() throws IOException {
    assertMatEquals(Arma.trimatu(_squMat), load("Arma.trimatu"));
  }

  @Test
  public void testArmaTrimatl() throws IOException {
    assertMatEquals(Arma.trimatl(_squMat), load("Arma.trimatl"));
  }

}
