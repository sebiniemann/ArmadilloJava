package org.armadillojava;

import static org.armadillojava.TestUtil.assertMatEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestGenColVec extends TestClass {

  @Parameters(name = "{index}: _GenColVec = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _GenColVecString;

  @Parameter(1)
  public Col    _GenColVec;

  @Before
  public void before() {
    _fileSuffix = _GenColVecString;
  }

  @Test  public void testAbs() throws IOException {
    assertMatEquals(Arma.abs(_GenColVec), load("abs"));
  }

  @Test  public void testEps() throws IOException {
    assertMatEquals(Arma.eps(_GenColVec), load("eps"));
  }

  @Test  public void testExp() throws IOException {
    assertMatEquals(Arma.exp(_GenColVec), load("exp"));
  }

  @Test  public void testExp2() throws IOException {
    assertMatEquals(Arma.exp2(_GenColVec), load("exp2"));
  }

  @Test  public void testExp10() throws IOException {
    assertMatEquals(Arma.exp10(_GenColVec), load("exp10"));
  }

  @Test  public void testTrunc_exp() throws IOException {
    assertMatEquals(Arma.trunc_exp(_GenColVec), load("trunc_exp"));
  }

  @Test  public void testLog() throws IOException {
    assertMatEquals(Arma.log(_GenColVec), load("log"));
  }

  @Test  public void testLog2() throws IOException {
    assertMatEquals(Arma.log2(_GenColVec), load("log2"));
  }

  @Test  public void testLog10() throws IOException {
    assertMatEquals(Arma.log10(_GenColVec), load("log10"));
  }

  @Test  public void testTrunc_log() throws IOException {
    assertMatEquals(Arma.trunc_log(_GenColVec), load("trunc_log"));
  }

  @Test  public void testSquare() throws IOException {
    assertMatEquals(Arma.square(_GenColVec), load("square"));
  }

  @Test  public void testFloor() throws IOException {
    assertMatEquals(Arma.floor(_GenColVec), load("floor"));
  }

  @Test  public void testCeil() throws IOException {
    assertMatEquals(Arma.ceil(_GenColVec), load("ceil"));
  }

  @Test  public void testRound() throws IOException {
    assertMatEquals(Arma.round(_GenColVec), load("round"));
  }

  @Test  public void testSign() throws IOException {
    assertMatEquals(Arma.sign(_GenColVec), load("sign"));
  }

  @Test  public void testSin() throws IOException {
    assertMatEquals(Arma.sin(_GenColVec), load("sin"));
  }

  @Test  public void testAsin() throws IOException {
    assertMatEquals(Arma.asin(_GenColVec), load("asin"));
  }

  @Test  public void testSinh() throws IOException {
    assertMatEquals(Arma.sinh(_GenColVec), load("sinh"));
  }

  @Test  public void testAsinh() throws IOException {
    assertMatEquals(Arma.asinh(_GenColVec), load("asinh"));
  }

  @Test  public void testCos() throws IOException {
    assertMatEquals(Arma.cos(_GenColVec), load("cos"));
  }

  @Test  public void testAcos() throws IOException {
    assertMatEquals(Arma.acos(_GenColVec), load("acos"));
  }

  @Test  public void testCosh() throws IOException {
    assertMatEquals(Arma.cosh(_GenColVec), load("cosh"));
  }

  @Test  public void testAcosh() throws IOException {
    assertMatEquals(Arma.acosh(_GenColVec), load("acosh"));
  }

  @Test  public void testTan() throws IOException {
    assertMatEquals(Arma.tan(_GenColVec), load("tan"));
  }

  @Test  public void testAtan() throws IOException {
    assertMatEquals(Arma.atan(_GenColVec), load("atan"));
  }

  @Test  public void testTanh() throws IOException {
    assertMatEquals(Arma.tanh(_GenColVec), load("tanh"));
  }

  @Test  public void testAtanh() throws IOException {
    assertMatEquals(Arma.atanh(_GenColVec), load("atanh"));
  }

}
