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
public class TestGenMatMonRowVec extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, MonRowVec = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.MonRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _monRowVecString;

  @Parameter(3)
  public Row    _monRowVecMat;

  protected Row _copyOfMonRowVec;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _monRowVecString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfMonRowVec = new Row(_monRowVecMat);
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertMatEquals(_monRowVecMat, _copyOfMonRowVec, 0);
  }

  @Test
  public void testHist() throws IOException {
    assertMatEquals(Arma.hist(_genMat, _monRowVecMat), load("hist"));
  }

  @Test
  public void testHistc() throws IOException {
    assertMatEquals(Arma.histc(_genMat, _monRowVecMat), load("histc"));
  }

}
