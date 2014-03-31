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
public class TestLogicRowVec extends TestClass {

  @Parameters(name = "{index}: LogicRowVec = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.LogicRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _logicRowVecString;

  @Parameter(1)
  public Row    _logicRowVec;

  protected Row _copyOfLogicRowVec;

  @Before
  public void before() {
    _fileSuffix = _logicRowVecString;

    _copyOfLogicRowVec = new Row(_logicRowVec);
  }

  @After
  public void after() {
    assertMatEquals(_logicRowVec, _copyOfLogicRowVec, 0);
  }

  @Test
  public void testAll() throws IOException {
    int expected = (int) load("all")._data[0];
    if (Arma.all(_logicRowVec)) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }

  @Test
  public void testAny() throws IOException {
    int expected = (int) load("any")._data[0];
    if (Arma.any(_logicRowVec)) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }

  @Test
  public void testFind() throws IOException {
    assertMatEquals(Arma.find(_logicRowVec), load("find"));
  }

}
