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
public class TestInPlaceGenRowVecElemIndGenDouble extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, ElemInd = {2}, GenDouble = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.ElemInd);
    inputClasses.add(InputClass.GenDouble);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genRowVecString;

  @Parameter(1)
  public Row       _genRowVec;

  protected Row    _copyOfGenRowVec;

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
    _fileSuffix = _genRowVecString + "," + _elemIndString + "," + _genDoubleString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfElemInd = new Integer(_elemInd);
    _copyOfGenDouble = new Double(_genDouble);
  }

  @After
  public void after() {
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
    _elemInd = new Integer(_copyOfElemInd);
    _genDouble = new Double(_copyOfGenDouble);
  }

  @Test
  public void testRowVecAtEqual() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));

    _genRowVec.at(_elemInd, Op.EQUAL, _genDouble);

    assertMatEquals(_genRowVec, load("Row.atEqual"));
  }
  
  @Test
  public void testRowVecAtPlus() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));

    _genRowVec.at(_elemInd, Op.PLUS, _genDouble);

    assertMatEquals(_genRowVec, load("Row.atPlus"));
  }

  @Test
  public void testRowVecAtMinus() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));

    _genRowVec.at(_elemInd, Op.MINUS, _genDouble);

    assertMatEquals(_genRowVec, load("Row.atMinus"));
  }

  @Test
  public void testRowVecAtTimes() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));

    _genRowVec.at(_elemInd, Op.TIMES, _genDouble);

    assertMatEquals(_genRowVec, load("Row.atTimes"));
  }

  @Test
  public void testRowAtDivide() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));

    _genRowVec.at(_elemInd, Op.DIVIDE, _genDouble);

    assertMatEquals(_genRowVec, load("Row.atDivide"));
  }
  
  @Test
  public void testRowVecColPlus() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));

    _genRowVec.col(_elemInd, Op.PLUS, _genDouble);

    assertMatEquals(_genRowVec, load("Row.colPlus"));
  }

  @Test
  public void testRowVecColMinus() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));

    _genRowVec.col(_elemInd, Op.MINUS, _genDouble);

    assertMatEquals(_genRowVec, load("Row.colMinus"));
  }

  @Test
  public void testRowVecColTimes() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));

    _genRowVec.col(_elemInd, Op.TIMES, _genDouble);

    assertMatEquals(_genRowVec, load("Row.colTimes"));
  }

  @Test
  public void testRowColDivide() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));

    _genRowVec.col(_elemInd, Op.DIVIDE, _genDouble);

    assertMatEquals(_genRowVec, load("Row.colDivide"));
  }

}