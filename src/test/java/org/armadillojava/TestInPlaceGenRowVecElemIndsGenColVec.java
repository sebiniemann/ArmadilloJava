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
public class TestInPlaceGenRowVecElemIndsGenColVec extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, ElemInds = {2}, GenColVec = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.ElemInds);
    inputClasses.add(InputClass.GenColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Parameter(2)
  public String _elemIndsString;

  @Parameter(3)
  public Col    _elemInds;

  protected Col _copyOfElemInds;

  @Parameter(4)
  public String _genColVecString;

  @Parameter(5)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _elemIndsString + "," + _genColVecString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfElemInds = new Col(_elemInds);
    _copyOfGenColVec = new Col(_genColVec);
  }

  @After
  public void after() {
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
    _elemInds.inPlace(Op.EQUAL, _copyOfElemInds);
    _genColVec.inPlace(Op.EQUAL, _copyOfGenColVec);
  }

  @Test
  public void testRowElemEqual() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVec.n_elem, is(_elemInds.n_elem));

    _genRowVec.elem(_elemInds, Op.EQUAL, _genColVec);

    assertMatEquals(_genRowVec, load("Row.elemEqual"));
  }

  @Test
  public void testRowElemPlus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVec.n_elem, is(_elemInds.n_elem));

    _genRowVec.elem(_elemInds, Op.PLUS, _genColVec);

    assertMatEquals(_genRowVec, load("Row.elemPlus"));
  }

  @Test
  public void testRowElemMinus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVec.n_elem, is(_elemInds.n_elem));

    _genRowVec.elem(_elemInds, Op.MINUS, _genColVec);

    assertMatEquals(_genRowVec, load("Row.elemMinus"));
  }

  @Test
  public void testRowElemElemTimes() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVec.n_elem, is(_elemInds.n_elem));

    _genRowVec.elem(_elemInds, Op.ELEMTIMES, _genColVec);

    assertMatEquals(_genRowVec, load("Row.elemElemTimes"));
  }

  @Test
  public void testRowElemElemDivide() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVec.n_elem, is(_elemInds.n_elem));

    _genRowVec.elem(_elemInds, Op.ELEMDIVIDE, _genColVec);

    assertMatEquals(_genRowVec, load("Row.elemElemDivide"));
  }

  @Test
  public void testRowColsEqual() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVec.n_elem, is(_elemInds.n_elem));

    _genRowVec.cols(_elemInds, Op.EQUAL, _genColVec);

    assertMatEquals(_genRowVec, load("Row.colsEqual"));
  }

  @Test
  public void testRowColsPlus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVec.n_elem, is(_elemInds.n_elem));

    _genRowVec.cols(_elemInds, Op.PLUS, _genColVec);

    assertMatEquals(_genRowVec, load("Row.colsPlus"));
  }

  @Test
  public void testRowColsMinus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVec.n_elem, is(_elemInds.n_elem));

    _genRowVec.cols(_elemInds, Op.MINUS, _genColVec);

    assertMatEquals(_genRowVec, load("Row.colsMinus"));
  }

  @Test
  public void testRowColsElemTimes() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVec.n_elem, is(_elemInds.n_elem));

    _genRowVec.cols(_elemInds, Op.ELEMTIMES, _genColVec);

    assertMatEquals(_genRowVec, load("Row.colsElemTimes"));
  }

  @Test
  public void testRowColsElemDivide() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVec.n_elem, is(_elemInds.n_elem));

    _genRowVec.cols(_elemInds, Op.ELEMDIVIDE, _genColVec);

    assertMatEquals(_genRowVec, load("Row.colsElemDivide"));
  }

}
