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
public class TestGenMatSinValSel extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, SinValSel = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.SinValSel);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genMatString;

  @Parameter(1)
  public Mat       _genMat;

  protected Mat    _copyOfGenMat;

  @Parameter(2)
  public String    _sinValSelString;

  @Parameter(3)
  public String    _sinValSel;

  protected String _copyOfSinValSel;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _sinValSelString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfSinValSel = new String(_sinValSel);
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertThat(_sinValSel, is(_copyOfSinValSel));
  }

  @Test
  public void testArmaSvd_econ() throws IOException {
    Mat U = new Mat();
    Col s = new Col();
    Mat V = new Mat();

    Arma.svd_econ(U, s, V, _genMat, _sinValSel);

    assertMatEquals(s, load("Arma.svd_econ"));

    if (_genMat.is_square() && _sinValSel.equals("both")) {
      assertMatEquals(U.times(Arma.diagmat(s)).times(V.t()), _genMat);
    }
  }

}
