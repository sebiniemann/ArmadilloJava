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
import static org.junit.Assume.assumeThat;
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
public class TestGenRowVecGenRowVecNormal extends TestClass {

  @Parameters(name = "{index}: GenRowVecA = {0}, GenRowVecB = {2}, Normal = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.Normal);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genRowVecAString;

  @Parameter(1)
  public Row       _genRowVecA;

  protected Row    _copyOfGenRowVecA;

  @Parameter(2)
  public String    _genRowVecBString;

  @Parameter(3)
  public Row       _genRowVecB;

  protected Row    _copyOfGenRowVecB;

  @Parameter(4)
  public String    _normalString;

  @Parameter(5)
  public int    _normal;

  protected int _copyOfNormal;

  @Before
  public void before() {
    _fileSuffix = _genRowVecAString + "," + _genRowVecBString + "," + _normalString;

    _copyOfGenRowVecA = new Row(_genRowVecA);
    _copyOfGenRowVecB = new Row(_genRowVecB);
    _copyOfNormal = new Integer(_normal);
  }

  @After
  public void after() {
    assertMatEquals(_genRowVecA, _copyOfGenRowVecA, 0);
    assertMatEquals(_genRowVecB, _copyOfGenRowVecB, 0);
    assertThat(_normal, is(_copyOfNormal));
  }
  
  @Test
  public void testCor() throws IOException {
    assumeThat(_genRowVecA.n_elem, is(_genRowVecB.n_elem));
    
    double expected = load("cor")._data[0];
    double actual = Arma.cor(_genRowVecA, _genRowVecB, _normal);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testCov() throws IOException {
    assumeThat(_genRowVecA.n_elem, is(_genRowVecB.n_elem));
    
    double expected = load("cov")._data[0];
    double actual = Arma.cov(_genRowVecA, _genRowVecB, _normal);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

}
