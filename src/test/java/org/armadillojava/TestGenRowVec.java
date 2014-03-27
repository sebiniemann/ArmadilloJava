package org.armadillojava;

import static org.armadillojava.TestUtil.*;

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

  @Parameters(name = "{index}: _genRowVec = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString;
  }

  @Test  public void testAbs() throws IOException {
    assertMatEquals(Arma.abs(_genRowVec), load("abs"));
  }

  @Test  public void testEps() throws IOException {
    assertMatEquals(Arma.eps(_genRowVec), load("eps"));
  }

  @Test  public void testExp() throws IOException {
    assertMatEquals(Arma.exp(_genRowVec), load("exp"));
  }

  @Test  public void testExp2() throws IOException {
    assertMatEquals(Arma.exp2(_genRowVec), load("exp2"));
  }

  @Test  public void testExp10() throws IOException {
    assertMatEquals(Arma.exp10(_genRowVec), load("exp10"));
  }

  @Test  public void testTrunc_exp() throws IOException {
    assertMatEquals(Arma.trunc_exp(_genRowVec), load("trunc_exp"));
  }

  @Test  public void testLog() throws IOException {
    assertMatEquals(Arma.log(_genRowVec), load("log"));
  }

  @Test  public void testLog2() throws IOException {
    assertMatEquals(Arma.log2(_genRowVec), load("log2"));
  }

  @Test  public void testLog10() throws IOException {
    assertMatEquals(Arma.log10(_genRowVec), load("log10"));
  }

  @Test  public void testTrunc_log() throws IOException {
    assertMatEquals(Arma.trunc_log(_genRowVec), load("trunc_log"));
  }

  @Test  public void testSquare() throws IOException {
    assertMatEquals(Arma.square(_genRowVec), load("square"));
  }

  @Test  public void testFloor() throws IOException {
    assertMatEquals(Arma.floor(_genRowVec), load("floor"));
  }

  @Test  public void testCeil() throws IOException {
    assertMatEquals(Arma.ceil(_genRowVec), load("ceil"));
  }

  @Test  public void testRound() throws IOException {
    assertMatEquals(Arma.round(_genRowVec), load("round"));
  }

  @Test  public void testSign() throws IOException {
    assertMatEquals(Arma.sign(_genRowVec), load("sign"));
  }

  @Test  public void testSin() throws IOException {
    assertMatEquals(Arma.sin(_genRowVec), load("sin"));
  }

  @Test  public void testAsin() throws IOException {
    assertMatEquals(Arma.asin(_genRowVec), load("asin"));
  }

  @Test  public void testSinh() throws IOException {
    assertMatEquals(Arma.sinh(_genRowVec), load("sinh"));
  }

  @Test  public void testAsinh() throws IOException {
    assertMatEquals(Arma.asinh(_genRowVec), load("asinh"));
  }

  @Test  public void testCos() throws IOException {
    assertMatEquals(Arma.cos(_genRowVec), load("cos"));
  }

  @Test  public void testAcos() throws IOException {
    assertMatEquals(Arma.acos(_genRowVec), load("acos"));
  }

  @Test  public void testCosh() throws IOException {
    assertMatEquals(Arma.cosh(_genRowVec), load("cosh"));
  }

  @Test  public void testAcosh() throws IOException {
    assertMatEquals(Arma.acosh(_genRowVec), load("acosh"));
  }

  @Test  public void testTan() throws IOException {
    assertMatEquals(Arma.tan(_genRowVec), load("tan"));
  }

  @Test  public void testAtan() throws IOException {
    assertMatEquals(Arma.atan(_genRowVec), load("atan"));
  }

  @Test  public void testTanh() throws IOException {
    assertMatEquals(Arma.tanh(_genRowVec), load("tanh"));
  }

  @Test  public void testAtanh() throws IOException {
    assertMatEquals(Arma.atanh(_genRowVec), load("atanh"));
  }

}
