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
import static org.junit.Assert.assertEquals;
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
public class TestInPlaceGenRowVecElemIndNumElems extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, ElemInd = {2}, NumElems = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.ElemInd);
    inputClasses.add(InputClass.NumElems);

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
  public String    _numElemsString;

  @Parameter(5)
  public int	_numElems;

  protected int _copyOfNumElems;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _elemIndString + "," + _numElemsString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfElemInd = new Integer(_elemInd);
    _copyOfNumElems = _numElems;
  }

  @After
  public void after() {
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
    _elemInd = new Integer(_copyOfElemInd);
    _numElems = _copyOfNumElems;
  }

  @Test
  public void testRowVecInsertCols() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));

    _genRowVec.insert_cols(_elemInd, _numElems);

    assertMatEquals(_genRowVec, load("Row.insert_cols"));
  }
  
  @Test
  public void testRowVecInsertColsTrue() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));

    _genRowVec.insert_cols(_elemInd, _numElems, true);

    assertMatEquals(_genRowVec, load("Row.insert_colsTrue"));
  }
  
  @Test
  public void testRowVecInsertColsFalse() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));

    _genRowVec.insert_cols(_elemInd, _numElems, false);

    assertEquals(_genRowVec.n_elem, load("Row.insert_colsFalse").n_elem);
  }
  
  @Test
  public void testRowVecShedCols() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));
    assumeThat(_numElems, is(lessThan(_elemInd)));
    assumeThat(_numElems, is(lessThan(_genRowVec.n_elem)));

    _genRowVec.shed_cols(_elemInd, _numElems);

    assertMatEquals(_genRowVec, load("Row.shed_cols"));
  }

}