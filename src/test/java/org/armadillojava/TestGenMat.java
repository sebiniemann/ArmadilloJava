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
public class TestGenMat extends TestClass {

  @Parameters(name = "{index}: _GenMat = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _GenMatString;

  @Parameter(1)
  public Mat    _GenMat;

  @Before
  public void before() {
    _fileSuffix = _GenMatString;
  }

  @Test  public void testAbs() throws IOException {
    assertMatEquals(Arma.abs(_GenMat), load("abs"));
  }

  @Test  public void testEps() throws IOException {
    assertMatEquals(Arma.eps(_GenMat), load("eps"));
  }

  @Test  public void testExp() throws IOException {
    assertMatEquals(Arma.exp(_GenMat), load("exp"));
  }

  @Test  public void testExp2() throws IOException {
    assertMatEquals(Arma.exp2(_GenMat), load("exp2"));
  }

  @Test  public void testExp10() throws IOException {
    assertMatEquals(Arma.exp10(_GenMat), load("exp10"));
  }

  @Test  public void testTrunc_exp() throws IOException {
    assertMatEquals(Arma.trunc_exp(_GenMat), load("trunc_exp"));
  }

  @Test  public void testLog() throws IOException {
    assertMatEquals(Arma.log(_GenMat), load("log"));
  }

  @Test  public void testLog2() throws IOException {
    assertMatEquals(Arma.log2(_GenMat), load("log2"));
  }

  @Test  public void testLog10() throws IOException {
    assertMatEquals(Arma.log10(_GenMat), load("log10"));
  }

  @Test  public void testTrunc_log() throws IOException {
    assertMatEquals(Arma.trunc_log(_GenMat), load("trunc_log"));
  }

  @Test  public void testSquare() throws IOException {
    assertMatEquals(Arma.square(_GenMat), load("square"));
  }

  @Test  public void testFloor() throws IOException {
    assertMatEquals(Arma.floor(_GenMat), load("floor"));
  }

  @Test  public void testCeil() throws IOException {
    assertMatEquals(Arma.ceil(_GenMat), load("ceil"));
  }

  @Test  public void testRound() throws IOException {
    assertMatEquals(Arma.round(_GenMat), load("round"));
  }

  @Test  public void testSign() throws IOException {
    assertMatEquals(Arma.sign(_GenMat), load("sign"));
  }

  @Test  public void testSin() throws IOException {
    assertMatEquals(Arma.sin(_GenMat), load("sin"));
  }

  @Test  public void testAsin() throws IOException {
    assertMatEquals(Arma.asin(_GenMat), load("asin"));
  }

  @Test  public void testSinh() throws IOException {
    assertMatEquals(Arma.sinh(_GenMat), load("sinh"));
  }

  @Test  public void testAsinh() throws IOException {
    assertMatEquals(Arma.asinh(_GenMat), load("asinh"));
  }

  @Test  public void testCos() throws IOException {
    assertMatEquals(Arma.cos(_GenMat), load("cos"));
  }

  @Test  public void testAcos() throws IOException {
    assertMatEquals(Arma.acos(_GenMat), load("acos"));
  }

  @Test  public void testCosh() throws IOException {
    assertMatEquals(Arma.cosh(_GenMat), load("cosh"));
  }

  @Test  public void testAcosh() throws IOException {
    assertMatEquals(Arma.acosh(_GenMat), load("acosh"));
  }

  @Test  public void testTan() throws IOException {
    assertMatEquals(Arma.tan(_GenMat), load("tan"));
  }

  @Test  public void testAtan() throws IOException {
    assertMatEquals(Arma.atan(_GenMat), load("atan"));
  }

  @Test  public void testTanh() throws IOException {
    assertMatEquals(Arma.tanh(_GenMat), load("tanh"));
  }

  @Test  public void testAtanh() throws IOException {
    assertMatEquals(Arma.atanh(_GenMat), load("atanh"));
  }

}
