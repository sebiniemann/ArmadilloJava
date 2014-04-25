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
public class TestGenRowVecGenColVec extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, GenColVec = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.GenColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Parameter(2)
  public String _genColVecString;

  @Parameter(3)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _genColVecString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfGenColVec = new Col(_genColVec);
  }

  @After
  public void after() {
    assertMatEquals(_genRowVec, _copyOfGenRowVec, 0);
    assertMatEquals(_genColVec, _copyOfGenColVec, 0);
  }

  @Test
  public void testArmaToeplitz() throws IOException {
    assertMatEquals(Arma.toeplitz(_genRowVec, _genColVec), load("Arma.toeplitz"));
  }

  @Test
  public void testArmaDot() throws IOException {
    assumeThat(_genRowVec.n_elem, is(_genColVec.n_elem));

    double expected = load("Arma.dot")._data[0];
    double actual = Arma.dot(_genRowVec, _genColVec);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaNorm_dot() throws IOException {
    assumeThat(_genRowVec.n_elem, is(_genColVec.n_elem));

    double expected = load("Arma.norm_dot")._data[0];
    double actual = Arma.norm_dot(_genRowVec, _genColVec);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaConv() throws IOException {
    assertMatEquals(Arma.conv(_genRowVec, _genColVec), load("Arma.conv"));
  }

  @Test
  public void testArmaCor() throws IOException {
    assumeThat(_genRowVec.n_rows, is(_genColVec.n_rows));
    assumeThat(_genRowVec.n_cols, is(_genColVec.n_cols));

    double expected = load("Arma.cor")._data[0];
    double actual = Arma.cor(_genRowVec, _genColVec);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaCov() throws IOException {
    assumeThat(_genRowVec.n_rows, is(_genColVec.n_rows));
    assumeThat(_genRowVec.n_cols, is(_genColVec.n_cols));

    double expected = load("Arma.cov")._data[0];
    double actual = Arma.cov(_genRowVec, _genColVec);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testArmaCross() throws IOException {
    assumeThat(_genRowVec.is_vec(), is(true));
    assumeThat(_genRowVec.n_elem, is(3));
    assumeThat(_genColVec.is_vec(), is(true));
    assumeThat(_genColVec.n_elem, is(3));

    assertMatEquals(Arma.cross(_genRowVec, _genColVec), load("Arma.cross"));
  }

  @Test
  public void testArmaJoin_rows() throws IOException {
    assumeThat(_genRowVec.n_rows, is(_genColVec.n_rows));

    assertMatEquals(Arma.join_rows(_genRowVec, _genColVec), load("Arma.join_rows"));
  }

  @Test
  public void testArmaJoin_horiz() throws IOException {
    assumeThat(_genRowVec.n_rows, is(_genColVec.n_rows));

    assertMatEquals(Arma.join_horiz(_genRowVec, _genColVec), load("Arma.join_horiz"));
  }

  @Test
  public void testArmaJoin_cols() throws IOException {
    assumeThat(_genRowVec.n_cols, is(_genColVec.n_cols));

    assertMatEquals(Arma.join_cols(_genRowVec, _genColVec), load("Arma.join_cols"));
  }

  @Test
  public void testArmaJoin_vert() throws IOException {
    assumeThat(_genRowVec.n_cols, is(_genColVec.n_cols));

    assertMatEquals(Arma.join_vert(_genRowVec, _genColVec), load("Arma.join_vert"));
  }

  @Test
  public void testArmaKron() throws IOException {
    assertMatEquals(Arma.kron(_genRowVec, _genColVec), load("Arma.kron"));
  }

}
