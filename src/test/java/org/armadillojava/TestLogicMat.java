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
public class TestLogicMat extends TestClass {

  @Parameters(name = "{index}: LogicMat = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.LogicMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _logicMatString;

  @Parameter(1)
  public Mat    _logicMat;

  protected Mat _copyOfLogicMat;

  @Before
  public void before() {
    _fileSuffix = _logicMatString;

    _copyOfLogicMat = new Mat(_logicMat);
  }

  @After
  public void after() {
    assertMatEquals(_logicMat, _copyOfLogicMat, 0);
  }

  @Test
  public void testAll() throws IOException {
    assertMatEquals(Arma.all(Row.class, _logicMat), load("all"));
    assertMatEquals(Arma.all(Col.class, _logicMat), load("all").t());
  }

  @Test
  public void testAny() throws IOException {
    assertMatEquals(Arma.any(Row.class, _logicMat), load("any"));
    assertMatEquals(Arma.any(Col.class, _logicMat), load("any").t());
  }

}
