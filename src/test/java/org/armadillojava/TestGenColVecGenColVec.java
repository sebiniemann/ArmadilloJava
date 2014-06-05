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
  public void testArmaToeplitz() throws IOException {
    assertMatEquals(Arma.toeplitz(_genColVecA, _genColVecB), load("Arma.toeplitz"));
  }

  @Test
  public void testArmaDot() throws IOException {
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
  public void testArmaNorm_dot() throws IOException {
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
  public void testArmaConv() throws IOException {
    assertMatEquals(Arma.conv(_genColVecA, _genColVecB), load("Arma.conv"));
  }

  @Test
  public void testArmaCor() throws IOException {
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
  public void testArmaCov() throws IOException {
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
  public void testArmaCross() throws IOException {
    Col tempGenColVecA = new Col(_genColVecA);
    tempGenColVecA.resize(3);
    Col tempGenColVecB = new Col(_genColVecB);
    tempGenColVecB.resize(3);

    assertMatEquals(Arma.cross(tempGenColVecA, tempGenColVecB), load("Arma.cross"));
  }

  @Test
  public void testArmaJoin_rows() throws IOException {
    assumeThat(_genColVecA.n_rows, is(_genColVecB.n_rows));

    assertMatEquals(Arma.join_rows(_genColVecA, _genColVecB), load("Arma.join_rows"));
  }

  @Test
  public void testArmaJoin_horiz() throws IOException {
    assumeThat(_genColVecA.n_rows, is(_genColVecB.n_rows));

    assertMatEquals(Arma.join_horiz(_genColVecA, _genColVecB), load("Arma.join_horiz"));
  }

  @Test
  public void testArmaJoin_cols() throws IOException {
    assumeThat(_genColVecA.n_cols, is(_genColVecB.n_cols));

    assertMatEquals(Arma.join_cols(_genColVecA, _genColVecB), load("Arma.join_cols"));
  }

  @Test
  public void testArmaJoin_vert() throws IOException {
    assumeThat(_genColVecA.n_cols, is(_genColVecB.n_cols));

    assertMatEquals(Arma.join_vert(_genColVecA, _genColVecB), load("Arma.join_vert"));
  }

  @Test
  public void testArmaKron() throws IOException {
    assertMatEquals(Arma.kron(_genColVecA, _genColVecB), load("Arma.kron"));
  }

  @Test
  public void testColPlus() throws IOException {
    assumeThat(_genColVecA.n_elem, is(_genColVecB.n_elem));

    
    assertMatEquals(_genColVecA.plus(_genColVecB), load("Col.plus"));
  }

  @Test
  public void testColMinus() throws IOException {
    assumeThat(_genColVecA.n_elem, is(_genColVecB.n_elem));
    
    assertMatEquals(_genColVecA.minus(_genColVecB), load("Col.minus"));
  }

  @Test
  public void testColElemTimes() throws IOException {
    assumeThat(_genColVecA.n_elem, is(_genColVecB.n_elem));
    
    assertMatEquals(_genColVecA.elemTimes(_genColVecB), load("Col.elemTimes"));
  }

  @Test
  public void testColElemDivide() throws IOException {
    assumeThat(_genColVecA.n_elem, is(_genColVecB.n_elem));
    
    assertMatEquals(_genColVecA.elemDivide(_genColVecB), load("Col.elemDivide"));
  }

  @Test
  public void testColEquals() throws IOException {
    assumeThat(_genColVecA.n_elem, is(_genColVecB.n_elem));
    
    assertMatEquals(_genColVecA.equals(_genColVecB), load("Col.equals"));
  }

  @Test
  public void testColNonEquals() throws IOException {
    assumeThat(_genColVecA.n_elem, is(_genColVecB.n_elem));
    
    assertMatEquals(_genColVecA.nonEquals(_genColVecB), load("Col.nonEquals"));
  }

  @Test
  public void testColGreaterThan() throws IOException {
    assumeThat(_genColVecA.n_elem, is(_genColVecB.n_elem));
    
    assertMatEquals(_genColVecA.greaterThan(_genColVecB), load("Col.greaterThan"));
  }

  @Test
  public void testColLessThan() throws IOException {
    assumeThat(_genColVecA.n_elem, is(_genColVecB.n_elem));
    
    assertMatEquals(_genColVecA.lessThan(_genColVecB), load("Col.lessThan"));
  }

  @Test
  public void testColStrictGreaterThan() throws IOException {
    assumeThat(_genColVecA.n_elem, is(_genColVecB.n_elem));
    
    assertMatEquals(_genColVecA.strictGreaterThan(_genColVecB), load("Col.strictGreaterThan"));
  }

  @Test
  public void testColStrictLessThan() throws IOException {
    assumeThat(_genColVecA.n_rows, is(_genColVecB.n_rows));
    
    assertMatEquals(_genColVecA.strictLessThan(_genColVecB), load("Col.strictLessThan"));
  }
}
