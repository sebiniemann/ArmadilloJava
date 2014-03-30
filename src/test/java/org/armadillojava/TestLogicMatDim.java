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
public class TestLogicMatDim extends TestClass {

  @Parameters(name = "{index}: LogicMat = {0}, Dim = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.LogicMat);
    inputClasses.add(InputClass.Dim);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _logicMatString;

  @Parameter(1)
  public Mat    _logicMat;

  protected Mat _copyOfLogicMat;

  @Parameter(2)
  public String _dimString;

  @Parameter(3)
  public int    _dim;

  protected int _copyOfDim;

  @Before
  public void before() {
    _fileSuffix = _logicMatString + "," + _dimString;

    _copyOfLogicMat = new Mat(_logicMat);
    _copyOfDim = new Integer(_dim);
  }

  @After
  public void after() {
    assertMatEquals(_logicMat, _copyOfLogicMat, 0);
    assertThat(_dim, is(_copyOfDim));
  }

  @Test
  public void testAll() throws IOException {
    if (_dim == 0) {
      assertMatEquals(Arma.all(Row.class, _logicMat, _dim), load("all"));
      assertMatEquals(Arma.all(Col.class, _logicMat, _dim), load("all").t());
    } else {
      assertMatEquals(Arma.all(Row.class, _logicMat, _dim), load("all").t());
      assertMatEquals(Arma.all(Col.class, _logicMat, _dim), load("all"));
    }
  }

  @Test
  public void testAny() throws IOException {
    if (_dim == 0) {
      assertMatEquals(Arma.any(Row.class, _logicMat, _dim), load("any"));
      assertMatEquals(Arma.any(Col.class, _logicMat, _dim), load("any").t());
    } else {
      assertMatEquals(Arma.any(Row.class, _logicMat, _dim), load("any").t());
      assertMatEquals(Arma.any(Col.class, _logicMat, _dim), load("any"));
    }
  }

}
