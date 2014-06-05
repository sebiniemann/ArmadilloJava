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
import static org.junit.Assume.assumeThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.IsCloseTo.closeTo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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
  public void testArmaAbs() throws IOException {
    assertMatEquals(Arma.abs(_genColVec), load("Arma.abs"));
  }

  @Test
  public void testArmaEps() throws IOException {
    assertMatEquals(Arma.eps(_genColVec), load("Arma.eps"));
  }

  @Test
  public void testArmaExp() throws IOException {
    assertMatEquals(Arma.exp(_genColVec), load("Arma.exp"));
  }

  @Test
  public void testArmaExp2() throws IOException {
    assertMatEquals(Arma.exp2(_genColVec), load("Arma.exp2"));
  }

  @Test
  public void testArmaExp10() throws IOException {
    assertMatEquals(Arma.exp10(_genColVec), load("Arma.exp10"));
  }

  @Test
  public void testArmaTrunc_exp() throws IOException {
    assertMatEquals(Arma.trunc_exp(_genColVec), load("Arma.trunc_exp"));
  }

  @Test
  public void testArmaLog() throws IOException {
    assertMatEquals(Arma.log(_genColVec), load("Arma.log"));
  }

  @Test
  public void testArmaLog2() throws IOException {
    assertMatEquals(Arma.log2(_genColVec), load("Arma.log2"));
  }

  @Test
  public void testArmaLog10() throws IOException {
    assertMatEquals(Arma.log10(_genColVec), load("Arma.log10"));
  }

  @Test
  public void testArmaTrunc_log() throws IOException {
    assertMatEquals(Arma.trunc_log(_genColVec), load("Arma.trunc_log"));
  }

  @Test
  public void testArmaSqrt() throws IOException {
    assertMatEquals(Arma.sqrt(_genColVec), load("Arma.sqrt"));
  }

  @Test
  public void testArmaSquare() throws IOException {
    assertMatEquals(Arma.square(_genColVec), load("Arma.square"));
  }

  @Test
  public void testArmaFloor() throws IOException {
    assertMatEquals(Arma.floor(_genColVec), load("Arma.floor"));
  }

  @Test
  public void testArmaCeil() throws IOException {
    assertMatEquals(Arma.ceil(_genColVec), load("Arma.ceil"));
  }

  @Test
  public void testArmaRound() throws IOException {
    assertMatEquals(Arma.round(_genColVec), load("Arma.round"));
  }

  @Test
  public void testArmaSign() throws IOException {
    assertMatEquals(Arma.sign(_genColVec), load("Arma.sign"));
  }

  @Test
  public void testArmaSin() throws IOException {
    assertMatEquals(Arma.sin(_genColVec), load("Arma.sin"));
  }

  @Test
  public void testArmaAsin() throws IOException {
    assertMatEquals(Arma.asin(_genColVec), load("Arma.asin"));
  }

  @Test
  public void testArmaSinh() throws IOException {
    assertMatEquals(Arma.sinh(_genColVec), load("Arma.sinh"));
  }

  @Test
  public void testArmaAsinh() throws IOException {
    assertMatEquals(Arma.asinh(_genColVec), load("Arma.asinh"));
  }

  @Test
  public void testArmaCos() throws IOException {
    assertMatEquals(Arma.cos(_genColVec), load("Arma.cos"));
  }

  @Test
  public void testArmaAcos() throws IOException {
    assertMatEquals(Arma.acos(_genColVec), load("Arma.acos"));
  }

  @Test
  public void testArmaCosh() throws IOException {
    assertMatEquals(Arma.cosh(_genColVec), load("Arma.cosh"));
  }

  @Test
  public void testArmaAcosh() throws IOException {
    assertMatEquals(Arma.acosh(_genColVec), load("Arma.acosh"));
  }

  @Test
  public void testArmaTan() throws IOException {
    assertMatEquals(Arma.tan(_genColVec), load("Arma.tan"));
  }

  @Test
  public void testArmaAtan() throws IOException {
    assertMatEquals(Arma.atan(_genColVec), load("Arma.atan"));
  }

  @Test
  public void testArmaTanh() throws IOException {
    assertMatEquals(Arma.tanh(_genColVec), load("Arma.tanh"));
  }

  @Test
  public void testArmaAtanh() throws IOException {
    assertMatEquals(Arma.atanh(_genColVec), load("Arma.atanh"));
  }

  @Test
  public void testArmaCumsum() throws IOException {
    assertMatEquals(Arma.cumsum(_genColVec), load("Arma.cumsum"));
  }

  @Test
  public void testArmaHist() throws IOException {
    assertMatEquals(Arma.hist(_genColVec), load("Arma.hist"));
  }

  @Test
  public void testArmaSort() throws IOException {
    assumeThat(_genColVec.is_finite(), is(true));

    assertMatEquals(Arma.sort(_genColVec), load("Arma.sort"));
  }

  @Test
  public void testArmaSort_index() throws IOException {
    assumeThat(_genColVec.is_finite(), is(true));

    assertMatEquals(Arma.sort_index(_genColVec), load("Arma.sort_index"));
  }

  @Test
  public void testArmaStable_sort_index() throws IOException {
    assumeThat(_genColVec.is_finite(), is(true));

    assertMatEquals(Arma.stable_sort_index(_genColVec), load("Arma.stable_sort_index"));
  }

  @Test
  public void testArmaTrans() throws IOException {
    assertMatEquals(Arma.trans(_genColVec), load("Arma.trans"));
  }

  @Test
  public void testArmaUnique() throws IOException {
    assertMatEquals(Arma.unique(_genColVec), load("Arma.unique"));
  }

  @Test
  public void testArmaNegate() throws IOException {
    assertMatEquals(Arma.negate(_genColVec), load("Arma.negate"));
  }

  @Test
  public void testArmaReciprocal() throws IOException {
    assertMatEquals(Arma.reciprocal(_genColVec), load("Arma.reciprocal"));
  }

  @Test
  public void testArmaToeplitz() throws IOException {
    assertMatEquals(Arma.toeplitz(_genColVec), load("Arma.toeplitz"));
  }

  @Test
  public void testArmaCirc_toeplitz() throws IOException {
    assertMatEquals(Arma.circ_toeplitz(_genColVec), load("Arma.circ_toeplitz"));
  }

  @Test
  public void testArmaAccu() throws IOException {
    double expected = load("Arma.accu")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.accu(_genColVec), is(expected));
    } else {
      assertThat(Arma.accu(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaMin() throws IOException {
    double expected = load("Arma.min")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.min(_genColVec), is(expected));
    } else {
      assertThat(Arma.min(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaMax() throws IOException {
    double expected = load("Arma.max")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.max(_genColVec), is(expected));
    } else {
      assertThat(Arma.max(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaProd() throws IOException {
    double expected = load("Arma.prod")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.prod(_genColVec), is(expected));
    } else {
      assertThat(Arma.prod(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaSum() throws IOException {
    double expected = load("Arma.sum")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.sum(_genColVec), is(expected));
    } else {
      assertThat(Arma.sum(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaMean() throws IOException {
    double expected = load("Arma.mean")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.mean(_genColVec), is(expected));
    } else {
      assertThat(Arma.mean(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaMedian() throws IOException {
    double expected = load("Arma.median")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.median(_genColVec), is(expected));
    } else {
      assertThat(Arma.median(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaStddev() throws IOException {
    double expected = load("Arma.stddev")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.stddev(_genColVec), is(expected));
    } else {
      assertThat(Arma.stddev(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaVar() throws IOException {
    double expected = load("Arma.var")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.var(_genColVec), is(expected));
    } else {
      assertThat(Arma.var(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaCor() throws IOException {
    double expected = load("Arma.cor")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.cor(_genColVec), is(expected));
    } else {
      assertThat(Arma.cor(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaCov() throws IOException {
    double expected = load("Arma.cov")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.cov(_genColVec), is(expected));
    } else {
      assertThat(Arma.cov(_genColVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaDiagmat() throws IOException {
    assertMatEquals(Arma.diagmat(_genColVec), load("Arma.diagmat"));
  }

  @Test
  public void testArmaIs_finite() throws IOException {
    int expected = (int) load("Arma.is_finite")._data[0];
    if (Arma.is_finite(_genColVec)) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }
  
  @Test
  public void testMat() throws IOException {
    assertMatEquals(new Mat(_genColVec), load("Mat"));
  }
  
  @Test
  public void testColVecEmpty() throws IOException {
    assertThat(_genColVec.empty(), is(false));
  }
  
  @Test
  public void testColVecSize() throws IOException {
    assertThat(_genColVec.size(), is((int) load("Col.size")._data[0]));
  }
  
  @Test
  public void testColVecT() throws IOException {
    assertMatEquals(_genColVec.t(), load("Col.t"));
  }
  
  @Test
  public void testColVecToString() throws IOException {
    OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(byteArrayOutputStream);
    PrintStream previousStream = System.out;

    System.setOut(printStream);

    System.out.println(_genColVec);
    System.out.flush();
    
    System.setOut(previousStream);
    
    assertThat(byteArrayOutputStream.toString().replaceAll("\\s+", " "), containsString(new String(Files.readAllBytes(Paths.get(_filepath + "Col.print(" + _fileSuffix + ").txt")), StandardCharsets.UTF_8).replaceAll("\\s+", " "))); 
  }
  
  @Test
  public void testColVecPrint() throws IOException {
    OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(byteArrayOutputStream);
    PrintStream previousStream = System.out;

    System.setOut(printStream);

    _genColVec.print();
    System.out.flush();
    
    System.setOut(previousStream);
    
    assertThat(byteArrayOutputStream.toString().replaceAll("\\s+", " "), containsString(new String(Files.readAllBytes(Paths.get(_filepath + "Col.print(" + _fileSuffix + ").txt")), StandardCharsets.UTF_8).replaceAll("\\s+", " ")));
  }
  
  @Test
  public void testColVecRaw_print() throws IOException {
    OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(byteArrayOutputStream);
    PrintStream previousStream = System.out;

    System.setOut(printStream);

    _genColVec.raw_print();
    System.out.flush();
    
    System.setOut(previousStream);
    
    assertThat(byteArrayOutputStream.toString().replaceAll("\\s+", " "), containsString(new String(Files.readAllBytes(Paths.get(_filepath + "Col.raw_print(" + _fileSuffix + ").txt")), StandardCharsets.UTF_8).replaceAll("\\s+", " "))); 
    
  }
  
  @Test
  public void testColIs_finite() throws IOException {
    int expected = (int) load("Col.is_finite")._data[0];
    if (_genColVec.is_finite()) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }
  
  @Test
  public void testColMinA() throws IOException {
    double expected = load("Col.minA")._data[0];
    
    assertThat(_genColVec.min(), is(expected));
  }

  @Test
  public void testColMaxA() throws IOException {
    double expected = load("Col.maxA")._data[0];
    assertThat(_genColVec.max(), is(expected));

  }
  
  @Test
  public void testColMinB() throws IOException {
    int expected = (int) load("Col.minB")._data[0];
    int[] value = new int [1];
    _genColVec.min(value);
    assertThat(value[0], is(expected));
  }

  @Test
  public void testColMaxB() throws IOException {
    int expected = (int) load("Col.maxB")._data[0];
	int[] value = new int[1];
	_genColVec.max(value);
	assertThat(value[0], is(expected));
  }
  
  @Test
  public void testColIs_empty() throws IOException {
    int expected = (int) load("Col.is_empty")._data[0];
    if (_genColVec.is_empty()) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }

}
