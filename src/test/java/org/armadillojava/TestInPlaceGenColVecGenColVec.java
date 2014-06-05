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
public class TestInPlaceGenColVecGenColVec extends TestClass {

  @Parameters(name = "{index}: GenColVecA = {0}, GenColVecB = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.GenColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genColVecAString;

  @Parameter(1)
  public Col    _genColVecA;

  @Parameter(2)
  public String _genColVecBString;

  @Parameter(3)
  public Col    _genColVecB;

  protected Col _copyOfGenColVecB;
  protected Col _copyOfGenColVecA;

  @Before
  public void before() {
    _fileSuffix = _genColVecAString + "," + _genColVecBString;

    _copyOfGenColVecB = new Col(_genColVecB);
    _copyOfGenColVecA = new Col(_genColVecA);
  }

  @After
  public void after() {
    _genColVecA.inPlace(Op.EQUAL, _copyOfGenColVecA);
    _genColVecB.inPlace(Op.EQUAL, _copyOfGenColVecB);
  }

  @Test
  public void testColInPlaceEqual() throws IOException {
	assumeThat(_genColVecB.is_colvec(), is(true));
	assumeThat(_genColVecB.n_elem, is(_genColVecA.n_elem));
	_genColVecA.inPlace(Op.EQUAL, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.inPlaceEqual"));
  }

  @Test
  public void testColInPlacePlus() throws IOException {
    assumeThat(_genColVecA.n_rows, is(_genColVecB.n_rows));
    assumeThat(_genColVecA.n_cols, is(_genColVecB.n_cols));

    _genColVecA.inPlace(Op.PLUS, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.inPlacePlus"));
  }

  @Test
  public void testColInPlaceMinus() throws IOException {
    assumeThat(_genColVecA.n_rows, is(_genColVecB.n_rows));
    assumeThat(_genColVecA.n_cols, is(_genColVecB.n_cols));

    _genColVecA.inPlace(Op.MINUS, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.inPlaceMinus"));
  }

  @Test
  public void testColInPlaceTimes() throws IOException {
    assumeThat(_genColVecA.is_finite(), is(true));
    assumeThat(_genColVecB.is_finite(), is(true));
    assumeThat(_genColVecA.n_cols, is(_genColVecB.n_rows));
    assumeThat(_genColVecA.n_cols, is(_genColVecB.n_cols));

    _genColVecA.inPlace(Op.TIMES, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.inPlaceTimes"));
  }

  @Test
  public void testColInPlaceElemTimes() throws IOException {
    assumeThat(_genColVecA.n_rows, is(_genColVecB.n_rows));
    assumeThat(_genColVecA.n_cols, is(_genColVecB.n_cols));

    _genColVecA.inPlace(Op.ELEMTIMES, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.inPlaceElemTimes"));
  }

  @Test
  public void testColInPlaceElemDivide() throws IOException {
    assumeThat(_genColVecA.n_rows, is(_genColVecB.n_rows));
    assumeThat(_genColVecA.n_cols, is(_genColVecB.n_cols));

    _genColVecA.inPlace(Op.ELEMDIVIDE, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.inPlaceElemDivide"));
  }

  @Test
  public void testColSwap() throws IOException {
    assumeThat(_genColVecB.is_colvec(), is(true));
    
    _genColVecA.swap(_genColVecB);

    assertMatEquals(_genColVecA, load("Col.swap"));
  }

  @Test
  public void testColCopy_size() throws IOException {
	assumeThat(_genColVecB.is_colvec(), is(true));
	_genColVecA.copy_size(_genColVecB);

    Mat expected = load("Col.copySize");

    assertThat(_genColVecA.n_rows, is(expected.n_rows));
    assertThat(_genColVecA.n_cols, is(expected.n_cols));
  }
  
  @Test
  public void testColCol() throws IOException {
    assumeThat(_genColVecB.is_colvec(), is(true));
    
    _genColVecA = new Col(_genColVecB);

    assertMatEquals(_genColVecA, load("Col.col"));
  }

}
