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
  public String _genMatString;

  @Parameter(3)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _genMatString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfGenMat = new Mat(_genMat);
  }

  @After
  public void after() {
    assertMatEquals(_genColVec, _copyOfGenColVec, 0);
    assertMatEquals(_genMat, _copyOfGenMat, 0);
  }

  @Test
  public void testArmaCross() throws IOException {
    Col tempGenColVec = new Col(_genColVec);
    tempGenColVec.resize(3);
    Mat tempGenMat = new Mat(_genMat);
    tempGenMat.resize(3, 1);

    assertMatEquals(Arma.cross(tempGenColVec, tempGenMat), load("Arma.cross"));
  }

  @Test
  public void testArmaJoin_rows() throws IOException {
    assumeThat(_genColVec.n_rows, is(_genMat.n_rows));

    assertMatEquals(Arma.join_rows(_genColVec, _genMat), load("Arma.join_rows"));
  }

  @Test
  public void testArmaJoin_horiz() throws IOException {
    assumeThat(_genColVec.n_rows, is(_genMat.n_rows));

    assertMatEquals(Arma.join_horiz(_genColVec, _genMat), load("Arma.join_horiz"));
  }

  @Test
  public void testArmaJoin_cols() throws IOException {
    assumeThat(_genColVec.n_cols, is(_genMat.n_cols));

    assertMatEquals(Arma.join_cols(_genColVec, _genMat), load("Arma.join_cols"));
  }

  @Test
  public void testArmaJoin_vert() throws IOException {
    assumeThat(_genColVec.n_cols, is(_genMat.n_cols));

    assertMatEquals(Arma.join_vert(_genColVec, _genMat), load("Arma.join_vert"));
  }

  @Test
  public void testArmaKron() throws IOException {
    assertMatEquals(Arma.kron(_genColVec, _genMat), load("Arma.kron"));
  }
  
  @Test
  public void testColPlus() throws IOException {
    assumeThat(_genMat.n_rows, is(_genColVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genColVec.n_cols));
    
    assertMatEquals(_genColVec.plus(_genMat), load("Col.plus"));
  }

  @Test
  public void testColMinus() throws IOException {
    assumeThat(_genMat.n_rows, is(_genColVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genColVec.n_cols));
    
    assertMatEquals(_genColVec.minus(_genMat), load("Col.minus"));
  }
  
  @Test
  public void testColTimes() throws IOException {
    assumeThat(_genMat.n_rows, is(_genColVec.n_cols));
    assumeThat(_genMat.is_finite(), is(true));
    assumeThat(_genColVec.is_finite(), is(true));
    
    assertMatEquals(_genColVec.times(_genMat), load("Col.times"));
  }

  @Test
  public void testColElemTimes() throws IOException {
    assumeThat(_genMat.n_rows, is(_genColVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genColVec.n_cols));
    
    assertMatEquals(_genColVec.elemTimes(_genMat), load("Col.elemTimes"));
  }

  @Test
  public void testColElemDivide() throws IOException {
    assumeThat(_genMat.n_rows, is(_genColVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genColVec.n_cols));
    
    assertMatEquals(_genColVec.elemDivide(_genMat), load("Col.elemDivide"));
  }

  @Test
  public void testColEquals() throws IOException {
    assumeThat(_genMat.n_rows, is(_genColVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genColVec.n_cols));
    
    assertMatEquals(_genColVec.equals(_genMat), load("Col.equals"));
  }

  @Test
  public void testColNonEquals() throws IOException {
    assumeThat(_genMat.n_rows, is(_genColVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genColVec.n_cols));
    
    assertMatEquals(_genColVec.nonEquals(_genMat), load("Col.nonEquals"));
  }

  @Test
  public void testColGreaterThan() throws IOException {
    assumeThat(_genMat.n_rows, is(_genColVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genColVec.n_cols));
    
    assertMatEquals(_genColVec.greaterThan(_genMat), load("Col.greaterThan"));
  }

  @Test
  public void testColLessThan() throws IOException {
    assumeThat(_genMat.n_rows, is(_genColVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genColVec.n_cols));
    
    assertMatEquals(_genColVec.lessThan(_genMat), load("Col.lessThan"));
  }

  @Test
  public void testColStrictGreaterThan() throws IOException {
    assumeThat(_genMat.n_rows, is(_genColVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genColVec.n_cols));
    
    assertMatEquals(_genColVec.strictGreaterThan(_genMat), load("Col.strictGreaterThan"));
  }

  @Test
  public void testColStrictLessThan() throws IOException {
    assumeThat(_genMat.n_rows, is(_genColVec.n_rows));
    assumeThat(_genMat.n_cols, is(_genColVec.n_cols));
    
    assertMatEquals(_genColVec.strictLessThan(_genMat), load("Col.strictLessThan"));
  }

}
