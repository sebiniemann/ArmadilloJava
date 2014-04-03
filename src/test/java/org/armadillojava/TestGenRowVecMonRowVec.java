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
public class TestGenRowVecMonRowVec extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, MonRowVec = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.MonRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Parameter(2)
  public String _monRowVecString;

  @Parameter(3)
  public Row    _monRowVecMat;

  protected Row _copyOfMonRowVec;

  protected int _copyOfDim;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _monRowVecString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfMonRowVec = new Row(_monRowVecMat);
  }

  @After
  public void after() {
    assertMatEquals(_genRowVec, _copyOfGenRowVec, 0);
    assertMatEquals(_monRowVecMat, _copyOfMonRowVec, 0);
  }

  @Test
  public void testHist() throws IOException {
    assertMatEquals(Arma.hist(_genRowVec, _monRowVecMat), load("Arma.hist"));
  }

  @Test
  public void testHistc() throws IOException {
    assertMatEquals(Arma.histc(_genRowVec, _monRowVecMat), load("Arma.histc"));
  }

}
