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
public class TestGenMatNormal extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, Normal = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.Normal);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _normalString;

  @Parameter(3)
  public int    _normal;

  protected int _copyOfNormal;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _normalString;

    _copyOfGenMat = _genMat;
    _copyOfNormal = _normal;
  }

  @After
  public void after() {
    assertMatEquals(_genMat, _copyOfGenMat, 0);
    assertThat(_normal, is(_copyOfNormal));
  }

  @Test
  public void testStddev() throws IOException {
    assertMatEquals(Arma.stddev(Row.class, _genMat, _normal), load("stddev"));
    assertMatEquals(Arma.stddev(Col.class, _genMat, _normal), load("stddev").t());
  }

  @Test
  public void testVar() throws IOException {
    assertMatEquals(Arma.var(Row.class, _genMat, _normal), load("var"));
    assertMatEquals(Arma.var(Col.class, _genMat, _normal), load("var").t());
  }

  @Test
  public void testCor() throws IOException {
    assertMatEquals(Arma.cor(_genMat, _normal), load("cor"));
  }

  @Test
  public void testCov() throws IOException {
    assertMatEquals(Arma.cov(_genMat, _normal), load("cov"));
  }

}
