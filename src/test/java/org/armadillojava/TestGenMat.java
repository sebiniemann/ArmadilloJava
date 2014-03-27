package org.armadillojava;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.number.IsCloseTo.*;
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
public class TestGenMat extends TestClass {

  @Parameters(name = "{index}: _genMat = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  @Before
  public void before() {
    _fileSuffix = _genMatString;
  }

  @Test
  public void testAbs() throws IOException {
    assertMatEquals(Arma.abs(_genMat), load("abs"));
  }

  @Test
  public void testEps() throws IOException {
    assertMatEquals(Arma.eps(_genMat), load("eps"));
  }

  @Test
  public void testExp() throws IOException {
    assertMatEquals(Arma.exp(_genMat), load("exp"));
  }

  @Test
  public void testExp2() throws IOException {
    assertMatEquals(Arma.exp2(_genMat), load("exp2"));
  }

  @Test
  public void testExp10() throws IOException {
    assertMatEquals(Arma.exp10(_genMat), load("exp10"));
  }

  @Test
  public void testTrunc_exp() throws IOException {
    assertMatEquals(Arma.trunc_exp(_genMat), load("trunc_exp"));
  }

  @Test
  public void testLog() throws IOException {
    assertMatEquals(Arma.log(_genMat), load("log"));
  }

  @Test
  public void testLog2() throws IOException {
    assertMatEquals(Arma.log2(_genMat), load("log2"));
  }

  @Test
  public void testLog10() throws IOException {
    assertMatEquals(Arma.log10(_genMat), load("log10"));
  }

  @Test
  public void testTrunc_log() throws IOException {
    assertMatEquals(Arma.trunc_log(_genMat), load("trunc_log"));
  }

  @Test
  public void testSquare() throws IOException {
    assertMatEquals(Arma.square(_genMat), load("square"));
  }

  @Test
  public void testFloor() throws IOException {
    assertMatEquals(Arma.floor(_genMat), load("floor"));
  }

  @Test
  public void testCeil() throws IOException {
    assertMatEquals(Arma.ceil(_genMat), load("ceil"));
  }

  @Test
  public void testRound() throws IOException {
    assertMatEquals(Arma.round(_genMat), load("round"));
  }

  @Test
  public void testSign() throws IOException {
    assertMatEquals(Arma.sign(_genMat), load("sign"));
  }

  @Test
  public void testSin() throws IOException {
    assertMatEquals(Arma.sin(_genMat), load("sin"));
  }

  @Test
  public void testAsin() throws IOException {
    assertMatEquals(Arma.asin(_genMat), load("asin"));
  }

  @Test
  public void testSinh() throws IOException {
    assertMatEquals(Arma.sinh(_genMat), load("sinh"));
  }

  @Test
  public void testAsinh() throws IOException {
    assertMatEquals(Arma.asinh(_genMat), load("asinh"));
  }

  @Test
  public void testCos() throws IOException {
    assertMatEquals(Arma.cos(_genMat), load("cos"));
  }

  @Test
  public void testAcos() throws IOException {
    assertMatEquals(Arma.acos(_genMat), load("acos"));
  }

  @Test
  public void testCosh() throws IOException {
    assertMatEquals(Arma.cosh(_genMat), load("cosh"));
  }

  @Test
  public void testAcosh() throws IOException {
    assertMatEquals(Arma.acosh(_genMat), load("acosh"));
  }

  @Test
  public void testTan() throws IOException {
    assertMatEquals(Arma.tan(_genMat), load("tan"));
  }

  @Test
  public void testAtan() throws IOException {
    assertMatEquals(Arma.atan(_genMat), load("atan"));
  }

  @Test
  public void testTanh() throws IOException {
    assertMatEquals(Arma.tanh(_genMat), load("tanh"));
  }

  @Test
  public void testAtanh() throws IOException {
    assertMatEquals(Arma.atanh(_genMat), load("atanh"));
  }

  @Test
  public void testCond() throws IOException {
    double expected = load("cond")._data[0];
    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(Arma.cond(_genMat), is(expected));
    } else {
      assertThat(Arma.cond(_genMat), is(closeTo(expected, Math.abs(expected) * 1e-10)));
    }
  }

  @Test
  public void testRank() throws IOException {
    int expected = (int) load("rank")._data[0];
    assertThat(Arma.rank(_genMat), is(expected));
  }

  @Test
  public void testDiagvec() throws IOException {
    assertMatEquals(Arma.diagvec(_genMat), load("diagvec"));
  }

  @Test
  public void testMin() throws IOException {
    assertMatEquals(Arma.min(Row.class, _genMat), load("min"));
    assertMatEquals(Arma.min(Col.class, _genMat), load("min").t());
  }

  @Test
  public void testMax() throws IOException {
    assertMatEquals(Arma.max(Row.class, _genMat), load("max"));
    assertMatEquals(Arma.max(Col.class, _genMat), load("max").t());
  }

  @Test
  public void testProd() throws IOException {
    assertMatEquals(Arma.prod(Row.class, _genMat), load("prod"));
    assertMatEquals(Arma.prod(Col.class, _genMat), load("prod").t());
  }

  @Test
  public void testMean() throws IOException {
    assertMatEquals(Arma.mean(Row.class, _genMat), load("mean"));
    assertMatEquals(Arma.mean(Col.class, _genMat), load("mean").t());
  }

  @Test
  public void testMedian() throws IOException {
    assertMatEquals(Arma.median(Row.class, _genMat), load("median"));
    assertMatEquals(Arma.median(Col.class, _genMat), load("median").t());
  }

  @Test
  public void testStddev() throws IOException {
    assertMatEquals(Arma.stddev(Row.class, _genMat), load("stddev"));
    assertMatEquals(Arma.stddev(Col.class, _genMat), load("stddev").t());
  }

  @Test
  public void testVar() throws IOException {
    assertMatEquals(Arma.var(Row.class, _genMat), load("var"));
    assertMatEquals(Arma.var(Col.class, _genMat), load("var").t());
  }

  @Test
  public void testCor() throws IOException {
    assertMatEquals(Arma.cor(_genMat), load("cor"));
  }

  @Test
  public void testCov() throws IOException {
    assertMatEquals(Arma.cov(_genMat), load("cov"));
  }

  @Test
  public void testCumsum() throws IOException {
    assertMatEquals(Arma.cumsum(_genMat), load("cumsum"));
  }

  @Test
  public void testFliplr() throws IOException {
    assertMatEquals(Arma.fliplr(_genMat), load("fliplr"));
  }

  @Test
  public void testFlipud() throws IOException {
    assertMatEquals(Arma.flipud(_genMat), load("flipud"));
  }

  @Test
  public void testHist() throws IOException {
    assertMatEquals(Arma.hist(_genMat), load("hist"));
  }

  @Test
  public void testSort() throws IOException {
    assertMatEquals(Arma.sort(_genMat), load("sort"));
  }

  @Test
  public void testTrans() throws IOException {
    assertMatEquals(Arma.trans(_genMat), load("trans"));
  }

  @Test
  public void testUnique() throws IOException {
    assertMatEquals(Arma.unique(_genMat), load("unique"));
  }

  @Test
  public void testVectorise() throws IOException {
    assertMatEquals(Arma.vectorise(Col.class, _genMat), load("vectorise"));
    assertMatEquals(Arma.vectorise(Row.class, _genMat), load("vectorise").t());
  }

  @Test
  public void testLu1() throws IOException {
    assumeThat(load("lu")._data[0], is(1.0));

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

    assertMatEquals((P.t()).times(L).times(U), _genMat);
  }

  @Test
  public void testLu2() throws IOException {
    assumeThat(load("lu")._data[0], is(1.0));

    Mat L = new Mat();
    Mat U = new Mat();

    Arma.lu(L, U, _genMat);
    
    if (_genMat.is_square()) {
      assertMatEquals(U, Arma.trimatu(U));
    }
    assertMatEquals(L.times(U), _genMat);
  }

  @Test
  public void testPinv() throws IOException {
    assertMatEquals(Arma.pinv(_genMat), load("pinv"), TestUtil.globalDelta(load("pinv"), 1e-12));
  }

  @Test
  public void testPrincompA() throws IOException {
    Mat coeff = new Mat();
    Mat score = new Mat();
    Col latent = new Col();
    Col tsquared = new Col();

    Arma.princomp(coeff, score, latent, tsquared, _genMat);

    assertMatEquals(latent, load("princompLatent"));
  }

  @Test
  public void testPrincompB() throws IOException {
    Mat coeff = new Mat();
    Mat score = new Mat();
    Col latent = new Col();

    Arma.princomp(coeff, score, latent, _genMat);

    assertMatEquals(latent, load("princompLatent"));
  }

  @Test
  public void testQr() throws IOException {
    Mat Q = new Mat();
    Mat R = new Mat();

    Arma.qr(Q, R, _genMat);

    if(_genMat.is_square()) {
      assertMatEquals(Q.t(), Q.i());
      assertMatEquals(Arma.trimatu(R), R);
    }
    assertMatEquals(Q.times(R), _genMat);
  }
  
  @Test
  public void testQr_econ() throws IOException {
    Mat Q = new Mat();
    Mat R = new Mat();

    Arma.qr_econ(Q, R, _genMat);

    if(_genMat.n_rows <= _genMat.n_cols && _genMat.is_square()) {
      assertMatEquals(Q.t(), Q.i());
      assertMatEquals(Arma.trimatu(R), R);
    }
    assertMatEquals(Q.times(R), _genMat);
  }

  @Test
  public void testSvd() throws IOException {
    Mat U = new Mat();
    Col s = new Col();
    Mat V = new Mat();

    Arma.svd(U, s, V, _genMat);

    assertMatEquals(s, load("svd"));

    if (_genMat.is_square()) {
      assertMatEquals(U.times(Arma.diagmat(s)).times(V.t()), _genMat);
    }
  }

  @Test
  public void testSvd_econ() throws IOException {
    Mat U = new Mat();
    Col s = new Col();
    Mat V = new Mat();

    Arma.svd_econ(U, s, V, _genMat);

    assertMatEquals(s, load("svd_econ"));

    if (_genMat.is_square()) {
      assertMatEquals(U.times(Arma.diagmat(s)).times(V.t()), _genMat);
    }
  }

  @Test
  public void testNegate() throws IOException {
    assertMatEquals(Arma.negate(_genMat), load("negate"));
  }

  @Test
  public void testReciprocal() throws IOException {
    assertMatEquals(Arma.reciprocal(_genMat), load("reciprocal"));
  }
}
