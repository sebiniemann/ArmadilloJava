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
public class TestGenMatGenDouble extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, GenDouble = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.GenDouble);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genMatString;

  @Parameter(1)
  public Mat       _genMat;

  protected Mat    _copyOfGenMat;

  @Parameter(2)
  public String    _genDoubleString;

  @Parameter(3)
  public double    _genDouble;

  protected double _copyOfGenDouble;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _genDoubleString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfGenDouble = new Double(_genDouble);
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertThat(_genDouble, is(_copyOfGenDouble));
  }

  @Test
  public void testMatPlus() throws IOException {
    assertMatEquals(_genMat.plus(_genDouble), load("Mat.plus"));
  }

  @Test
  public void testMatMinus() throws IOException {
    assertMatEquals(_genMat.minus(_genDouble), load("Mat.minus"));
  }

  @Test
  public void testMatTimes() throws IOException {
    assertMatEquals(_genMat.times(_genDouble), load("Mat.times"));
  }

  @Test
  public void testMatDivide() throws IOException {
    assertMatEquals(_genMat.divide(_genDouble), load("Mat.divide"));
  }

  @Test
  public void testMatEquals() throws IOException {
    assertMatEquals(_genMat.equals(_genDouble), load("Mat.equals"));
  }

  @Test
  public void testMatNonEquals() throws IOException {
    assertMatEquals(_genMat.nonEquals(_genDouble), load("Mat.nonEquals"));
  }

  @Test
  public void testMatGreaterThan() throws IOException {
    assertMatEquals(_genMat.greaterThan(_genDouble), load("Mat.greaterThan"));
  }

  @Test
  public void testMatLessThan() throws IOException {
    assertMatEquals(_genMat.lessThan(_genDouble), load("Mat.lessThan"));
  }

  @Test
  public void testMatStrictGreaterThan() throws IOException {
    assertMatEquals(_genMat.strictGreaterThan(_genDouble), load("Mat.strictGreaterThan"));
  }

  @Test
  public void testMatStrictLessThan() throws IOException {
    assertMatEquals(_genMat.strictLessThan(_genDouble), load("Mat.strictLessThan"));
  }

}
