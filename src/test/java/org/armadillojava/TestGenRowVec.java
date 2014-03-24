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
public class TestGenRowVec extends TestClass {

  @Parameters(name = "{index}: _GenRowVec = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _GenRowVecString;

  @Parameter(1)
  public Row    _GenRowVec;

  @Before
  public void before() {
    _fileSuffix = _GenRowVecString;
  }

  @Test  public void testAbs() throws IOException {
    assertMatEquals(Arma.abs(_GenRowVec), load("abs"));
  }

  @Test  public void testEps() throws IOException {
    assertMatEquals(Arma.eps(_GenRowVec), load("eps"));
  }

  @Test  public void testExp() throws IOException {
    assertMatEquals(Arma.exp(_GenRowVec), load("exp"));
  }

  @Test  public void testExp2() throws IOException {
    assertMatEquals(Arma.exp2(_GenRowVec), load("exp2"));
  }

  @Test  public void testExp10() throws IOException {
    assertMatEquals(Arma.exp10(_GenRowVec), load("exp10"));
  }

  @Test  public void testTrunc_exp() throws IOException {
    assertMatEquals(Arma.trunc_exp(_GenRowVec), load("trunc_exp"));
  }

  @Test  public void testLog() throws IOException {
    assertMatEquals(Arma.log(_GenRowVec), load("log"));
  }

  @Test  public void testLog2() throws IOException {
    assertMatEquals(Arma.log2(_GenRowVec), load("log2"));
  }

  @Test  public void testLog10() throws IOException {
    assertMatEquals(Arma.log10(_GenRowVec), load("log10"));
  }

  @Test  public void testTrunc_log() throws IOException {
    assertMatEquals(Arma.trunc_log(_GenRowVec), load("trunc_log"));
  }

  @Test  public void testSquare() throws IOException {
    assertMatEquals(Arma.square(_GenRowVec), load("square"));
  }

  @Test  public void testFloor() throws IOException {
    assertMatEquals(Arma.floor(_GenRowVec), load("floor"));
  }

  @Test  public void testCeil() throws IOException {
    assertMatEquals(Arma.ceil(_GenRowVec), load("ceil"));
  }

  @Test  public void testRound() throws IOException {
    assertMatEquals(Arma.round(_GenRowVec), load("round"));
  }

  @Test  public void testSign() throws IOException {
    assertMatEquals(Arma.sign(_GenRowVec), load("sign"));
  }

  @Test  public void testSin() throws IOException {
    assertMatEquals(Arma.sin(_GenRowVec), load("sin"));
  }

  @Test  public void testAsin() throws IOException {
    assertMatEquals(Arma.asin(_GenRowVec), load("asin"));
  }

  @Test  public void testSinh() throws IOException {
    assertMatEquals(Arma.sinh(_GenRowVec), load("sinh"));
  }

  @Test  public void testAsinh() throws IOException {
    assertMatEquals(Arma.asinh(_GenRowVec), load("asinh"));
  }

  @Test  public void testCos() throws IOException {
    assertMatEquals(Arma.cos(_GenRowVec), load("cos"));
  }

  @Test  public void testAcos() throws IOException {
    assertMatEquals(Arma.acos(_GenRowVec), load("acos"));
  }

  @Test  public void testCosh() throws IOException {
    assertMatEquals(Arma.cosh(_GenRowVec), load("cosh"));
  }

  @Test  public void testAcosh() throws IOException {
    assertMatEquals(Arma.acosh(_GenRowVec), load("acosh"));
  }

  @Test  public void testTan() throws IOException {
    assertMatEquals(Arma.tan(_GenRowVec), load("tan"));
  }

  @Test  public void testAtan() throws IOException {
    assertMatEquals(Arma.atan(_GenRowVec), load("atan"));
  }

  @Test  public void testTanh() throws IOException {
    assertMatEquals(Arma.tanh(_GenRowVec), load("tanh"));
  }

  @Test  public void testAtanh() throws IOException {
    assertMatEquals(Arma.atanh(_GenRowVec), load("atanh"));
  }

}
