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
public class TestInPlaceGenColVecGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, GenRowVec = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genColVecString;

  @Parameter(1)
  public Col    _genColVec;

  @Parameter(2)
  public String _genRowVecString;

  @Parameter(3)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;
  protected Col _copyOfGenColVec;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _genRowVecString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfGenColVec = new Col(_genColVec);
  }

  @After
  public void after() {
    _genColVec.inPlace(Op.EQUAL, _copyOfGenColVec);
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
  }

  @Test
  public void testColInPlaceEqual() throws IOException {
	assumeThat(_genRowVec.is_colvec(), is(true));
	assumeThat(_genRowVec.n_elem, is(_genColVec.n_elem));
	_genColVec.inPlace(Op.EQUAL, _genRowVec);

    assertMatEquals(_genColVec, load("Col.inPlaceEqual"));
  }

  @Test
  public void testColInPlacePlus() throws IOException {
    assumeThat(_genColVec.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genColVec.n_cols, is(_genRowVec.n_cols));

    _genColVec.inPlace(Op.PLUS, _genRowVec);

    assertMatEquals(_genColVec, load("Col.inPlacePlus"));
  }

  @Test
  public void testColInPlaceMinus() throws IOException {
    assumeThat(_genColVec.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genColVec.n_cols, is(_genRowVec.n_cols));

    _genColVec.inPlace(Op.MINUS, _genRowVec);

    assertMatEquals(_genColVec, load("Col.inPlaceMinus"));
  }

  @Test
  public void testColInPlaceTimes() throws IOException {
    assumeThat(_genColVec.is_finite(), is(true));
    assumeThat(_genRowVec.is_finite(), is(true));
    assumeThat(_genColVec.n_cols, is(_genRowVec.n_rows));
    assumeThat(_genColVec.n_cols, is(_genRowVec.n_cols));

    _genColVec.inPlace(Op.TIMES, _genRowVec);

    assertMatEquals(_genColVec, load("Col.inPlaceTimes"));
  }

  @Test
  public void testColInPlaceElemTimes() throws IOException {
    assumeThat(_genColVec.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genColVec.n_cols, is(_genRowVec.n_cols));

    _genColVec.inPlace(Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genColVec, load("Col.inPlaceElemTimes"));
  }

  @Test
  public void testColInPlaceElemDivide() throws IOException {
    assumeThat(_genColVec.n_rows, is(_genRowVec.n_rows));
    assumeThat(_genColVec.n_cols, is(_genRowVec.n_cols));

    _genColVec.inPlace(Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genColVec, load("Col.inPlaceElemDivide"));
  }

  @Test
  public void testColSwap() throws IOException {
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genColVec.is_rowvec(), is(true));
    
    _genColVec.swap(_genRowVec);

    assertMatEquals(_genColVec, load("Col.swap"));
  }

  @Test
  public void testColCopy_size() throws IOException {
	assumeThat(_genRowVec.is_colvec(), is(true));
	_genColVec.copy_size(_genRowVec);

    Mat expected = load("Col.copySize");

    assertThat(_genColVec.n_rows, is(expected.n_rows));
    assertThat(_genColVec.n_cols, is(expected.n_cols));
  }
  
  @Test
  public void testColCol() throws IOException {
    assumeThat(_genRowVec.is_colvec(), is(true));
    
    _genColVec = new Col(_genRowVec);

    assertMatEquals(_genColVec, load("Col.col"));
  }

}
