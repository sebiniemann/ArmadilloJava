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
public class TestNumElems extends TestClass {

  @Parameters(name = "{index}: NumElems = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.NumElems);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _numElemsString;

  @Parameter(1)
  public int    _numElems;

  protected int _copyOfNumElems;

  @Before
  public void before() {
    _fileSuffix = _numElemsString;

    _copyOfNumElems = _numElems;
  }

  @After
  public void after() {
    assertThat(_numElems, is(_copyOfNumElems));
  }

  @Test
  public void testOnes() throws IOException {
    assertMatEquals(Arma.ones(Col.class, _numElems), load("ones"));
    assertMatEquals(Arma.ones(Row.class, _numElems), load("ones").t());
  }

  @Test
  public void testZeros() throws IOException {
    assertMatEquals(Arma.zeros(Col.class, _numElems), load("zeros"));
    assertMatEquals(Arma.zeros(Row.class, _numElems), load("zeros").t());
  }

}
