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
public class TestInPlaceGenMatElemInd extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, ElemInd = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.ElemInd);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _elemIndString;

  @Parameter(3)
  public int    _elemInd;

  protected int _copyOfElemInd;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _elemIndString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfElemInd = new Integer(_elemInd);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _elemInd = new Integer(_copyOfElemInd);
  }

  @Test
  public void testMatAtIncrement() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genMat.n_elem)));

    _genMat.at(_elemInd, Op.INCREMENT);

    assertMatEquals(_genMat, load("Mat.atIncrement"));
  }

  @Test
  public void testMatAtDecrement() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genMat.n_elem)));

    _genMat.at(_elemInd, Op.DECREMENT);

    assertMatEquals(_genMat, load("Mat.atDecrement"));
  }

}
