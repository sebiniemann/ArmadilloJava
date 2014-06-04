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
public class TestGenRowVecGenMat extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, GenMat = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Parameter(2)
  public String _genMatString;

  @Parameter(3)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _genMatString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfGenMat = new Mat(_genMat);
  }

  @After
  public void after() {
    assertMatEquals(_genRowVec, _copyOfGenRowVec, 0);
    assertMatEquals(_genMat, _copyOfGenMat, 0);
  }

  @Test
  public void testArmaCross() throws IOException {
    Row tempGenRowVec = new Row(_genRowVec);
    tempGenRowVec.resize(3);
    Mat tempGenMat = new Mat(_genMat);
    tempGenMat.resize(3, 1);
    
    assertMatEquals(Arma.cross(tempGenRowVec, tempGenMat), load("Arma.cross"));
  }

  @Test
  public void testArmaJoin_rows() throws IOException {
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));

    assertMatEquals(Arma.join_rows(_genRowVec, _genMat), load("Arma.join_rows"));
  }

  @Test
  public void testArmaJoin_horiz() throws IOException {
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));

    assertMatEquals(Arma.join_horiz(_genRowVec, _genMat), load("Arma.join_horiz"));
  }

  @Test
  public void testArmaJoin_cols() throws IOException {
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    assertMatEquals(Arma.join_cols(_genRowVec, _genMat), load("Arma.join_cols"));
  }

  @Test
  public void testArmaJoin_vert() throws IOException {
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    assertMatEquals(Arma.join_vert(_genRowVec, _genMat), load("Arma.join_vert"));
  }

  @Test
  public void testArmaKron() throws IOException {
    assertMatEquals(Arma.kron(_genRowVec, _genMat), load("Arma.kron"));
  }

  @Test
  public void testMatPlus() throws IOException {
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    
    assertMatEquals(_genRowVec.plus(_genMat), load("Row.plus"));
  }

  @Test
  public void testMatMinus() throws IOException {
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    
    assertMatEquals(_genRowVec.minus(_genMat), load("Row.minus"));
  }
  
  @Test
  public void testMatTimes() throws IOException {
    assumeThat(_genRowVec.n_cols, is(_genMat.n_rows));
    assumeThat(_genMat.is_finite(), is(true));
    assumeThat(_genRowVec.is_finite(), is(true));
    
    assertMatEquals(_genRowVec.times(_genMat), load("Row.times"));
  }

  @Test
  public void testMatElemTimes() throws IOException {
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    
    assertMatEquals(_genRowVec.elemTimes(_genMat), load("Row.elemTimes"));
  }

  @Test
  public void testMatElemDivide() throws IOException {
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    
    assertMatEquals(_genRowVec.elemDivide(_genMat), load("Row.elemDivide"));
  }

  @Test
  public void testMatEquals() throws IOException {
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    
    assertMatEquals(_genRowVec.equals(_genMat), load("Row.equals"));
  }

  @Test
  public void testMatNonEquals() throws IOException {
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    
    assertMatEquals(_genRowVec.nonEquals(_genMat), load("Row.nonEquals"));
  }

  @Test
  public void testMatGreaterThan() throws IOException {
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    
    assertMatEquals(_genRowVec.greaterThan(_genMat), load("Row.greaterThan"));
  }

  @Test
  public void testMatLessThan() throws IOException {
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    
    assertMatEquals(_genRowVec.lessThan(_genMat), load("Row.lessThan"));
  }

  @Test
  public void testMatStrictGreaterThan() throws IOException {
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    
    assertMatEquals(_genRowVec.strictGreaterThan(_genMat), load("Row.strictGreaterThan"));
  }

  @Test
  public void testMatStrictLessThan() throws IOException {
    assumeThat(_genRowVec.n_rows, is(_genMat.n_rows));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));
    
    assertMatEquals(_genRowVec.strictLessThan(_genMat), load("Row.strictLessThan"));
  }

}
