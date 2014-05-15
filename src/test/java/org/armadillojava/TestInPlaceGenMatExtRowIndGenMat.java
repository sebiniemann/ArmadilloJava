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
import static org.hamcrest.Matchers.lessThanOrEqualTo;

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
public class TestInPlaceGenMatExtRowIndGenMat extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, ExtRowInd = {2}, GenMat = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.ExtRowInd);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatAString;

  @Parameter(1)
  public Mat    _genMatA;

  protected Mat _copyOfGenMatA;

  @Parameter(2)
  public String _extRowIndString;

  @Parameter(3)
  public int    _extRowInd;

  protected int _copyOfExtRowInd;

  @Parameter(4)
  public String _genMatBString;

  @Parameter(5)
  public Mat    _genMatB;

  protected Mat _copyOfGenMatB;

  @Before
  public void before() {
    _fileSuffix = _genMatAString + "," + _extRowIndString + "," + _genMatBString;

    _copyOfGenMatA = new Mat(_genMatA);
    _copyOfExtRowInd = new Integer(_extRowInd);
    _copyOfGenMatB = new Mat(_genMatB);
  }

  @After
  public void after() {
    _genMatA.inPlace(Op.EQUAL, _copyOfGenMatA);
    _extRowInd = new Integer(_copyOfExtRowInd);
    _genMatB.inPlace(Op.EQUAL, _copyOfGenMatB);
  }

  @Test
  public void testMatInsert_rows() throws IOException {
    assumeThat(_extRowInd, is(lessThanOrEqualTo(_genMatA.n_rows)));
    assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));

    _genMatA.insert_rows(_extRowInd, _genMatB);

    assertMatEquals(_genMatA, load("Mat.insert_rows"));
  }

}
