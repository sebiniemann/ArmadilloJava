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
import static org.junit.Assume.assumeThat;
import static org.hamcrest.CoreMatchers.is;

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
public class TestGenMatGenMat extends TestClass {

  @Parameters(name = "{index}: GenMatA = {0}, GenMatB = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatAString;

  @Parameter(1)
  public Mat    _genMatA;

  protected Mat _copyOfGenMatA;

  @Parameter(2)
  public String _genMatBString;

  @Parameter(3)
  public Mat    _genMatB;

  protected Mat _copyOfGenMatB;

  @Before
  public void before() {
    _fileSuffix = _genMatAString + "," + _genMatBString;

    _copyOfGenMatA = new Mat(_genMatA);
    _copyOfGenMatB = new Mat(_genMatB);
  }

  @After
  public void after() {
    assertMatEquals(_genMatA, _copyOfGenMatA, 0);
    assertMatEquals(_genMatB, _copyOfGenMatB, 0);
  }

  @Test
  public void testArmaMin() throws IOException {
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    assertMatEquals(Arma.min(_genMatA, _genMatB), load("Arma.min"));
  }

  @Test
  public void testArmaMax() throws IOException {
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    assertMatEquals(Arma.max(_genMatA, _genMatB), load("Arma.max"));
  }

  @Test
  public void testArmaCor() throws IOException {
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    assertMatEquals(Arma.cor(_genMatA, _genMatB), load("Arma.cor"));
  }

  @Test
  public void testArmaCov() throws IOException {
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    assertMatEquals(Arma.cov(_genMatA, _genMatB), load("Arma.cov"));
  }

  @Test
  public void testArmaCross() throws IOException {
    assumeThat(_genMatA.is_vec(), is(true));
    assumeThat(_genMatA.n_elem, is(3));
    assumeThat(_genMatB.is_vec(), is(true));
    assumeThat(_genMatB.n_elem, is(3));

    assertMatEquals(Arma.cross(_genMatA, _genMatB), load("Arma.cross"));
  }

  @Test
  public void testArmaJoin_rows() throws IOException {
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));

    assertMatEquals(Arma.join_rows(_genMatA, _genMatB), load("Arma.join_rows"));
  }

  @Test
  public void testArmaJoin_horiz() throws IOException {
    assumeThat(_genMatA.n_rows, is(_genMatB.n_rows));

    assertMatEquals(Arma.join_horiz(_genMatA, _genMatB), load("Arma.join_horiz"));
  }

  @Test
  public void testArmaJoin_cols() throws IOException {
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    assertMatEquals(Arma.join_cols(_genMatA, _genMatB), load("Arma.join_cols"));
  }

  @Test
  public void testArmaJoin_vert() throws IOException {
    assumeThat(_genMatA.n_cols, is(_genMatB.n_cols));

    assertMatEquals(Arma.join_vert(_genMatA, _genMatB), load("Arma.join_vert"));
  }

  @Test
  public void testArmaKron() throws IOException {
    assertMatEquals(Arma.kron(_genMatA, _genMatB), load("Arma.kron"));
  }

}
