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
public class TestGenColVecGenColVec extends TestClass {

  @Parameters(name = "{index}: GenColVecA = {0}, GenColVecB = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.GenColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genColVecAString;

  @Parameter(1)
  public Col    _genColVecA;

  protected Col _copyOfGenColVecA;

  @Parameter(2)
  public String _genColVecBString;

  @Parameter(3)
  public Col    _genColVecB;

  protected Col _copyOfGenColVecB;

  @Before
  public void before() {
    _fileSuffix = _genColVecAString + "," + _genColVecBString;

    _copyOfGenColVecA = new Col(_genColVecA);
    _copyOfGenColVecB = new Col(_genColVecB);
  }

  @After
  public void after() {
    assertMatEquals(_genColVecA, _copyOfGenColVecA, 0);
    assertMatEquals(_genColVecB, _copyOfGenColVecB, 0);
  }

  @Test
  public void testToeplitz() throws IOException {
    assertMatEquals(Arma.toeplitz(_genColVecA, _genColVecB), load("Arma.toeplitz"));
  }

  @Test
  public void testDot() throws IOException {
    assumeThat(_genColVecA.n_elem, is(_genColVecB.n_elem));

    double expected = load("Arma.dot")._data[0];
    double actual = Arma.dot(_genColVecA, _genColVecB);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testNorm_dot() throws IOException {
    assumeThat(_genColVecA.n_elem, is(_genColVecB.n_elem));

    double expected = load("Arma.norm_dot")._data[0];
    double actual = Arma.norm_dot(_genColVecA, _genColVecB);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testConv() throws IOException {
    assertMatEquals(Arma.conv(_genColVecA, _genColVecB), load("Arma.conv"));
  }

  @Test
  public void testCor() throws IOException {
    assumeThat(_genColVecA.n_rows, is(_genColVecB.n_rows));
    assumeThat(_genColVecA.n_cols, is(_genColVecB.n_cols));

    double expected = load("Arma.cor")._data[0];
    double actual = Arma.cor(_genColVecA, _genColVecB);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testCov() throws IOException {
    assumeThat(_genColVecA.n_rows, is(_genColVecB.n_rows));
    assumeThat(_genColVecA.n_cols, is(_genColVecB.n_cols));

    double expected = load("Arma.cov")._data[0];
    double actual = Arma.cov(_genColVecA, _genColVecB);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testCross() throws IOException {
    assumeThat(_genColVecA.is_vec(), is(true));
    assumeThat(_genColVecA.n_elem, is(3));
    assumeThat(_genColVecB.is_vec(), is(true));
    assumeThat(_genColVecB.n_elem, is(3));

    assertMatEquals(Arma.cross(_genColVecA, _genColVecB), load("Arma.cross"));
  }

  @Test
  public void testJoin_rows() throws IOException {
    assumeThat(_genColVecA.n_rows, is(_genColVecB.n_rows));

    assertMatEquals(Arma.join_rows(_genColVecA, _genColVecB), load("Arma.join_rows"));
  }

  @Test
  public void testJoin_horiz() throws IOException {
    assumeThat(_genColVecA.n_rows, is(_genColVecB.n_rows));

    assertMatEquals(Arma.join_horiz(_genColVecA, _genColVecB), load("Arma.join_horiz"));
  }

  @Test
  public void testJoin_cols() throws IOException {
    assumeThat(_genColVecA.n_cols, is(_genColVecB.n_cols));

    assertMatEquals(Arma.join_cols(_genColVecA, _genColVecB), load("Arma.join_cols"));
  }

  @Test
  public void testJoin_vert() throws IOException {
    assumeThat(_genColVecA.n_cols, is(_genColVecB.n_cols));

    assertMatEquals(Arma.join_vert(_genColVecA, _genColVecB), load("Arma.join_vert"));
  }

  @Test
  public void testKron() throws IOException {
    assertMatEquals(Arma.kron(_genColVecA, _genColVecB), load("Arma.kron"));
  }

}
