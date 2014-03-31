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
public class TestGenRowVecMonColVec extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, MonColVec = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.MonColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Parameter(2)
  public String _monColVecString;

  @Parameter(3)
  public Col    _monColVecMat;

  protected Col _copyOfMonColVec;

  protected int _copyOfDim;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _monColVecString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfMonColVec = new Col(_monColVecMat);
  }

  @After
  public void after() {
    assertMatEquals(_genRowVec, _copyOfGenRowVec, 0);
    assertMatEquals(_monColVecMat, _copyOfMonColVec, 0);
  }

  @Test
  public void testHist() throws IOException {
    assertMatEquals(Arma.hist(_genRowVec, _monColVecMat), load("hist"));
  }

  @Test
  public void testHistc() throws IOException {
    assertMatEquals(Arma.histc(_genRowVec, _monColVecMat), load("histc"));
  }

}
