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
public class TestInPlaceGenMatExtColIndGenMat extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, ExtColInd = {2}, GenMat = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.ExtColInd);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatAString;

  @Parameter(1)
  public Mat    _genMatA;

  protected Mat _copyOfGenMatA;

  @Parameter(2)
  public String _extColIndString;

  @Parameter(3)
  public int    _extColInd;

  protected int _copyOfExtColInd;

  @Parameter(4)
  public String _genMatBString;

  @Parameter(5)
  public Mat    _genMatB;

  protected Mat _copyOfGenMatB;

  @Before
  public void before() {
    _fileSuffix = _genMatAString + "," + _extColIndString + "," + _genMatBString;

    _copyOfGenMatA = new Mat(_genMatA);
    _copyOfExtColInd = new Integer(_extColInd);
    _copyOfGenMatB = new Mat(_genMatB);
  }

  @After
  public void after() {
    _genMatA.inPlace(Op.EQUAL, _copyOfGenMatA);
    _extColInd = new Integer(_copyOfExtColInd);
    _genMatB.inPlace(Op.EQUAL, _copyOfGenMatB);
  }

  @Test
  public void testMatInsert_cols() throws IOException {
    assumeThat(_extColInd, is(lessThanOrEqualTo(_genMatA.n_cols)));
    assumeThat(_genMatB.n_rows, is(_genMatA.n_rows));

    _genMatA.insert_cols(_extColInd, _genMatB);

    assertMatEquals(_genMatA, load("Mat.insert_cols"));
  }

}
