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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

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
public class TestGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString;

    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    assertMatEquals(_genRowVec, _copyOfGenRowVec, 0);
  }

  @Test
  public void testAbs() throws IOException {
    assertMatEquals(Arma.abs(_genRowVec), load("Arma.abs"));
  }

  @Test
  public void testEps() throws IOException {
    assertMatEquals(Arma.eps(_genRowVec), load("Arma.eps"));
  }

  @Test
  public void testExp() throws IOException {
    assertMatEquals(Arma.exp(_genRowVec), load("Arma.exp"));
  }

  @Test
  public void testExp2() throws IOException {
    assertMatEquals(Arma.exp2(_genRowVec), load("Arma.exp2"));
  }

  @Test
  public void testExp10() throws IOException {
    assertMatEquals(Arma.exp10(_genRowVec), load("Arma.exp10"));
  }

  @Test
  public void testTrunc_exp() throws IOException {
    assertMatEquals(Arma.trunc_exp(_genRowVec), load("Arma.trunc_exp"));
  }

  @Test
  public void testLog() throws IOException {
    assertMatEquals(Arma.log(_genRowVec), load("Arma.log"));
  }

  @Test
  public void testLog2() throws IOException {
    assertMatEquals(Arma.log2(_genRowVec), load("Arma.log2"));
  }

  @Test
  public void testLog10() throws IOException {
    assertMatEquals(Arma.log10(_genRowVec), load("Arma.log10"));
  }

  @Test
  public void testTrunc_log() throws IOException {
    assertMatEquals(Arma.trunc_log(_genRowVec), load("Arma.trunc_log"));
  }

  @Test
  public void testSqrt() throws IOException {
    assertMatEquals(Arma.sqrt(_genRowVec), load("Arma.sqrt"));
  }

  @Test
  public void testSquare() throws IOException {
    assertMatEquals(Arma.square(_genRowVec), load("Arma.square"));
  }

  @Test
  public void testFloor() throws IOException {
    assertMatEquals(Arma.floor(_genRowVec), load("Arma.floor"));
  }

  @Test
  public void testCeil() throws IOException {
    assertMatEquals(Arma.ceil(_genRowVec), load("Arma.ceil"));
  }

  @Test
  public void testRound() throws IOException {
    assertMatEquals(Arma.round(_genRowVec), load("Arma.round"));
  }

  @Test
  public void testSign() throws IOException {
    assertMatEquals(Arma.sign(_genRowVec), load("Arma.sign"));
  }

  @Test
  public void testSin() throws IOException {
    assertMatEquals(Arma.sin(_genRowVec), load("Arma.sin"));
  }

  @Test
  public void testAsin() throws IOException {
    assertMatEquals(Arma.asin(_genRowVec), load("Arma.asin"));
  }

  @Test
  public void testSinh() throws IOException {
    assertMatEquals(Arma.sinh(_genRowVec), load("Arma.sinh"));
  }

  @Test
  public void testAsinh() throws IOException {
    assertMatEquals(Arma.asinh(_genRowVec), load("Arma.asinh"));
  }

  @Test
  public void testCos() throws IOException {
    assertMatEquals(Arma.cos(_genRowVec), load("Arma.cos"));
  }

  @Test
  public void testAcos() throws IOException {
    assertMatEquals(Arma.acos(_genRowVec), load("Arma.acos"));
  }

  @Test
  public void testCosh() throws IOException {
    assertMatEquals(Arma.cosh(_genRowVec), load("Arma.cosh"));
  }

  @Test
  public void testAcosh() throws IOException {
    assertMatEquals(Arma.acosh(_genRowVec), load("Arma.acosh"));
  }

  @Test
  public void testTan() throws IOException {
    assertMatEquals(Arma.tan(_genRowVec), load("Arma.tan"));
  }

  @Test
  public void testAtan() throws IOException {
    assertMatEquals(Arma.atan(_genRowVec), load("Arma.atan"));
  }

  @Test
  public void testTanh() throws IOException {
    assertMatEquals(Arma.tanh(_genRowVec), load("Arma.tanh"));
  }

  @Test
  public void testAtanh() throws IOException {
    assertMatEquals(Arma.atanh(_genRowVec), load("Arma.atanh"));
  }

  @Test
  public void testCumsum() throws IOException {
    assertMatEquals(Arma.cumsum(_genRowVec), load("Arma.cumsum"));
  }

  @Test
  public void testHist() throws IOException {
    assertMatEquals(Arma.hist(_genRowVec), load("Arma.hist"));
  }

  @Test
  public void testSort() throws IOException {
    assertMatEquals(Arma.sort(_genRowVec), load("Arma.sort"));
  }

  @Test
  public void testSort_index() throws IOException {
    assertMatEquals(Arma.sort_index(_genRowVec), load("Arma.sort_index"));
  }

  @Test
  public void testStable_sort_index() throws IOException {
    assertMatEquals(Arma.stable_sort_index(_genRowVec), load("Arma.stable_sort_index"));
  }

  @Test
  public void testTrans() throws IOException {
    assertMatEquals(Arma.trans(_genRowVec), load("Arma.trans"));
  }

  @Test
  public void testUnique() throws IOException {
    assertMatEquals(Arma.unique(_genRowVec), load("Arma.unique"));
  }

  @Test
  public void testNegate() throws IOException {
    assertMatEquals(Arma.negate(_genRowVec), load("Arma.negate"));
  }

  @Test
  public void testReciprocal() throws IOException {
    assertMatEquals(Arma.reciprocal(_genRowVec), load("Arma.reciprocal"));
  }

  @Test
  public void testToeplitz() throws IOException {
    assertMatEquals(Arma.toeplitz(_genRowVec), load("Arma.toeplitz"));
  }

  @Test
  public void testCirc_toeplitz() throws IOException {
    assertMatEquals(Arma.circ_toeplitz(_genRowVec), load("Arma.circ_toeplitz"));
  }

  @Test
  public void testAccu() throws IOException {
    double expected = load("Arma.accu")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.accu(_genRowVec), is(expected));
    } else {
      assertThat(Arma.accu(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testMin() throws IOException {
    double expected = load("Arma.min")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.min(_genRowVec), is(expected));
    } else {
      assertThat(Arma.min(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testMax() throws IOException {
    double expected = load("Arma.max")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.max(_genRowVec), is(expected));
    } else {
      assertThat(Arma.max(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testProd() throws IOException {
    double expected = load("Arma.prod")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.prod(_genRowVec), is(expected));
    } else {
      assertThat(Arma.prod(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testSum() throws IOException {
    double expected = load("Arma.sum")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.sum(_genRowVec), is(expected));
    } else {
      assertThat(Arma.sum(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testMean() throws IOException {
    double expected = load("Arma.mean")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.mean(_genRowVec), is(expected));
    } else {
      assertThat(Arma.mean(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testMedian() throws IOException {
    double expected = load("Arma.median")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.median(_genRowVec), is(expected));
    } else {
      assertThat(Arma.median(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testStddev() throws IOException {
    double expected = load("Arma.stddev")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.stddev(_genRowVec), is(expected));
    } else {
      assertThat(Arma.stddev(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testVar() throws IOException {
    double expected = load("Arma.var")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.var(_genRowVec), is(expected));
    } else {
      assertThat(Arma.var(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testCor() throws IOException {
    double expected = load("Arma.cor")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.cor(_genRowVec), is(expected));
    } else {
      assertThat(Arma.cor(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testCov() throws IOException {
    double expected = load("Arma.cov")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.cov(_genRowVec), is(expected));
    } else {
      assertThat(Arma.cov(_genRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testDiagmat() throws IOException {
    assertMatEquals(Arma.diagmat(_genRowVec), load("Arma.diagmat"));
  }

  @Test
  public void testIs_finite() throws IOException {
    int expected = (int) load("Arma.is_finite")._data[0];
    if (Arma.is_finite(_genRowVec)) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }

}
