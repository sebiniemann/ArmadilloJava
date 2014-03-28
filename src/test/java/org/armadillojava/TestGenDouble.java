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
public class TestGenDouble extends TestClass {

  @Parameters(name = "{index}: GenDouble = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenDouble);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genDoubleString;

  @Parameter(1)
  public double    _genDouble;

  protected double _copyOfGenDouble;

  @Before
  public void before() {
    _fileSuffix = _genDoubleString;
    _copyOfGenDouble = _genDouble;
  }

  @After
  public void after() {
    assertThat(_genDouble, is(_copyOfGenDouble));
  }

  @Test
  public void testIs_finite() throws IOException {
    int expected = (int) load("is_finite")._data[0];
    if (Arma.is_finite(_genDouble)) {
      assertThat(1, is(expected));
    } else {
      assertThat(0, is(expected));
    }
  }

}
