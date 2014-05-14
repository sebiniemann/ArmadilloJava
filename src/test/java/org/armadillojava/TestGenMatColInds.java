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
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assume.assumeThat;

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
public class TestGenMatColInds extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, ColInds = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.ColInds);

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
  public Col    _colInds;
  protected Col _copyOfColInds;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _monColVecString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfColInds = new Col(_colInds);
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertMatEquals(_colInds, _copyOfColInds, 0);
  }

  @Test
  public void testMatCols() throws IOException {
    for (int j = 0; j < _colInds.n_elem; j++) {
      assumeThat((int) _colInds.at(j), is(lessThan(_genMat.n_cols)));
    }
    
    assertMatEquals(_genMat.cols(_colInds), load("Mat.cols"));
  }

}
