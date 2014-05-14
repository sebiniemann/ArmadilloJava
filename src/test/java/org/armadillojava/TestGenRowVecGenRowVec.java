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
public class TestGenRowVecGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenRowVecA = {0}, GenRowVecB = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecAString;

  @Parameter(1)
  public Row    _genRowVecA;

  protected Row _copyOfGenRowVecA;

  @Parameter(2)
  public String _genRowVecBString;

  @Parameter(3)
  public Row    _genRowVecB;

  protected Row _copyOfGenRowVecB;

  @Before
  public void before() {
    _fileSuffix = _genRowVecAString + "," + _genRowVecBString;

    _copyOfGenRowVecA = new Row(_genRowVecA);
    _copyOfGenRowVecB = new Row(_genRowVecB);
  }

  @After
  public void after() {
    assertMatEquals(_genRowVecA, _copyOfGenRowVecA, 0);
    assertMatEquals(_genRowVecB, _copyOfGenRowVecB, 0);
  }

  @Test
  public void testArmaToeplitz() throws IOException {
    assertMatEquals(Arma.toeplitz(_genRowVecA, _genRowVecB), load("Arma.toeplitz"));
  }

  @Test
  public void testArmaDot() throws IOException {
    assumeThat(_genRowVecA.n_elem, is(_genRowVecB.n_elem));

    double expected = load("Arma.dot")._data[0];
    double actual = Arma.dot(_genRowVecA, _genRowVecB);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaNorm_dot() throws IOException {
    assumeThat(_genRowVecA.n_elem, is(_genRowVecB.n_elem));

    double expected = load("Arma.norm_dot")._data[0];
    double actual = Arma.norm_dot(_genRowVecA, _genRowVecB);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaConv() throws IOException {
    assertMatEquals(Arma.conv(_genRowVecA, _genRowVecB), load("Arma.conv"));
  }

  @Test
  public void testArmaCor() throws IOException {
    assumeThat(_genRowVecA.n_rows, is(_genRowVecB.n_rows));
    assumeThat(_genRowVecA.n_cols, is(_genRowVecB.n_cols));

    double expected = load("Arma.cor")._data[0];
    double actual = Arma.cor(_genRowVecA, _genRowVecB);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaCov() throws IOException {
    assumeThat(_genRowVecA.n_rows, is(_genRowVecB.n_rows));
    assumeThat(_genRowVecA.n_cols, is(_genRowVecB.n_cols));

    double expected = load("Arma.cov")._data[0];
    double actual = Arma.cov(_genRowVecA, _genRowVecB);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaCross() throws IOException {
    Row tempGenRowVecA = new Row(_genRowVecA);
    tempGenRowVecA.resize(3);
    Row tempGenRowVecB = new Row(_genRowVecB);
    tempGenRowVecB.resize(3);

    assertMatEquals(Arma.cross(tempGenRowVecA, tempGenRowVecB), load("Arma.cross"));
  }

  @Test
  public void testArmaJoin_rows() throws IOException {
    assumeThat(_genRowVecA.n_rows, is(_genRowVecB.n_rows));

    assertMatEquals(Arma.join_rows(_genRowVecA, _genRowVecB), load("Arma.join_rows"));
  }

  @Test
  public void testArmaJoin_horiz() throws IOException {
    assumeThat(_genRowVecA.n_rows, is(_genRowVecB.n_rows));

    assertMatEquals(Arma.join_horiz(_genRowVecA, _genRowVecB), load("Arma.join_horiz"));
  }

  @Test
  public void testArmaJoin_cols() throws IOException {
    assumeThat(_genRowVecA.n_cols, is(_genRowVecB.n_cols));

    assertMatEquals(Arma.join_cols(_genRowVecA, _genRowVecB), load("Arma.join_cols"));
  }

  @Test
  public void testArmaJoin_vert() throws IOException {
    assumeThat(_genRowVecA.n_cols, is(_genRowVecB.n_cols));

    assertMatEquals(Arma.join_vert(_genRowVecA, _genRowVecB), load("Arma.join_vert"));
  }

  @Test
  public void testArmaKron() throws IOException {
    assertMatEquals(Arma.kron(_genRowVecA, _genRowVecB), load("Arma.kron"));
  }

}
