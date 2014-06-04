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
public class TestInPlaceGenColVecElemIndRangeGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, ElemIndRange = {2}, GenRowVec = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.ElemIndRange);
    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String  _genColVecString;

  @Parameter(1)
  public Col     _genColVec;

  protected Col  _copyOfGenColVec;

  @Parameter(2)
  public String  _elemIndRangeString;

  @Parameter(3)
  public Span    _elemIndRange;

  protected Span _copyOfElemIndRange;

  @Parameter(4)
  public String  _genRowVecString;

  @Parameter(5)
  public Row     _genRowVec;

  protected Row  _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _elemIndRangeString + "," + _genRowVecString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfElemIndRange = new Span(_elemIndRange);
    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    _genColVec.inPlace(Op.EQUAL, _copyOfGenColVec);
    _elemIndRange = new Span(_copyOfElemIndRange);
    _genRowVec = new Row(_copyOfGenRowVec);
  }

  @Test
  public void testColVecRowsEqual() throws IOException {
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVec.rows(_elemIndRange._first, _elemIndRange._last, Op.EQUAL, _genRowVec);

    assertMatEquals(_genColVec, load("Col.rowsEqual"));
  }

  @Test
  public void testColVecRowsPlus() throws IOException {
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVec.rows(_elemIndRange._first, _elemIndRange._last, Op.PLUS, _genRowVec);

    assertMatEquals(_genColVec, load("Col.rowsPlus"));
  }

  @Test
  public void testColVecRowsMinus() throws IOException {
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVec.rows(_elemIndRange._first, _elemIndRange._last, Op.MINUS, _genRowVec);

    assertMatEquals(_genColVec, load("Col.rowsMinus"));
  }

  @Test
  public void testColVecRowsTimes() throws IOException {
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVec.rows(_elemIndRange._first, _elemIndRange._last, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genColVec, load("Col.rowsElemTimes"));
  }

  @Test
  public void testColRowsDivide() throws IOException {
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVec.rows(_elemIndRange._first, _elemIndRange._last, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genColVec, load("Col.rowsElemDivide"));
  }

  @Test
  public void testColVecSubvecEqualA() throws IOException {
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVec.subvec(_elemIndRange, Op.EQUAL, _genRowVec);

    assertMatEquals(_genColVec, load("Col.subvecEqual"));
  }

  @Test
  public void testColVecSubvecEqualB() throws IOException {
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVec.subvec(_elemIndRange._first, _elemIndRange._last, Op.EQUAL, _genRowVec);

    assertMatEquals(_genColVec, load("Col.subvecEqual"));
  }


  @Test
  public void testColVecSubvecPlusA() throws IOException {
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVec.subvec(_elemIndRange, Op.PLUS, _genRowVec);

    assertMatEquals(_genColVec, load("Col.subvecPlus"));
  }


  @Test
  public void testColVecSubvecPlusB() throws IOException {
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVec.subvec(_elemIndRange._first, _elemIndRange._last, Op.PLUS, _genRowVec);

    assertMatEquals(_genColVec, load("Col.subvecPlus"));
  }

  @Test
  public void testColVecSubvecMinusA() throws IOException {
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVec.subvec(_elemIndRange, Op.MINUS, _genRowVec);

    assertMatEquals(_genColVec, load("Col.subvecMinus"));
  }

  @Test
  public void testColVecSubvecMinusB() throws IOException {
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVec.subvec(_elemIndRange._first, _elemIndRange._last, Op.MINUS, _genRowVec);

    assertMatEquals(_genColVec, load("Col.subvecMinus"));
  }

  @Test
  public void testColVecSubvecTimesA() throws IOException {
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVec.subvec(_elemIndRange, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genColVec, load("Col.subvecElemTimes"));
  }

  @Test
  public void testColVecSubvecTimesB() throws IOException {
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVec.subvec(_elemIndRange._first, _elemIndRange._last, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genColVec, load("Col.subvecElemTimes"));
  }

  @Test
  public void testColSubvecDivideA() throws IOException {
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVec.subvec(_elemIndRange, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genColVec, load("Col.subvecElemDivide"));
  }

  @Test
  public void testColSubvecDivideB() throws IOException {
    assumeThat(_genColVec.in_range(_elemIndRange), is(true));
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genRowVec.n_rows, is(_elemIndRange._last - _elemIndRange._first + 1));

    _genColVec.subvec(_elemIndRange._first, _elemIndRange._last, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genColVec, load("Col.subvecElemDivide"));
  }

}