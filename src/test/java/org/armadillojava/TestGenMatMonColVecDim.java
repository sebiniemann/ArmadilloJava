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
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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
public class TestGenMatMonColVecDim extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, MonColVec = {2}, Dim = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.MonColVec);
    inputClasses.add(InputClass.Dim);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _monColVecString;

  @Parameter(3)
  public Col    _monColVecMat;

  protected Col _copyOfMonColVec;

  @Parameter(4)
  public String _dimString;

  @Parameter(5)
  public int    _dim;

  protected int _copyOfDim;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _monColVecString + "," + _dimString;
    
    _copyOfGenMat = new Mat(_genMat);
    _copyOfMonColVec = new Col(_monColVecMat);
    _copyOfDim = _dim;
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertMatEquals(_monColVecMat, _copyOfMonColVec, 0);
    assertThat(_dim, is(_copyOfDim));
  }

  @Test
  public void testHist() throws IOException {
    assertMatEquals(Arma.hist(_genMat, _monColVecMat, _dim), load("hist"));
  }

  @Test
  public void testHistc() throws IOException {
    assertMatEquals(Arma.histc(_genMat, _monColVecMat, _dim), load("histc"));
  }

}
