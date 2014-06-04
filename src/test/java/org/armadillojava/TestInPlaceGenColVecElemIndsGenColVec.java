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
public class TestInPlaceGenColVecElemIndsGenColVec extends TestClass {

  @Parameters(name = "{index}: GenColVecA = {0}, ElemInds = {2}, GenColVecB = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.ElemInds);
    inputClasses.add(InputClass.GenColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genColVecAString;

  @Parameter(1)
  public Col    _genColVecA;

  protected Col _copyOfGenColVecA;

  @Parameter(2)
  public String _elemIndsString;

  @Parameter(3)
  public Col    _elemInds;

  protected Col _copyOfElemInds;

  @Parameter(4)
  public String _genColVecBString;

  @Parameter(5)
  public Col    _genColVecB;

  protected Col _copyOfGenColVecB;

  @Before
  public void before() {
    _fileSuffix = _genColVecAString + "," + _elemIndsString + "," + _genColVecBString;

    _copyOfGenColVecA = new Col(_genColVecA);
    _copyOfElemInds = new Col(_elemInds);
    _copyOfGenColVecB = new Col(_genColVecB);
  }

  @After
  public void after() {
    _genColVecA.inPlace(Op.EQUAL, _copyOfGenColVecA);
    _elemInds.inPlace(Op.EQUAL, _copyOfElemInds);
    _genColVecB.inPlace(Op.EQUAL, _copyOfGenColVecB);
  }

  @Test
  public void testColElemEqual() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVecB.n_elem, is(_elemInds.n_elem));

    _genColVecA.elem(_elemInds, Op.EQUAL, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.elemEqual"));
  }

  @Test
  public void testColElemPlus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVecB.n_elem, is(_elemInds.n_elem));

    _genColVecA.elem(_elemInds, Op.PLUS, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.elemPlus"));
  }

  @Test
  public void testColElemMinus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVecB.n_elem, is(_elemInds.n_elem));

    _genColVecA.elem(_elemInds, Op.MINUS, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.elemMinus"));
  }

  @Test
  public void testColElemElemTimes() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVecB.n_elem, is(_elemInds.n_elem));

    _genColVecA.elem(_elemInds, Op.ELEMTIMES, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.elemElemTimes"));
  }

  @Test
  public void testColElemElemDivide() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVecB.n_elem, is(_elemInds.n_elem));

    _genColVecA.elem(_elemInds, Op.ELEMDIVIDE, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.elemElemDivide"));
  }

  @Test
  public void testColRowsEqual() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVecB.n_elem, is(_elemInds.n_elem));

    _genColVecA.rows(_elemInds, Op.EQUAL, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.rowsEqual"));
  }

  @Test
  public void testColRowsPlus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVecB.n_elem, is(_elemInds.n_elem));

    _genColVecA.rows(_elemInds, Op.PLUS, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.rowsPlus"));
  }

  @Test
  public void testColRowsMinus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVecB.n_elem, is(_elemInds.n_elem));

    _genColVecA.rows(_elemInds, Op.MINUS, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.rowsMinus"));
  }

  @Test
  public void testColRowsElemTimes() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVecB.n_elem, is(_elemInds.n_elem));

    _genColVecA.rows(_elemInds, Op.ELEMTIMES, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.rowsElemTimes"));
  }

  @Test
  public void testColRowsElemDivide() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVecA.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genColVecB.n_elem, is(_elemInds.n_elem));

    _genColVecA.rows(_elemInds, Op.ELEMDIVIDE, _genColVecB);

    assertMatEquals(_genColVecA, load("Col.rowsElemDivide"));
  }

}
