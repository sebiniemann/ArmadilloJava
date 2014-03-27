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

import static org.armadillojava.TestUtil.*;

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
public class TestGenColVec extends TestClass {

  @Parameters(name = "{index}: _genColVec = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genColVecString;

  @Parameter(1)
  public Col    _genColVec;

  protected Col _copyOfgenColVec;

  @Before
  public void before() {
    _fileSuffix = _genColVecString;
    _copyOfgenColVec = new Col(_genColVec);
  }

  @After
  public void after() {
    assertMatEquals(_genColVec, _copyOfgenColVec);
  }

  @Test
  public void testAbs() throws IOException {
    assertMatEquals(Arma.abs(_genColVec), load("abs"));
  }

  @Test
  public void testEps() throws IOException {
    assertMatEquals(Arma.eps(_genColVec), load("eps"));
  }

  @Test
  public void testExp() throws IOException {
    assertMatEquals(Arma.exp(_genColVec), load("exp"));
  }

  @Test
  public void testExp2() throws IOException {
    assertMatEquals(Arma.exp2(_genColVec), load("exp2"));
  }

  @Test
  public void testExp10() throws IOException {
    assertMatEquals(Arma.exp10(_genColVec), load("exp10"));
  }

  @Test
  public void testTrunc_exp() throws IOException {
    assertMatEquals(Arma.trunc_exp(_genColVec), load("trunc_exp"));
  }

  @Test
  public void testLog() throws IOException {
    assertMatEquals(Arma.log(_genColVec), load("log"));
  }

  @Test
  public void testLog2() throws IOException {
    assertMatEquals(Arma.log2(_genColVec), load("log2"));
  }

  @Test
  public void testLog10() throws IOException {
    assertMatEquals(Arma.log10(_genColVec), load("log10"));
  }

  @Test
  public void testTrunc_log() throws IOException {
    assertMatEquals(Arma.trunc_log(_genColVec), load("trunc_log"));
  }

  @Test
  public void testSquare() throws IOException {
    assertMatEquals(Arma.square(_genColVec), load("square"));
  }

  @Test
  public void testFloor() throws IOException {
    assertMatEquals(Arma.floor(_genColVec), load("floor"));
  }

  @Test
  public void testCeil() throws IOException {
    assertMatEquals(Arma.ceil(_genColVec), load("ceil"));
  }

  @Test
  public void testRound() throws IOException {
    assertMatEquals(Arma.round(_genColVec), load("round"));
  }

  @Test
  public void testSign() throws IOException {
    assertMatEquals(Arma.sign(_genColVec), load("sign"));
  }

  @Test
  public void testSin() throws IOException {
    assertMatEquals(Arma.sin(_genColVec), load("sin"));
  }

  @Test
  public void testAsin() throws IOException {
    assertMatEquals(Arma.asin(_genColVec), load("asin"));
  }

  @Test
  public void testSinh() throws IOException {
    assertMatEquals(Arma.sinh(_genColVec), load("sinh"));
  }

  @Test
  public void testAsinh() throws IOException {
    assertMatEquals(Arma.asinh(_genColVec), load("asinh"));
  }

  @Test
  public void testCos() throws IOException {
    assertMatEquals(Arma.cos(_genColVec), load("cos"));
  }

  @Test
  public void testAcos() throws IOException {
    assertMatEquals(Arma.acos(_genColVec), load("acos"));
  }

  @Test
  public void testCosh() throws IOException {
    assertMatEquals(Arma.cosh(_genColVec), load("cosh"));
  }

  @Test
  public void testAcosh() throws IOException {
    assertMatEquals(Arma.acosh(_genColVec), load("acosh"));
  }

  @Test
  public void testTan() throws IOException {
    assertMatEquals(Arma.tan(_genColVec), load("tan"));
  }

  @Test
  public void testAtan() throws IOException {
    assertMatEquals(Arma.atan(_genColVec), load("atan"));
  }

  @Test
  public void testTanh() throws IOException {
    assertMatEquals(Arma.tanh(_genColVec), load("tanh"));
  }

  @Test
  public void testAtanh() throws IOException {
    assertMatEquals(Arma.atanh(_genColVec), load("atanh"));
  }

}
