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
public class TestGenColVecGenColVecNormal extends TestClass {

  @Parameters(name = "{index}: GenColVecA = {0}, GenColVecB = {2}, Normal = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.Normal);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genColVecAString;

  @Parameter(1)
  public Col       _genColVecA;

  protected Col    _copyOfGenColVecA;

  @Parameter(2)
  public String    _genColVecBString;

  @Parameter(3)
  public Col       _genColVecB;

  protected Col    _copyOfGenColVecB;

  @Parameter(4)
  public String    _normalString;

  @Parameter(5)
  public int    _normal;

  protected int _copyOfNormal;

  @Before
  public void before() {
    _fileSuffix = _genColVecAString + "," + _genColVecBString + "," + _normalString;

    _copyOfGenColVecA = new Col(_genColVecA);
    _copyOfGenColVecB = new Col(_genColVecB);
    _copyOfNormal = new Integer(_normal);
  }

  @After
  public void after() {
    assertMatEquals(_genColVecA, _copyOfGenColVecA, 0);
    assertMatEquals(_genColVecB, _copyOfGenColVecB, 0);
    assertThat(_normal, is(_copyOfNormal));
  }
  
  @Test
  public void testCor() throws IOException {
    assumeThat(_genColVecA.n_elem, is(_genColVecB.n_elem));
    
    double expected = load("cor")._data[0];
    double actual = Arma.cor(_genColVecA, _genColVecB, _normal);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

  @Test
  public void testCov() throws IOException {
    assumeThat(_genColVecA.n_elem, is(_genColVecB.n_elem));
    
    double expected = load("cov")._data[0];
    double actual = Arma.cov(_genColVecA, _genColVecB, _normal);

    if (Double.isInfinite(expected) || Double.isNaN(expected)) {
      assertThat(actual, is(expected));
    } else {
      assertThat(actual, is(closeTo(expected, Math.abs(expected) * 1e-12)));
    }
  }

}
