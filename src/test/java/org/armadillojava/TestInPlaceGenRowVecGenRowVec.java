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
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

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
public class TestInPlaceGenRowVecGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenRowVecA = {0}, GenRowVecB = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecAString;

  @Parameter(1)
  public Row    _genRowVecA;

  @Parameter(2)
  public String _genRowVecBString;

  @Parameter(3)
  public Row    _genRowVecB;

  protected Row _copyOfGenRowVecB;
  protected Row _copyOfGenRowVecA;

  @Before
  public void before() {
    _fileSuffix = _genRowVecAString + "," + _genRowVecBString;

    _copyOfGenRowVecB = new Row(_genRowVecB);
    _copyOfGenRowVecA = new Row(_genRowVecA);
  }

  @After
  public void after() {
    _genRowVecA.inPlace(Op.EQUAL, _copyOfGenRowVecA);
    _genRowVecB.inPlace(Op.EQUAL, _copyOfGenRowVecB);
  }

  @Test
  public void testRowInPlaceEqual() throws IOException {
	assumeThat(_genRowVecB.is_rowvec(), is(true));
	assumeThat(_genRowVecB.n_elem, is(_genRowVecA.n_elem));
	_genRowVecA.inPlace(Op.EQUAL, _genRowVecB);

    assertMatEquals(_genRowVecA, load("Row.inPlaceEqual"));
  }

  @Test
  public void testRowInPlacePlus() throws IOException {
    assumeThat(_genRowVecA.n_cols, is(_genRowVecB.n_cols));
    assumeThat(_genRowVecA.n_rows, is(_genRowVecB.n_rows));

    _genRowVecA.inPlace(Op.PLUS, _genRowVecB);

    assertMatEquals(_genRowVecA, load("Row.inPlacePlus"));
  }

  @Test
  public void testRowInPlaceMinus() throws IOException {
    assumeThat(_genRowVecA.n_cols, is(_genRowVecB.n_cols));
    assumeThat(_genRowVecA.n_rows, is(_genRowVecB.n_rows));

    _genRowVecA.inPlace(Op.MINUS, _genRowVecB);

    assertMatEquals(_genRowVecA, load("Row.inPlaceMinus"));
  }

  @Test
  public void testRowInPlaceTimes() throws IOException {
    assumeThat(_genRowVecA.is_finite(), is(true));
    assumeThat(_genRowVecB.is_finite(), is(true));
    assumeThat(_genRowVecA.n_cols, is(_genRowVecB.n_rows));
    assumeThat(_genRowVecA.n_cols, is(_genRowVecB.n_cols));

    _genRowVecA.inPlace(Op.TIMES, _genRowVecB);

    assertMatEquals(_genRowVecA, load("Row.inPlaceTimes"));
  }

  @Test
  public void testRowInPlaceElemTimes() throws IOException {
    assumeThat(_genRowVecA.n_cols, is(_genRowVecB.n_cols));
    assumeThat(_genRowVecA.n_rows, is(_genRowVecB.n_rows));

    _genRowVecA.inPlace(Op.ELEMTIMES, _genRowVecB);

    assertMatEquals(_genRowVecA, load("Row.inPlaceElemTimes"));
  }

  @Test
  public void testRowInPlaceElemDivide() throws IOException {
    assumeThat(_genRowVecA.n_cols, is(_genRowVecB.n_cols));
    assumeThat(_genRowVecA.n_rows, is(_genRowVecB.n_rows));

    _genRowVecA.inPlace(Op.ELEMDIVIDE, _genRowVecB);

    assertMatEquals(_genRowVecA, load("Row.inPlaceElemDivide"));
  }

  @Test
  public void testRowSwap() throws IOException {
    assumeThat(_genRowVecB.is_rowvec(), is(true));
    
    _genRowVecA.swap(_genRowVecB);

    assertMatEquals(_genRowVecA, load("Row.swap"));
  }

  @Test
  public void testRowCopy_size() throws IOException {
	assumeThat(_genRowVecB.is_rowvec(), is(true));
	_genRowVecA.copy_size(_genRowVecB);

    Mat expected = load("Row.copy_size");

    assertThat(_genRowVecA.n_cols, is(expected.n_cols));
    assertThat(_genRowVecA.n_rows, is(expected.n_rows));
  }
  
  @Test
  public void testRowRow() throws IOException {
    assumeThat(_genRowVecB.is_rowvec(), is(true));
    
    _genRowVecA = new Row(_genRowVecB);

    assertMatEquals(_genRowVecA, load("Row.row"));
  }

}
