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
public class TestInPlaceGenRowVecElemIndsGenMat extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, ElemInds = {2}, GenMat = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.ElemInds);
    inputClasses.add(InputClass.GenMat);

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
  public String _genMatString;

  @Parameter(5)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _elemIndsString + "," + _genMatString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfElemInds = new Col(_elemInds);
    _copyOfGenMat = new Mat(_genMat);
  }

  @After
  public void after() {
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
    _elemInds.inPlace(Op.EQUAL, _copyOfElemInds);
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
  }

  @Test
  public void testRowElemEqual() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genMat.is_rowvec(), is(true));
    assumeThat(_genMat.n_elem, is(_elemInds.n_elem));

    _genRowVec.elem(_elemInds, Op.EQUAL, _genMat);

    assertMatEquals(_genRowVec, load("row.elemEqual"));
  }

  @Test
  public void testRowElemPlus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genMat.is_rowvec(), is(true));
    assumeThat(_genMat.n_elem, is(_elemInds.n_elem));

    _genRowVec.elem(_elemInds, Op.PLUS, _genMat);

    assertMatEquals(_genRowVec, load("row.elemPlus"));
  }

  @Test
  public void testRowElemMinus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genMat.is_rowvec(), is(true));
    assumeThat(_genMat.n_elem, is(_elemInds.n_elem));

    _genRowVec.elem(_elemInds, Op.MINUS, _genMat);

    assertMatEquals(_genRowVec, load("row.elemMinus"));
  }

  @Test
  public void testRowElemElemTimes() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genMat.is_rowvec(), is(true));
    assumeThat(_genMat.n_elem, is(_elemInds.n_elem));

    _genRowVec.elem(_elemInds, Op.ELEMTIMES, _genMat);

    assertMatEquals(_genRowVec, load("row.elemElemTimes"));
  }

  @Test
  public void testRowElemElemDivide() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genMat.is_rowvec(), is(true));
    assumeThat(_genMat.n_elem, is(_elemInds.n_elem));

    _genRowVec.elem(_elemInds, Op.ELEMDIVIDE, _genMat);

    assertMatEquals(_genRowVec, load("row.elemElemDivide"));
  }

  @Test
  public void testRowColsEqual() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genMat.is_rowvec(), is(true));
    assumeThat(_genMat.n_elem, is(_elemInds.n_elem));

    _genRowVec.cols(_elemInds, Op.EQUAL, _genMat);

    assertMatEquals(_genRowVec, load("row.colsEqual"));
  }

  @Test
  public void testRowColsPlus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genMat.is_rowvec(), is(true));
    assumeThat(_genMat.n_elem, is(_elemInds.n_elem));

    _genRowVec.cols(_elemInds, Op.PLUS, _genMat);

    assertMatEquals(_genRowVec, load("row.colsPlus"));
  }

  @Test
  public void testRowColsMinus() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genMat.is_rowvec(), is(true));
    assumeThat(_genMat.n_elem, is(_elemInds.n_elem));

    _genRowVec.cols(_elemInds, Op.MINUS, _genMat);

    assertMatEquals(_genRowVec, load("row.colsMinus"));
  }

  @Test
  public void testRowColsElemTimes() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genMat.is_rowvec(), is(true));
    assumeThat(_genMat.n_elem, is(_elemInds.n_elem));

    _genRowVec.cols(_elemInds, Op.ELEMTIMES, _genMat);

    assertMatEquals(_genRowVec, load("row.colsElemTimes"));
  }

  @Test
  public void testRowColsElemDivide() throws IOException {
    for (int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genRowVec.in_range((int) _elemInds.at(n)), is(true));
    }
    assumeThat(_genMat.is_rowvec(), is(true));
    assumeThat(_genMat.n_elem, is(_elemInds.n_elem));

    _genRowVec.cols(_elemInds, Op.ELEMDIVIDE, _genMat);

    assertMatEquals(_genRowVec, load("row.colsElemDivide"));
  }

}
