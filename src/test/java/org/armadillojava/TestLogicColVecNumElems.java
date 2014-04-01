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
public class TestLogicColVecNumElems extends TestClass {

  @Parameters(name = "{index}: LogicColVec = {0}, NumElems = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.LogicColVec);
    inputClasses.add(InputClass.NumElems);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _logicColVecString;

  @Parameter(1)
  public Col    _logicColVec;

  protected Col _copyOfLogicColVec;

  @Parameter(2)
  public String _numElemsString;

  @Parameter(3)
  public int    _numElems;

  protected int _copyOfNumElems;

  @Before
  public void before() {
    _fileSuffix = _logicColVecString + "," + _numElemsString;

    _copyOfLogicColVec = new Col(_logicColVec);
    _copyOfNumElems = new Integer(_numElems);
  }

  @After
  public void after() {
    assertMatEquals(_logicColVec, _copyOfLogicColVec, 0);
    assertThat(_numElems, is(_copyOfNumElems));
  }

  @Test
  public void testFind() throws IOException {
    assertMatEquals(Arma.find(_logicColVec, _numElems), load("find"));
  }

}
