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
  public void testArmaCross() throws IOException {
    Mat tempGenMat = new Mat(_genMat);
    tempGenMat.resize(3, 1);
    Row tempGenRowVec = new Row(_genRowVec);
    tempGenRowVec.resize(3);
    
    assertMatEquals(Arma.cross(tempGenMat, tempGenRowVec), load("Arma.cross"));
  }

  @Test
  public void testArmaJoin_rows() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));

    assertMatEquals(Arma.join_rows(_genMat, _genRowVec), load("Arma.join_rows"));
  }

  @Test
  public void testArmaJoin_horiz() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));

    assertMatEquals(Arma.join_horiz(_genMat, _genRowVec), load("Arma.join_horiz"));
  }

  @Test
  public void testArmaJoin_cols() throws IOException {
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));

    assertMatEquals(Arma.join_cols(_genMat, _genRowVec), load("Arma.join_cols"));
  }

  @Test
  public void testArmaJoin_vert() throws IOException {
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));

    assertMatEquals(Arma.join_vert(_genMat, _genRowVec), load("Arma.join_vert"));
  }

  @Test
  public void testArmaKron() throws IOException {
    assertMatEquals(Arma.kron(_genMat, _genRowVec), load("Arma.kron"));
  }

  @Test
  public void testMatPlus() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));
    
    assertMatEquals(_genMat.plus(_genRowVec), load("Mat.plus"));
  }

  @Test
  public void testMatMinus() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));
    
    assertMatEquals(_genMat.minus(_genRowVec), load("Mat.minus"));
  }
  
  @Test
  public void testMatTimes() throws IOException {
    assumeThat(_genMat.n_cols, is(_genRowVec.n_rows));
    assumeThat(_genMat.is_finite(), is(true));
    assumeThat(_genRowVec.is_finite(), is(true));
    
    assertMatEquals(_genMat.times(_genRowVec), load("Mat.times"));
  }

  @Test
  public void testMatElemTimes() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));
    
    assertMatEquals(_genMat.elemTimes(_genRowVec), load("Mat.elemTimes"));
  }

  @Test
  public void testMatElemDivide() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));
    
    assertMatEquals(_genMat.elemDivide(_genRowVec), load("Mat.elemDivide"));
  }

  @Test
  public void testMatEquals() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));
    
    assertMatEquals(_genMat.equals(_genRowVec), load("Mat.equals"));
  }

  @Test
  public void testMatNonEquals() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));
    
    assertMatEquals(_genMat.nonEquals(_genRowVec), load("Mat.nonEquals"));
  }

  @Test
  public void testMatGreaterThan() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));
    
    assertMatEquals(_genMat.greaterThan(_genRowVec), load("Mat.greaterThan"));
  }

  @Test
  public void testMatLessThan() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));
    
    assertMatEquals(_genMat.lessThan(_genRowVec), load("Mat.lessThan"));
  }

  @Test
  public void testMatStrictGreaterThan() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));
    
    assertMatEquals(_genMat.strictGreaterThan(_genRowVec), load("Mat.strictGreaterThan"));
  }

  @Test
  public void testMatStrictLessThan() throws IOException {
    assumeThat(_genMat.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genRowVec.n_cols));
    
    assertMatEquals(_genMat.strictLessThan(_genRowVec), load("Mat.strictLessThan"));
  }

}
