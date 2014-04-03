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
import static org.hamcrest.number.IsCloseTo.closeTo;

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
public class TestOORowVec extends TestClass {

  @Parameters(name = "{index}: OOMat = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.OORowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _ooRowVecString;

  @Parameter(1)
  public Row    _ooRowVec;

  protected Row _copyOfOORowVec;

  @Before
  public void before() {
    _fileSuffix = _ooRowVecString;

    _copyOfOORowVec = new Row(_ooRowVec);
  }

  @After
  public void after() {
    assertMatEquals(_ooRowVec, _copyOfOORowVec, 0);
  }

  @Test
  public void testAs_scalar() throws IOException {
    double expected = load("Arma.as_scalar")._data[0];
    if (Double.isInfinite(expected)) {
      assertThat(Arma.as_scalar(_ooRowVec), is(expected));
    } else {
      assertThat(Arma.as_scalar(_ooRowVec), is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }
}
