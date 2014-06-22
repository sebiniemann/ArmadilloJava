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
public class TestInPlaceGenRowVecElemIndsGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenRowVecA = {0}, ElemInds = {2}, GenRowVecB = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.ElemInds);
    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecAString;

  @Parameter(1)
  public Row    _genRowVecA;

  protected Row _copyOfGenRowVecA;

  @Parameter(2)
  public String _elemIndsString;

  @Parameter(3)
  public Col    _elemInds;

  protected Col _copyOfElemInds;

  @Parameter(4)
  public String _genRowVecBString;

  @Parameter(5)
  public Row    _genRowVecB;

  protected Row _copyOfGenRowVecB;

  @Before
  public void before() {
    _fileSuffix = _genRowVecAString + "," + _elemIndsString + "," + _genRowVecBString;

    _copyOfGenRowVecA = new Row(_genRowVecA);
    _copyOfElemInds = new Col(_elemInds);
    _copyOfGenRowVecB = new Row(_genRowVecB);
  }

  @After
  public void after() {
    _genRowVecA.inPlace(Op.EQUAL, _copyOfGenRowVecA);
    _elemInds.inPlace(Op.EQUAL, _copyOfElemInds);
    _genRowVecB.inPlace(Op.EQUAL, _copyOfGenRowVecB);
  }

  @Test
  public void testRowElemEqual() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVecB.n_elem, is(_elemInds.n_elem));

    _genRowVecA.elem(_elemInds, Op.EQUAL, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.elemEqual"));
  }

  @Test
  public void testRowElemPlus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVecB.n_elem, is(_elemInds.n_elem));

    _genRowVecA.elem(_elemInds, Op.PLUS, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.elemPlus"));
  }

  @Test
  public void testRowElemMinus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVecB.n_elem, is(_elemInds.n_elem));

    _genRowVecA.elem(_elemInds, Op.MINUS, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.elemMinus"));
  }

  @Test
  public void testRowElemElemTimes() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVecB.n_elem, is(_elemInds.n_elem));

    _genRowVecA.elem(_elemInds, Op.ELEMTIMES, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.elemElemTimes"));
  }

  @Test
  public void testRowElemElemDivide() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVecB.n_elem, is(_elemInds.n_elem));

    _genRowVecA.elem(_elemInds, Op.ELEMDIVIDE, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.elemElemDivide"));
  }

  @Test
  public void testRowColsEqual() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVecB.n_elem, is(_elemInds.n_elem));

    _genRowVecA.cols(_elemInds, Op.EQUAL, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.colsEqual"));
  }

  @Test
  public void testRowColsPlus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVecB.n_elem, is(_elemInds.n_elem));

    _genRowVecA.cols(_elemInds, Op.PLUS, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.colsPlus"));
  }

  @Test
  public void testRowColsMinus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVecB.n_elem, is(_elemInds.n_elem));

    _genRowVecA.cols(_elemInds, Op.MINUS, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.colsMinus"));
  }

  @Test
  public void testRowColsElemTimes() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVecB.n_elem, is(_elemInds.n_elem));

    _genRowVecA.cols(_elemInds, Op.ELEMTIMES, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.colsElemTimes"));
  }

  @Test
  public void testRowColsElemDivide() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVecB.n_elem, is(_elemInds.n_elem));

    _genRowVecA.cols(_elemInds, Op.ELEMDIVIDE, _genRowVecB);

    assertMatEquals(_genRowVecA, load("row.colsElemDivide"));
  }

}
