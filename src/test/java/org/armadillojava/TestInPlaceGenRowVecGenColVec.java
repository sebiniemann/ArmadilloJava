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
public class TestInPlaceGenRowVecGenColVec extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, GenColVec = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.GenColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  @Parameter(2)
  public String _genColVecString;

  @Parameter(3)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;
  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _genColVecString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
    _genColVec.inPlace(Op.EQUAL, _copyOfGenColVec);
  }

  @Test
  public void testRowInPlaceEqual() throws IOException {
	assumeThat(_genColVec.is_rowvec(), is(true));
	assumeThat(_genColVec.n_elem, is(_genRowVec.n_elem));
	_genRowVec.inPlace(Op.EQUAL, _genColVec);

    assertMatEquals(_genRowVec, load("row.inPlaceEqual"));
  }

  @Test
  public void testRowInPlacePlus() throws IOException {
    assumeThat(_genRowVec.n_cols, is(_genColVec.n_cols));
    assumeThat(_genRowVec.n_rows, is(_genColVec.n_rows));

    _genRowVec.inPlace(Op.PLUS, _genColVec);

    assertMatEquals(_genRowVec, load("row.inPlacePlus"));
  }

  @Test
  public void testRowInPlaceMinus() throws IOException {
    assumeThat(_genRowVec.n_cols, is(_genColVec.n_cols));
    assumeThat(_genRowVec.n_rows, is(_genColVec.n_rows));

    _genRowVec.inPlace(Op.MINUS, _genColVec);

    assertMatEquals(_genRowVec, load("row.inPlaceMinus"));
  }

  @Test
  public void testRowInPlaceTimes() throws IOException {
    assumeThat(_genRowVec.is_finite(), is(true));
    assumeThat(_genColVec.is_finite(), is(true));
    assumeThat(_genRowVec.n_cols, is(_genColVec.n_rows));
    assumeThat(_genRowVec.n_cols, is(_genColVec.n_cols));

    _genRowVec.inPlace(Op.TIMES, _genColVec);

    assertMatEquals(_genRowVec, load("row.inPlaceTimes"));
  }

  @Test
  public void testRowInPlaceElemTimes() throws IOException {
    assumeThat(_genRowVec.n_cols, is(_genColVec.n_cols));
    assumeThat(_genRowVec.n_rows, is(_genColVec.n_rows));

    _genRowVec.inPlace(Op.ELEMTIMES, _genColVec);

    assertMatEquals(_genRowVec, load("row.inPlaceElemTimes"));
  }

  @Test
  public void testRowInPlaceElemDivide() throws IOException {
    assumeThat(_genRowVec.n_cols, is(_genColVec.n_cols));
    assumeThat(_genRowVec.n_rows, is(_genColVec.n_rows));

    _genRowVec.inPlace(Op.ELEMDIVIDE, _genColVec);

    assertMatEquals(_genRowVec, load("row.inPlaceElemDivide"));
  }

  @Test
  public void testRowSwap() throws IOException {
    assumeThat(_genColVec.is_rowvec(), is(true));
    assumeThat(_genRowVec.is_colvec(), is(true));
    
    _genRowVec.swap(_genColVec);

    assertMatEquals(_genRowVec, load("row.swap"));
  }

  @Test
  public void testRowCopy_size() throws IOException {
	assumeThat(_genColVec.is_rowvec(), is(true));
	_genRowVec.copy_size(_genColVec);

    Mat expected = load("row.copySize");

    assertThat(_genRowVec.n_cols, is(expected.n_cols));
    assertThat(_genRowVec.n_rows, is(expected.n_rows));
  }
  
  @Test
  public void testRowRow() throws IOException {
    assumeThat(_genColVec.is_rowvec(), is(true));
    
    _genRowVec = new Row(_genColVec);

    assertMatEquals(_genRowVec, load("row.row"));
  }

}
