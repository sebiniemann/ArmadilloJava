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
public class TestInPlaceGenColVecElemIndsGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, ElemInds = {2}, GenRowVec = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.ElemInds);
    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genColVecString;

  @Parameter(1)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;

  @Parameter(2)
  public String _elemIndsString;

  @Parameter(3)
  public Col    _elemInds;

  protected Col _copyOfElemInds;

  @Parameter(4)
  public String _genRowVecString;

  @Parameter(5)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _elemIndsString + "," + _genRowVecString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfElemInds = new Col(_elemInds);
    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    _genColVec.inPlace(Op.EQUAL, _copyOfGenColVec);
    _elemInds.inPlace(Op.EQUAL, _copyOfElemInds);
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
  }

  @Test
  public void testColElemEqual() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVec.n_elem, is(_elemInds.n_elem));

    _genColVec.elem(_elemInds, Op.EQUAL, _genRowVec);

    assertMatEquals(_genColVec, load("Col.elemEqual"));
  }

  @Test
  public void testColElemPlus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVec.n_elem, is(_elemInds.n_elem));

    _genColVec.elem(_elemInds, Op.PLUS, _genRowVec);

    assertMatEquals(_genColVec, load("Col.elemPlus"));
  }

  @Test
  public void testColElemMinus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVec.n_elem, is(_elemInds.n_elem));

    _genColVec.elem(_elemInds, Op.MINUS, _genRowVec);

    assertMatEquals(_genColVec, load("Col.elemMinus"));
  }

  @Test
  public void testColElemElemTimes() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVec.n_elem, is(_elemInds.n_elem));

    _genColVec.elem(_elemInds, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genColVec, load("Col.elemElemTimes"));
  }

  @Test
  public void testColElemElemDivide() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVec.n_elem, is(_elemInds.n_elem));

    _genColVec.elem(_elemInds, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genColVec, load("Col.elemElemDivide"));
  }

  @Test
  public void testColRowsEqual() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVec.n_elem, is(_elemInds.n_elem));

    _genColVec.rows(_elemInds, Op.EQUAL, _genRowVec);

    assertMatEquals(_genColVec, load("Col.rowsEqual"));
  }

  @Test
  public void testColRowsPlus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVec.n_elem, is(_elemInds.n_elem));

    _genColVec.rows(_elemInds, Op.PLUS, _genRowVec);

    assertMatEquals(_genColVec, load("Col.rowsPlus"));
  }

  @Test
  public void testColRowsMinus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVec.n_elem, is(_elemInds.n_elem));

    _genColVec.rows(_elemInds, Op.MINUS, _genRowVec);

    assertMatEquals(_genColVec, load("Col.rowsMinus"));
  }

  @Test
  public void testColRowsElemTimes() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVec.n_elem, is(_elemInds.n_elem));

    _genColVec.rows(_elemInds, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genColVec, load("Col.rowsElemTimes"));
  }

  @Test
  public void testColRowsElemDivide() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genRowVec.n_elem, is(_elemInds.n_elem));

    _genColVec.rows(_elemInds, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genColVec, load("Col.rowsElemDivide"));
  }

}
