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
public class TestInPlaceGenRowVecElemIndGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenRowVecA = {0}, ElemInd = {2}, GenRowVecB = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.ElemInd);
    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genRowVecAString;

  @Parameter(1)
  public Row       _genRowVecA;

  protected Row    _copyOfGenRowVecA;

  @Parameter(2)
  public String    _elemIndString;

  @Parameter(3)
  public int       _elemInd;

  protected int    _copyOfElemInd;

  @Parameter(4)
  public String    _genRowVecBString;

  @Parameter(5)
  public Row    _genRowVecB;

  protected Row _copyOfGenRowVecB;

  @Before
  public void before() {
    _fileSuffix = _genRowVecAString + "," + _elemIndString + "," + _genRowVecBString;

    _copyOfGenRowVecA = new Row(_genRowVecA);
    _copyOfElemInd = new Integer(_elemInd);
    _copyOfGenRowVecB = new Row(_genRowVecB);
  }

  @After
  public void after() {
    _genRowVecA.inPlace(Op.EQUAL, _copyOfGenRowVecA);
    _elemInd = new Integer(_copyOfElemInd);
    _genRowVecB = new Row(_copyOfGenRowVecB);
  }

  @Test
  public void testRowVecColEqual() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVecA.n_elem)));
    assumeThat(_genRowVecB.is_colvec(), is(true));
    assumeThat(_genRowVecB.n_rows, is(_genRowVecA.n_rows));

    _genRowVecA.col(_elemInd, Op.EQUAL, _genRowVecB);

    assertMatEquals(_genRowVecA, load("Row.ColEqual"));
  }
  
  @Test
  public void testRowVecColPlus() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVecA.n_elem)));
    assumeThat(_genRowVecB.is_colvec(), is(true));
    assumeThat(_genRowVecB.n_rows, is(_genRowVecA.n_rows));

    _genRowVecA.col(_elemInd, Op.PLUS, _genRowVecB);

    assertMatEquals(_genRowVecA, load("Row.ColPlus"));
  }

  @Test
  public void testRowVecColMinus() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVecA.n_elem)));
    assumeThat(_genRowVecB.is_colvec(), is(true));
    assumeThat(_genRowVecB.n_rows, is(_genRowVecA.n_rows));

    _genRowVecA.col(_elemInd, Op.MINUS, _genRowVecB);

    assertMatEquals(_genRowVecA, load("Row.ColMinus"));
  }
  
  @Test
  public void testRowVecInsertCols() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVecA.n_elem)));
    assumeThat(_genRowVecB.is_colvec(), is(true));
    assumeThat(_genRowVecB.n_rows, is(_genRowVecA.n_rows));

    _genRowVecA.insert_cols(_elemInd, _genRowVecB);

    assertMatEquals(_genRowVecA, load("Row.insert_cols"));
  }

}
