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
public class TestInPlaceGenColVecElemIndGenColVec extends TestClass {

  @Parameters(name = "{index}: GenColVecA = {0}, ElemInd = {2}, GenColVecB = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.ElemInd);
    inputClasses.add(InputClass.GenColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genColVecAString;

  @Parameter(1)
  public Col       _genColVecA;

  protected Col    _copyOfGenColVecA;

  @Parameter(2)
  public String    _elemIndString;

  @Parameter(3)
  public int       _elemInd;

  protected int    _copyOfElemInd;

  @Parameter(4)
  public String    _genColVecBString;

  @Parameter(5)
  public Col    _genColVecB;

  protected Col _copyOfGenColVecB;

  @Before
  public void before() {
    _fileSuffix = _genColVecAString + "," + _elemIndString + "," + _genColVecBString;

    _copyOfGenColVecA = new Col(_genColVecA);
    _copyOfElemInd = new Integer(_elemInd);
    _copyOfGenColVecB = new Col(_genColVecB);
  }

  @After
  public void after() {
    _genColVecA.inPlace(Op.EQUAL, _copyOfGenColVecA);
    _elemInd = new Integer(_copyOfElemInd);
    _genColVecB = new Col(_copyOfGenColVecB);
  }

  @Test
  public void testColVecRowEqual() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genColVecA.n_elem)));
    assumeThat(_genColVecB.is_rowvec(), is(true));
    assumeThat(_genColVecB.n_cols, is(_genColVecA.n_cols));

    _genColVecA.row(_elemInd, Op.EQUAL, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.rowEqual"));
  }
  
  @Test
  public void testColVecRowPlus() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genColVecA.n_elem)));
    assumeThat(_genColVecB.is_rowvec(), is(true));
    assumeThat(_genColVecB.n_cols, is(_genColVecA.n_cols));

    _genColVecA.row(_elemInd, Op.PLUS, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.rowPlus"));
  }

  @Test
  public void testColVecRowMinus() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genColVecA.n_elem)));
    assumeThat(_genColVecB.is_rowvec(), is(true));
    assumeThat(_genColVecB.n_cols, is(_genColVecA.n_cols));

    _genColVecA.row(_elemInd, Op.MINUS, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.rowMinus"));
  }
  
  @Test
  public void testColVecInsertRows() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genColVecA.n_elem)));
    assumeThat(_genColVecB.is_rowvec(), is(true));
    assumeThat(_genColVecB.n_cols, is(_genColVecA.n_cols));

    _genColVecA.insert_rows(_elemInd, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.insert_rows"));
  }

}
