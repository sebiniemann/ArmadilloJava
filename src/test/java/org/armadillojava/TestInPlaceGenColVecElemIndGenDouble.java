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
import static org.junit.Assume.assumeThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.lessThan;

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
public class TestInPlaceGenColVecElemIndGenDouble extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, ElemInd = {2}, GenDouble = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.ElemInd);
    inputClasses.add(InputClass.GenDouble);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genColVecString;

  @Parameter(1)
  public Col       _genColVec;

  protected Col    _copyOfGenColVec;

  @Parameter(2)
  public String    _elemIndString;

  @Parameter(3)
  public int       _elemInd;

  protected int    _copyOfElemInd;

  @Parameter(4)
  public String    _genDoubleString;

  @Parameter(5)
  public double    _genDouble;

  protected double _copyOfGenDouble;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _elemIndString + "," + _genDoubleString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfElemInd = new Integer(_elemInd);
    _copyOfGenDouble = new Double(_genDouble);
  }

  @After
  public void after() {
    _genColVec.inPlace(Op.EQUAL, _copyOfGenColVec);
    _elemInd = new Integer(_copyOfElemInd);
    _genDouble = new Double(_copyOfGenDouble);
  }

  @Test
  public void testColVecAtEqual() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genColVec.n_elem)));

    _genColVec.at(_elemInd, Op.EQUAL, _genDouble);

    assertMatEquals(_genColVec, load("Col.atEqual"));
  }
  
  @Test
  public void testColVecAtPlus() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genColVec.n_elem)));

    _genColVec.at(_elemInd, Op.PLUS, _genDouble);

    assertMatEquals(_genColVec, load("Col.atPlus"));
  }

  @Test
  public void testColVecAtMinus() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genColVec.n_elem)));

    _genColVec.at(_elemInd, Op.MINUS, _genDouble);

    assertMatEquals(_genColVec, load("Col.atMinus"));
  }

  @Test
  public void testColVecAtTimes() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genColVec.n_elem)));

    _genColVec.at(_elemInd, Op.TIMES, _genDouble);

    assertMatEquals(_genColVec, load("Col.atTimes"));
  }

  @Test
  public void testColAtDivide() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genColVec.n_elem)));

    _genColVec.at(_elemInd, Op.DIVIDE, _genDouble);

    assertMatEquals(_genColVec, load("Col.atDivide"));
  }

}