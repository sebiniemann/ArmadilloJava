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
public class TestGenMatGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, GenRowVec = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _genRowVecString;

  @Parameter(3)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _genRowVecString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertMatEquals(_genRowVec, _copyOfGenRowVec, 0);
  }

  @Test
  public void testCross() throws IOException {
    assumeThat(_genMat.is_vec(), is(true));
    assumeThat(_genMat.n_elem, is(3));
    assumeThat(_genRowVec.is_vec(), is(true));
    assumeThat(_genRowVec.n_elem, is(3));

    assertMatEquals(Arma.cross(_genMat, _genRowVec), load("Arma.cross"));
  }

  @Test
  public void testJoin_rows() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));

    assertMatEquals(Arma.join_rows(_genMat, _genRowVec), load("Arma.join_rows"));
  }

  @Test
  public void testJoin_horiz() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));

    assertMatEquals(Arma.join_horiz(_genMat, _genRowVec), load("Arma.join_horiz"));
  }

  @Test
  public void testJoin_cols() throws IOException {
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));

    assertMatEquals(Arma.join_cols(_genMat, _genRowVec), load("Arma.join_cols"));
  }

  @Test
  public void testJoin_vert() throws IOException {
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));

    assertMatEquals(Arma.join_vert(_genMat, _genRowVec), load("Arma.join_vert"));
  }

  @Test
  public void testKron() throws IOException {
    assertMatEquals(Arma.kron(_genMat, _genRowVec), load("Arma.kron"));
  }

}
