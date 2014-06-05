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
public class TestGenColVecGenDouble extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, GenDouble = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.GenDouble);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genColVecString;

  @Parameter(1)
  public Col       _genColVec;

  protected Col    _copyOfGenColVec;

  @Parameter(2)
  public String    _genDoubleString;

  @Parameter(3)
  public double    _genDouble;

  protected double _copyOfGenDouble;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _genDoubleString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfGenDouble = new Double(_genDouble);
  }

  @After
  public void after() {
    assertMatEquals(_genColVec, _copyOfGenColVec, 0);
    assertThat(_genDouble, is(_copyOfGenDouble));
  }

  @Test
  public void testColPlus() throws IOException {
    assertMatEquals(_genColVec.plus(_genDouble), load("Col.plus"));
  }

  @Test
  public void testColMinus() throws IOException {
    assertMatEquals(_genColVec.minus(_genDouble), load("Col.minus"));
  }

  @Test
  public void testColTimes() throws IOException {
    assertMatEquals(_genColVec.times(_genDouble), load("Col.times"));
  }

  @Test
  public void testColDivide() throws IOException {
    assertMatEquals(_genColVec.divide(_genDouble), load("Col.divide"));
  }

  @Test
  public void testColEquals() throws IOException {
    assertMatEquals(_genColVec.equals(_genDouble), load("Col.equals"));
  }

  @Test
  public void testColNonEquals() throws IOException {
    assertMatEquals(_genColVec.nonEquals(_genDouble), load("Col.nonEquals"));
  }

  @Test
  public void testColGreaterThan() throws IOException {
    assertMatEquals(_genColVec.greaterThan(_genDouble), load("Col.greaterThan"));
  }

  @Test
  public void testColLessThan() throws IOException {
    assertMatEquals(_genColVec.lessThan(_genDouble), load("Col.lessThan"));
  }

  @Test
  public void testColStrictGreaterThan() throws IOException {
    assertMatEquals(_genColVec.strictGreaterThan(_genDouble), load("Col.strictGreaterThan"));
  }

  @Test
  public void testColStrictLessThan() throws IOException {
    assertMatEquals(_genColVec.strictLessThan(_genDouble), load("Col.strictLessThan"));
  }

}
