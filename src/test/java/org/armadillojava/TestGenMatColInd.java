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
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.lessThan;

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
public class TestGenMatColInd extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, ColInd = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.ColInd);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _colIndString;

  @Parameter(3)
  public int    _colInd;

  protected int _copyOfColInd;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _colIndString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfColInd = new Integer(_colInd);
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertThat(_colInd, is(_copyOfColInd));
  }

  @Test
  public void testArmaDiagvec() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));

    assertMatEquals(Arma.diagvec(_genMat, _colInd), load("Arma.diagvecSuper"));
  }

  @Test
  public void testMatCol() throws IOException {
    assumeThat(_colInd, is(lessThan(_genMat.n_cols)));

    assertMatEquals(_genMat.col(_colInd), load("Mat.col"));
  }

}
