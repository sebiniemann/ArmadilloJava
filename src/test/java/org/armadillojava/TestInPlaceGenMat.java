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
public class TestInPlaceGenMat extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Before
  public void before() {
    _fileSuffix = _genMatString;

    _copyOfGenMat = new Mat(_genMat);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
  }

  @Test
  public void testArmaInplace_trans() throws IOException {
    Arma.inplace_trans(_genMat);

    assertMatEquals(_genMat, load("Arma.inplace_trans"));
  }

  @Test
  public void testMatOnes() throws IOException {
    _genMat.ones();

    assertMatEquals(_genMat, load("Mat.ones"));
  }

  @Test
  public void testMatZeros() throws IOException {
    _genMat.zeros();

    assertMatEquals(_genMat, load("Mat.zeros"));
  }

  @Test
  public void testMatInPlaceIncrement() throws IOException {
    _genMat.inPlace(Op.INCREMENT);

    assertMatEquals(_genMat, load("Mat.inPlaceIncrement"));
  }

  @Test
  public void testMatInPlaceDecrement() throws IOException {
    _genMat.inPlace(Op.DECREMENT);

    assertMatEquals(_genMat, load("Mat.inPlaceDecrement"));
  }

  @Test
  public void testMatReset() throws IOException {
    _genMat.reset();

    assertThat(_genMat.n_elem, is(0));
  }

  @Test
  public void testMatClear() throws IOException {
    _genMat.clear();

    assertThat(_genMat.n_elem, is(0));
  }

  @Test
  public void testMatEye() throws IOException {
    _genMat.eye();

    assertMatEquals(_genMat, load("Mat.eye"));
  }

}
