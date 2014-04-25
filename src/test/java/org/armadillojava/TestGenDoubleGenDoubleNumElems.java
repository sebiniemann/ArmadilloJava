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
import static org.armadillojava.TestUtil.assertMatEquals;
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
public class TestGenDoubleGenDoubleNumElems extends TestClass {

  @Parameters(name = "{index}: GenDoubleA = {0}, GenDoubleB = {2}, NumElems = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenDouble);
    inputClasses.add(InputClass.GenDouble);
    inputClasses.add(InputClass.NumElems);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genDoubleAString;

  @Parameter(1)
  public double    _genDoubleA;

  protected double _copyOfGenDoubleA;

  @Parameter(2)
  public String    _genDoubleBString;

  @Parameter(3)
  public double    _genDoubleB;

  protected double _copyOfGenDoubleB;

  @Parameter(4)
  public String    _numElemsString;

  @Parameter(5)
  public int       _numElems;

  protected int    _copyOfNumElems;

  @Before
  public void before() {
    _fileSuffix = _genDoubleAString + "," + _genDoubleBString + "," + _numElemsString;

    _copyOfGenDoubleA = new Double(_genDoubleA);
    _copyOfGenDoubleB = new Double(_genDoubleB);
    _copyOfNumElems = new Integer(_numElems);
  }

  @After
  public void after() {
    assertThat(_genDoubleA, is(_copyOfGenDoubleA));
    assertThat(_genDoubleB, is(_copyOfGenDoubleB));
    assertThat(_numElems, is(_copyOfNumElems));
  }

  @Test
  public void testArmaLinspace() throws IOException {
    assertMatEquals(Arma.linspace(Col.class, _genDoubleA, _genDoubleB, _numElems), load("Arma.linspace"));
    assertMatEquals(Arma.linspace(Row.class, _genDoubleA, _genDoubleB, _numElems), load("Arma.linspace").t());
  }

}
