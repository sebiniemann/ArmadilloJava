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
public class TestGenRowVecGenDouble extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, GenDouble = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.GenDouble);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Parameter(2)
  public String _genDoubleString;

  @Parameter(3)
  public double    _genDouble;

  protected double _copyOfGenDouble;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _genDoubleString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfGenDouble = new Double(_genDouble);
  }

  @After
  public void after() {
    assertMatEquals(_genRowVec, _copyOfGenRowVec, 0);
    assertThat(_genDouble, is(_copyOfGenDouble));
  }

  @Test
  public void testMatPlus() throws IOException {
    assertMatEquals(_genRowVec.plus(_genDouble), load("Row.plus"));
  }

  @Test
  public void testMatMinus() throws IOException {
    assertMatEquals(_genRowVec.minus(_genDouble), load("Row.minus"));
  }
  
  @Test
  public void testMatElemTimes() throws IOException {
    assertMatEquals(_genRowVec.times(_genDouble), load("Row.times"));
  }

  @Test
  public void testMatElemDivide() throws IOException {
    assertMatEquals(_genRowVec.divide(_genDouble), load("Row.divide"));
  }

  @Test
  public void testMatEquals() throws IOException {
    assertMatEquals(_genRowVec.equals(_genDouble), load("Row.equals"));
  }

  @Test
  public void testMatNonEquals() throws IOException {
    assertMatEquals(_genRowVec.nonEquals(_genDouble), load("Row.nonEquals"));
  }

  @Test
  public void testMatGreaterThan() throws IOException {
    assertMatEquals(_genRowVec.greaterThan(_genDouble), load("Row.greaterThan"));
  }

  @Test
  public void testMatLessThan() throws IOException {
    assertMatEquals(_genRowVec.lessThan(_genDouble), load("Row.lessThan"));
  }

  @Test
  public void testMatStrictGreaterThan() throws IOException {
    assertMatEquals(_genRowVec.strictGreaterThan(_genDouble), load("Row.strictGreaterThan"));
  }

  @Test
  public void testMatStrictLessThan() throws IOException {
    assertMatEquals(_genRowVec.strictLessThan(_genDouble), load("Row.strictLessThan"));
  }

}
