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
public class TestInPlaceGenRowVecElemIndGenColVec extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, ElemInd = {2}, GenColVec = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.ElemInd);
    inputClasses.add(InputClass.GenColVec);

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
  public String    _genColVecString;

  @Parameter(5)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _elemIndString + "," + _genColVecString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfElemInd = new Integer(_elemInd);
    _copyOfGenColVec = new Col(_genColVec);
  }

  @After
  public void after() {
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
    _elemInd = new Integer(_copyOfElemInd);
    _genColVec = new Col(_copyOfGenColVec);
  }

  @Test
  public void testRowVecColEqual() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));
    assumeThat(_genColVec.n_rows, is(_genRowVec.n_rows));

    _genRowVec.col(_elemInd, Op.EQUAL, _genColVec);

    assertMatEquals(_genRowVec, load("Row.colEqual"));
  }
  
  @Test
  public void testRowVecColPlus() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));
    assumeThat(_genColVec.n_rows, is(_genRowVec.n_rows));

    _genRowVec.col(_elemInd, Op.PLUS, _genColVec);

    assertMatEquals(_genRowVec, load("Row.colPlus"));
  }

  @Test
  public void testRowVecColMinus() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));
    assumeThat(_genColVec.n_rows, is(_genRowVec.n_rows));

    _genRowVec.col(_elemInd, Op.MINUS, _genColVec);

    assertMatEquals(_genRowVec, load("Row.colMinus"));
  }
  
  @Test
  public void testRowVecInsertCols() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));
    assumeThat(_genColVec.n_rows, is(_genRowVec.n_rows));

    _genRowVec.insert_cols(_elemInd, _genColVec);

    assertMatEquals(_genRowVec, load("Row.insertCols"));
  }

}

