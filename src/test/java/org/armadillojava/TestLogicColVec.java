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
public class TestLogicColVec extends TestClass {

  @Parameters(name = "{index}: LogicColVec = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.LogicColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _logicColVecString;

  @Parameter(1)
  public Col    _logicColVec;

  protected Col _copyOfLogicColVec;

  @Before
  public void before() {
    _fileSuffix = _logicColVecString;

    _copyOfLogicColVec = new Col(_logicColVec);
  }

  @After
  public void after() {
    assertMatEquals(_logicColVec, _copyOfLogicColVec, 0);
  }

  @Test
  public void testAll() throws IOException {
    int expected = (int) load("Arma.all")._data[0];
    if (Arma.all(_logicColVec)) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }

  @Test
  public void testAny() throws IOException {
    int expected = (int) load("Arma.any")._data[0];
    if (Arma.any(_logicColVec)) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }

  @Test
  public void testFind() throws IOException {
    assertMatEquals(Arma.find(_logicColVec), load("Arma.find"));
  }

}
