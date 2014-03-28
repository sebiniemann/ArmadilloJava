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
public class TestGenColVec extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genColVecString;

  @Parameter(1)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;

  @Before
  public void before() {
    _fileSuffix = _genColVecString;
    _copyOfGenColVec = new Col(_genColVec);
  }

  @After
  public void after() {
    assertMatEquals(_genColVec, _copyOfGenColVec, 0);
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

  @Test
  public void testCumsum() throws IOException {
    assertMatEquals(Arma.cumsum(_genColVec), load("cumsum"));
  }

  @Test
  public void testHist() throws IOException {
    assertMatEquals(Arma.hist(_genColVec), load("hist"));
  }

  @Test
  public void testSort() throws IOException {
    assertMatEquals(Arma.sort(_genColVec), load("sort"));
  }

  @Test
  public void testSort_index() throws IOException {
    assertMatEquals(Arma.sort_index(_genColVec), load("sort_index"));
  }

  @Test
  public void testStable_sort_index() throws IOException {
    assertMatEquals(Arma.stable_sort_index(_genColVec), load("stable_sort_index"));
  }

  @Test
  public void testTrans() throws IOException {
    assertMatEquals(Arma.trans(_genColVec), load("trans"));
  }

  @Test
  public void testUnique() throws IOException {
    assertMatEquals(Arma.unique(_genColVec), load("unique"));
  }

  @Test
  public void testNegate() throws IOException {
    assertMatEquals(Arma.negate(_genColVec), load("negate"));
  }

  @Test
  public void testReciprocal() throws IOException {
    assertMatEquals(Arma.reciprocal(_genColVec), load("reciprocal"));
  }

  @Test
  public void testToeplitz() throws IOException {
    assertMatEquals(Arma.toeplitz(_genColVec), load("toeplitz"));
  }

  @Test
  public void testCirc_toeplitz() throws IOException {
    assertMatEquals(Arma.circ_toeplitz(_genColVec), load("circ_toeplitz"));
  }

  @Test
  public void testAccu() throws IOException {
    double expected = load("accu")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.accu(_genColVec), is(expected));
    } else {
      assertThat(Arma.accu(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testMin() throws IOException {
    double expected = load("min")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.min(_genColVec), is(expected));
    } else {
      assertThat(Arma.min(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testMax() throws IOException {
    double expected = load("max")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.max(_genColVec), is(expected));
    } else {
      assertThat(Arma.max(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testProd() throws IOException {
    double expected = load("prod")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.prod(_genColVec), is(expected));
    } else {
      assertThat(Arma.prod(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testSum() throws IOException {
    double expected = load("sum")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.sum(_genColVec), is(expected));
    } else {
      assertThat(Arma.sum(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testMean() throws IOException {
    double expected = load("mean")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.mean(_genColVec), is(expected));
    } else {
      assertThat(Arma.mean(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testMedian() throws IOException {
    double expected = load("median")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.median(_genColVec), is(expected));
    } else {
      assertThat(Arma.median(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testStddev() throws IOException {
    double expected = load("stddev")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.stddev(_genColVec), is(expected));
    } else {
      assertThat(Arma.stddev(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testVar() throws IOException {
    double expected = load("var")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.var(_genColVec), is(expected));
    } else {
      assertThat(Arma.var(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testCor() throws IOException {
    double expected = load("cor")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.cor(_genColVec), is(expected));
    } else {
      assertThat(Arma.cor(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testCov() throws IOException {
    double expected = load("cov")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.cov(_genColVec), is(expected));
    } else {
      assertThat(Arma.cov(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testDiagmat() throws IOException {
    assertMatEquals(Arma.diagmat(_genColVec), load("diagmat"));
  }

  @Test
  public void testIs_finite() throws IOException {
    int expected = (int) load("is_finite")._data[0];
    if(Arma.is_finite(_genColVec)) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }
  
}
