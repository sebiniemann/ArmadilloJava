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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.containsString;
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
public class TestGenMat extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Before
  public void before() {
    _fileSuffix = _genMatString;

    _copyOfGenMat = new Mat(_genMat);
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
  }

  @Test
  public void testArmaAbs() throws IOException {
    assertMatEquals(Arma.abs(_genMat), load("Arma.abs"));
  }

  @Test
  public void testArmaEps() throws IOException {
    assertMatEquals(Arma.eps(_genMat), load("Arma.eps"));
  }

  @Test
  public void testArmaExp() throws IOException {
    assertMatEquals(Arma.exp(_genMat), load("Arma.exp"));
  }

  @Test
  public void testArmaExp2() throws IOException {
    assertMatEquals(Arma.exp2(_genMat), load("Arma.exp2"));
  }

  @Test
  public void testArmaExp10() throws IOException {
    assertMatEquals(Arma.exp10(_genMat), load("Arma.exp10"));
  }

  @Test
  public void testArmaTrunc_exp() throws IOException {
    assertMatEquals(Arma.trunc_exp(_genMat), load("Arma.trunc_exp"));
  }

  @Test
  public void testArmaLog() throws IOException {
    assertMatEquals(Arma.log(_genMat), load("Arma.log"));
  }

  @Test
  public void testArmaLog2() throws IOException {
    assertMatEquals(Arma.log2(_genMat), load("Arma.log2"));
  }

  @Test
  public void testArmaLog10() throws IOException {
    assertMatEquals(Arma.log10(_genMat), load("Arma.log10"));
  }

  @Test
  public void testArmaTrunc_log() throws IOException {
    assertMatEquals(Arma.trunc_log(_genMat), load("Arma.trunc_log"));
  }

  @Test
  public void testArmaSqrt() throws IOException {
    assertMatEquals(Arma.sqrt(_genMat), load("Arma.sqrt"));
  }

  @Test
  public void testArmaSquare() throws IOException {
    assertMatEquals(Arma.square(_genMat), load("Arma.square"));
  }

  @Test
  public void testArmaFloor() throws IOException {
    assertMatEquals(Arma.floor(_genMat), load("Arma.floor"));
  }

  @Test
  public void testArmaCeil() throws IOException {
    assertMatEquals(Arma.ceil(_genMat), load("Arma.ceil"));
  }

  @Test
  public void testArmaRound() throws IOException {
    assertMatEquals(Arma.round(_genMat), load("Arma.round"));
  }

  @Test
  public void testArmaSign() throws IOException {
    assertMatEquals(Arma.sign(_genMat), load("Arma.sign"));
  }

  @Test
  public void testArmaSin() throws IOException {
    assertMatEquals(Arma.sin(_genMat), load("Arma.sin"));
  }

  @Test
  public void testArmaAsin() throws IOException {
    assertMatEquals(Arma.asin(_genMat), load("Arma.asin"));
  }

  @Test
  public void testArmaSinh() throws IOException {
    assertMatEquals(Arma.sinh(_genMat), load("Arma.sinh"));
  }

  @Test
  public void testArmaAsinh() throws IOException {
    assertMatEquals(Arma.asinh(_genMat), load("Arma.asinh"));
  }

  @Test
  public void testArmaCos() throws IOException {
    assertMatEquals(Arma.cos(_genMat), load("Arma.cos"));
  }

  @Test
  public void testArmaAcos() throws IOException {
    assertMatEquals(Arma.acos(_genMat), load("Arma.acos"));
  }

  @Test
  public void testArmaCosh() throws IOException {
    assertMatEquals(Arma.cosh(_genMat), load("Arma.cosh"));
  }

  @Test
  public void testArmaAcosh() throws IOException {
    assertMatEquals(Arma.acosh(_genMat), load("Arma.acosh"));
  }

  @Test
  public void testArmaTan() throws IOException {
    assertMatEquals(Arma.tan(_genMat), load("Arma.tan"));
  }

  @Test
  public void testArmaAtan() throws IOException {
    assertMatEquals(Arma.atan(_genMat), load("Arma.atan"));
  }

  @Test
  public void testArmaTanh() throws IOException {
    assertMatEquals(Arma.tanh(_genMat), load("Arma.tanh"));
  }

  @Test
  public void testArmaAtanh() throws IOException {
    assertMatEquals(Arma.atanh(_genMat), load("Arma.atanh"));
  }

  @Test
  public void testArmaCond() throws IOException {
    double expected = load("Arma.cond")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.cond(_genMat), is(expected));
    } else {
      assertThat(Arma.cond(_genMat), is(closeTo(expected, Math.abs(expected) * 1e-10)));
    }
  }

  @Test
  public void testArmaRank() throws IOException {
    int expected = (int) load("Arma.rank")._data[0];
    assertThat(Arma.rank(_genMat), is(expected));
  }

  @Test
  public void testArmaDiagvec() throws IOException {
    assertMatEquals(Arma.diagvec(_genMat), load("Arma.diagvec"));
  }

  @Test
  public void testArmaMin() throws IOException {
    assertMatEquals(Arma.min(Row.class, _genMat), load("Arma.min"));
    assertMatEquals(Arma.min(Col.class, _genMat), load("Arma.min").t());
  }

  @Test
  public void testArmaMax() throws IOException {
    assertMatEquals(Arma.max(Row.class, _genMat), load("Arma.max"));
    assertMatEquals(Arma.max(Col.class, _genMat), load("Arma.max").t());
  }

  @Test
  public void testArmaProd() throws IOException {
    assertMatEquals(Arma.prod(Row.class, _genMat), load("Arma.prod"));
    assertMatEquals(Arma.prod(Col.class, _genMat), load("Arma.prod").t());
  }

  @Test
  public void testArmaMean() throws IOException {
    assertMatEquals(Arma.mean(Row.class, _genMat), load("Arma.mean"));
    assertMatEquals(Arma.mean(Col.class, _genMat), load("Arma.mean").t());
  }

  @Test
  public void testArmaMedian() throws IOException {
    assertMatEquals(Arma.median(Row.class, _genMat), load("Arma.median"));
    assertMatEquals(Arma.median(Col.class, _genMat), load("Arma.median").t());
  }

  @Test
  public void testArmaStddev() throws IOException {
    assertMatEquals(Arma.stddev(Row.class, _genMat), load("Arma.stddev"));
    assertMatEquals(Arma.stddev(Col.class, _genMat), load("Arma.stddev").t());
  }

  @Test
  public void testArmaVar() throws IOException {
    assertMatEquals(Arma.var(Row.class, _genMat), load("Arma.var"));
    assertMatEquals(Arma.var(Col.class, _genMat), load("Arma.var").t());
  }

  @Test
  public void testArmaCor() throws IOException {
    assertMatEquals(Arma.cor(_genMat), load("Arma.cor"));
  }

  @Test
  public void testArmaCov() throws IOException {
    assertMatEquals(Arma.cov(_genMat), load("Arma.cov"));
  }

  @Test
  public void testArmaCumsum() throws IOException {
    assertMatEquals(Arma.cumsum(_genMat), load("Arma.cumsum"));
  }

  @Test
  public void testArmaFliplr() throws IOException {
    assertMatEquals(Arma.fliplr(_genMat), load("Arma.fliplr"));
  }

  @Test
  public void testArmaFlipud() throws IOException {
    assertMatEquals(Arma.flipud(_genMat), load("Arma.flipud"));
  }

  @Test
  public void testArmaHist() throws IOException {
    assertMatEquals(Arma.hist(_genMat), load("Arma.hist"));
  }

  @Test
  public void testArmaSort() throws IOException {
    assumeThat(_genMat.is_finite(), is(true));

    assertMatEquals(Arma.sort(_genMat), load("Arma.sort"));
  }

  @Test
  public void testArmaTrans() throws IOException {
    assertMatEquals(Arma.trans(_genMat), load("Arma.trans"));
  }

  @Test
  public void testArmaUnique() throws IOException {
    assertMatEquals(Arma.unique(_genMat), load("Arma.unique"));
  }

  @Test
  public void testArmaVectorise() throws IOException {
    assertMatEquals(Arma.vectorise(Col.class, _genMat), load("Arma.vectorise"));
    assertMatEquals(Arma.vectorise(Row.class, _genMat), load("Arma.vectorise").t());
  }

  @Test
  public void testArmaLu1() throws IOException {
    assumeThat(load("Arma.lu")._data[0], is(1.0));

    Mat L = new Mat();
    Mat U = new Mat();
    Mat P = new Mat();

    Arma.lu(L, U, P, _genMat);

    if (_genMat.is_square()) {
      assertMatEquals(L, Arma.trimatl(L));
      assertMatEquals(U, Arma.trimatu(U));
    }

    assertMatEquals(Arma.sum(Col.class, P, 0), Arma.ones(Col.class, P.n_cols));
    assertMatEquals(Arma.sum(Col.class, P, 1), Arma.ones(Col.class, P.n_rows));

    if (_genMat.is_finite()) {
      assertMatEquals((P.t()).times(L).times(U), _genMat);
    }
  }

  @Test
  public void testArmaLu2() throws IOException {
    assumeThat(load("Arma.lu")._data[0], is(1.0));

    Mat L = new Mat();
    Mat U = new Mat();

    Arma.lu(L, U, _genMat);

    if (_genMat.is_square()) {
      assertMatEquals(U, Arma.trimatu(U));
    }

    if (_genMat.is_finite()) {
      assertMatEquals(L.times(U), _genMat);
    }
  }

  @Test
  public void testArmaPinvA() throws IOException {
    assertMatEquals(Arma.pinv(_genMat), load("Arma.pinv"), TestUtil.globalDelta(load("Arma.pinv"), 1e-12));
  }

  @Test
  public void testArmaPinvB() throws IOException {
    Mat pinv = new Mat();

    Arma.pinv(pinv, _genMat);

    assertMatEquals(pinv, load("Arma.pinv"), TestUtil.globalDelta(load("Arma.pinv"), 1e-12));
  }

  @Test
  public void testArmaPrincompA() throws IOException {
    Mat coeff = new Mat();
    Mat score = new Mat();
    Col latent = new Col();
    Col tsquared = new Col();

    Arma.princomp(coeff, score, latent, tsquared, _genMat);

    assertMatEquals(latent, load("Arma.princompLatent"));
  }

  @Test
  public void testArmaPrincompB() throws IOException {
    Mat coeff = new Mat();
    Mat score = new Mat();
    Col latent = new Col();

    Arma.princomp(coeff, score, latent, _genMat);

    assertMatEquals(latent, load("Arma.princompLatent"));
  }

  @Test
  public void testArmaPrincompC() throws IOException {
    /*
     * Only tests that no exception is thrown.
     */

    Mat coeff = new Mat();
    Mat score = new Mat();

    Arma.princomp(coeff, score, _genMat);
  }

  @Test
  public void testArmaPrincompD() throws IOException {
    /*
     * Only tests that no exception is thrown.
     */

    Mat coeff = new Mat();
    Arma.princomp(coeff, _genMat);
  }

  @Test
  public void testArmaPrincompE() throws IOException {
    /*
     * Only tests that no exception is thrown.
     */

    Arma.princomp(_genMat);
  }

  @Test
  public void testArmaQr() throws IOException {
    Mat Q = new Mat();
    Mat R = new Mat();

    Arma.qr(Q, R, _genMat);

    if (_genMat.is_square()) {
      assertMatEquals(Q.t(), Q.i());
      assertMatEquals(Arma.trimatu(R), R);
    }

    if (_genMat.is_finite()) {
      assertMatEquals(Q.times(R), _genMat);
    }
  }

  @Test
  public void testArmaQr_econ() throws IOException {
    Mat Q = new Mat();
    Mat R = new Mat();

    Arma.qr_econ(Q, R, _genMat);

    if (_genMat.n_rows <= _genMat.n_cols && _genMat.is_square()) {
      assertMatEquals(Q.t(), Q.i());
      assertMatEquals(Arma.trimatu(R), R);
    }

    if (_genMat.is_finite()) {
      assertMatEquals(Q.times(R), _genMat);
    }
  }

  @Test
  public void testArmaSvdA() throws IOException {
    assertMatEquals(Arma.svd(_genMat), load("Arma.svd"));
  }

  @Test
  public void testArmaSvdB() throws IOException {
    Col s = new Col();

    Arma.svd(s, _genMat);

    assertMatEquals(s, load("Arma.svd"));
  }

  @Test
  public void testArmaSvdC() throws IOException {
    Mat U = new Mat();
    Col s = new Col();
    Mat V = new Mat();

    Arma.svd(U, s, V, _genMat);

    assertMatEquals(s, load("Arma.svd"));

    if (_genMat.is_square() && _genMat.is_finite()) {
      assertMatEquals(U.times(Arma.diagmat(s)).times(V.t()), _genMat);
    }
  }

  @Test
  public void testArmaSvd_econ() throws IOException {
    Mat U = new Mat();
    Col s = new Col();
    Mat V = new Mat();

    Arma.svd_econ(U, s, V, _genMat);

    assertMatEquals(s, load("Arma.svd_econ"));

    if (_genMat.is_square() && _genMat.is_finite()) {
      assertMatEquals(U.times(Arma.diagmat(s)).times(V.t()), _genMat);
    }
  }

  @Test
  public void testArmaNegate() throws IOException {
    assertMatEquals(Arma.negate(_genMat), load("Arma.negate"));
  }

  @Test
  public void testArmaReciprocal() throws IOException {
    assertMatEquals(Arma.reciprocal(_genMat), load("Arma.reciprocal"));
  }

  @Test
  public void testArmaAccu() throws IOException {
    double expected = load("Arma.accu")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.accu(_genMat), is(expected));
    } else {
      assertThat(Arma.accu(_genMat), is(closeTo(expected, Math.abs(expected) * 1e-10)));
    }
  }

  @Test
  public void testMatMinA() throws IOException {
    double expected = load("Mat.minValue")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(_genMat.min(), is(expected));
    } else {
      assertThat(_genMat.min(), is(closeTo(expected, Math.abs(expected) * 1e-10)));
    }
  }

  @Test
  public void testMatMinB() throws IOException {
    double expectedValue = load("Mat.minValue")._data[0];
    int expectedIndex = (int) load("Mat.minIndex")._data[0];

    int[] index = new int[1];
    double value = _genMat.min(index);

    if (Double.isInfinite(expectedValue) || Double.isNaN(expectedValue)) {
      assertThat(value, is(expectedValue));
    } else {
      assertThat(value, is(closeTo(expectedValue, Math.abs(expectedValue) * 1e-10)));
    }

    assertThat(index[0], is(expectedIndex));
  }

  @Test
  public void testMatMinC() throws IOException {
    double expectedValue = load("Mat.minValue")._data[0];
    int expectedRow = (int) load("Mat.minRow")._data[0];
    int expectedColumn = (int) load("Mat.minColumn")._data[0];

    int[] row = new int[1];
    int[] column = new int[1];
    double value = _genMat.min(row, column);

    if (Double.isInfinite(expectedValue) || Double.isNaN(expectedValue)) {
      assertThat(value, is(expectedValue));
    } else {
      assertThat(value, is(closeTo(expectedValue, Math.abs(expectedValue) * 1e-10)));
    }

    assertThat(row[0], is(expectedRow));
    assertThat(column[0], is(expectedColumn));
  }

  @Test
  public void testMatMaxA() throws IOException {
    double expected = load("Mat.maxValue")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(_genMat.max(), is(expected));
    } else {
      assertThat(_genMat.max(), is(closeTo(expected, Math.abs(expected) * 1e-10)));
    }
  }

  @Test
  public void testMatMaxB() throws IOException {
    double expectedValue = load("Mat.maxValue")._data[0];
    int expectedIndex = (int) load("Mat.maxIndex")._data[0];

    int[] index = new int[1];
    double value = _genMat.max(index);

    if (Double.isInfinite(expectedValue) || Double.isNaN(expectedValue)) {
      assertThat(value, is(expectedValue));
    } else {
      assertThat(value, is(closeTo(expectedValue, Math.abs(expectedValue) * 1e-10)));
    }

    assertThat(index[0], is(expectedIndex));
  }
  
  @Test
  public void testMatEmpty() throws IOException {
    assertThat(_genMat.empty(), is(false));
  }
  
  @Test
  public void testMatSize() throws IOException {
    assertThat(_genMat.size(), is((int) load("Mat.size")._data[0]));
  }
  
  @Test
  public void testMatIs_empty() throws IOException {
    assertThat(_genMat.is_empty(), is(false));
  }
  
  @Test
  public void testMatIs_finite() throws IOException {
    int expected = (int) load("Mat.is_finite")._data[0];
    if (_genMat.is_finite()) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }
  
  @Test
  public void testMatT() throws IOException {
    assertMatEquals(_genMat.t(), load("Mat.t"));
  }
  
  @Test
  public void testMatDiag() throws IOException {
    assertMatEquals(_genMat.diag(), load("Mat.diag"));
  }
  
  @Test
  public void testMatIs_square() throws IOException {
    int expected = (int) load("Mat.is_square")._data[0];
    if (_genMat.is_square()) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }
  
  @Test
  public void testMatIs_vec() throws IOException {
    int expected = (int) load("Mat.is_vec")._data[0];
    if (_genMat.is_vec()) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }
  
  @Test
  public void testMatIs_colvec() throws IOException {
    int expected = (int) load("Mat.is_colvec")._data[0];
    if (_genMat.is_colvec()) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }
  
  @Test
  public void testMatIs_rowvec() throws IOException {
    int expected = (int) load("Mat.is_rowvec")._data[0];
    if (_genMat.is_rowvec()) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }
  
  @Test
  public void testMatPrint() throws IOException {
    OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(byteArrayOutputStream);
    PrintStream previousStream = System.out;
 
    System.setOut(printStream);

    _genMat.print();
    System.out.flush();
    
    System.setOut(previousStream);
    
    assertThat(byteArrayOutputStream.toString().replaceAll("\\s+", " "), containsString(new String(Files.readAllBytes(Paths.get(_filepath + "Mat.print(" + _fileSuffix + ").txt")), StandardCharsets.UTF_8).replaceAll("\\s+", " ")));
  }
  
  @Test
  public void testMatToString() throws IOException {
    OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(byteArrayOutputStream);
    PrintStream previousStream = System.out;

    System.setOut(printStream);

    System.out.println(_genMat);
    System.out.flush();
    
    System.setOut(previousStream);
    
    assertThat(byteArrayOutputStream.toString().replaceAll("\\s+", " "), containsString(new String(Files.readAllBytes(Paths.get(_filepath + "Mat.print(" + _fileSuffix + ").txt")), StandardCharsets.UTF_8).replaceAll("\\s+", " "))); 
  }
  
  @Test
  public void testMatRaw_print() throws IOException {
    OutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(byteArrayOutputStream);
    PrintStream previousStream = System.out;

    System.setOut(printStream);

    _genMat.raw_print();
    System.out.flush();
    
    System.setOut(previousStream);
    
    assertThat(byteArrayOutputStream.toString().replaceAll("\\s+", " "), containsString(new String(Files.readAllBytes(Paths.get(_filepath + "Mat.raw_print(" + _fileSuffix + ").txt")), StandardCharsets.UTF_8).replaceAll("\\s+", " "))); 
    
  }
  
  @Test
  public void testMat() throws IOException {
    assertMatEquals(new Mat(_genMat), load("Mat"));
  }

}
