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
public class TestGenColVecGenMat extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, GenMat = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genColVecString;

  @Parameter(1)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;

  @Parameter(2)
  public String _genRowVecString;

  @Parameter(3)
  public Mat    _genRowVec;

  protected Mat _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _genRowVecString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfGenRowVec = new Mat(_genRowVec);
  }

  @After
  public void after() {
    assertMatEquals(_genColVec, _copyOfGenColVec, 0);
    assertMatEquals(_genRowVec, _copyOfGenRowVec, 0);
  }

  @Test
  public void testCross() throws IOException {
    assumeThat(_genColVec.is_vec(), is(true));
    assumeThat(_genColVec.n_elem, is(3));
    assumeThat(_genRowVec.is_vec(), is(true));
    assumeThat(_genRowVec.n_elem, is(3));

    assertMatEquals(Arma.cross(_genColVec, _genRowVec), load("cross"));
  }

  @Test
  public void testJoin_rows() throws IOException {
    assumeThat(_genColVec.n_rows, is(_genRowVec.n_rows));

    assertMatEquals(Arma.join_rows(_genColVec, _genRowVec), load("join_rows"));
  }

  @Test
  public void testJoin_horiz() throws IOException {
    assumeThat(_genColVec.n_rows, is(_genRowVec.n_rows));

    assertMatEquals(Arma.join_horiz(_genColVec, _genRowVec), load("join_horiz"));
  }

  @Test
  public void testJoin_cols() throws IOException {
    assumeThat(_genColVec.n_cols, is(_genRowVec.n_cols));

    assertMatEquals(Arma.join_cols(_genColVec, _genRowVec), load("join_cols"));
  }

  @Test
  public void testJoin_vert() throws IOException {
    assumeThat(_genColVec.n_cols, is(_genRowVec.n_cols));

    assertMatEquals(Arma.join_vert(_genColVec, _genRowVec), load("join_vert"));
  }

  @Test
  public void testKron() throws IOException {
    assertMatEquals(Arma.kron(_genColVec, _genRowVec), load("kron"));
  }

}
