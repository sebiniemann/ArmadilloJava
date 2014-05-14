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
import static org.hamcrest.Matchers.lessThan;

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
public class TestInPlaceGenMatRowIndsGenMat extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowInds = {2}, GenMat = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowInds);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatAString;

  @Parameter(1)
  public Mat    _genMatA;

  protected Mat _copyOfGenMatA;

  @Parameter(2)
  public String _rowIndsString;

  @Parameter(3)
  public Col    _rowInds;

  protected Col _copyOfRowInds;

  @Parameter(4)
  public String _genMatBString;

  @Parameter(5)
  public Mat    _genMatB;

  protected Mat _copyOfGenMatB;

  @Before
  public void before() {
    _fileSuffix = _genMatAString + "," + _rowIndsString + "," + _genMatBString;

    _copyOfGenMatA = new Mat(_genMatA);
    _copyOfRowInds = new Col(_rowInds);
    _copyOfGenMatB = new Mat(_genMatB);
  }

  @After
  public void after() {
    _genMatA.inPlace(Op.EQUAL, _copyOfGenMatA);
    _rowInds.inPlace(Op.EQUAL, _copyOfRowInds);
    _genMatB.inPlace(Op.EQUAL, _copyOfGenMatB);
  }

  @Test
  public void testMatRowsEqual() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMatA.n_rows)));
    }
    assumeThat(_genMatB.n_rows, is(_rowInds.n_elem));
    assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));

    _genMatA.rows(_rowInds, Op.EQUAL, _genMatB);

    assertMatEquals(_genMatA, load("Mat.rowsEqual"));
  }

  @Test
  public void testMatRowsPlus() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMatA.n_rows)));
    }
    assumeThat(_genMatB.n_rows, is(_rowInds.n_elem));
    assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));

    _genMatA.rows(_rowInds, Op.PLUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.rowsPlus"));
  }

  @Test
  public void testMatRowsMinus() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMatA.n_rows)));
    }
    assumeThat(_genMatB.n_rows, is(_rowInds.n_elem));
    assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));

    _genMatA.rows(_rowInds, Op.MINUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.rowsMinus"));
  }

  @Test
  public void testMatRowsElemTimes() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMatA.n_rows)));
    }
    assumeThat(_genMatB.n_rows, is(_rowInds.n_elem));
    assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));

    _genMatA.rows(_rowInds, Op.ELEMTIMES, _genMatB);

    assertMatEquals(_genMatA, load("Mat.rowsElemTimes"));
  }

  @Test
  public void testMatRowsElemDivide() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMatA.n_rows)));
    }
    assumeThat(_genMatB.n_rows, is(_rowInds.n_elem));
    assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));

    _genMatA.rows(_rowInds, Op.ELEMDIVIDE, _genMatB);

    assertMatEquals(_genMatA, load("Mat.rowsElemDivide"));
  }

  @Test
  public void testMatEach_rowEqual() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMatA.n_rows)));
    }
    assumeThat(_genMatB.is_rowvec(), is(true));
    assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));

    _genMatA.each_row(_rowInds, Op.EQUAL, _genMatB);

    assertMatEquals(_genMatA, load("Mat.each_rowEqual"));
  }

  @Test
  public void testMatEach_rowPlus() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMatA.n_rows)));
    }
    assumeThat(_genMatB.is_rowvec(), is(true));
    assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));

    _genMatA.each_row(_rowInds, Op.PLUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.each_rowPlus"));
  }

  @Test
  public void testMatEach_rowMinus() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMatA.n_rows)));
    }
    assumeThat(_genMatB.is_rowvec(), is(true));
    assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));

    _genMatA.each_row(_rowInds, Op.MINUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.each_rowMinus"));
  }

  @Test
  public void testMatEach_rowElemTimes() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMatA.n_rows)));
    }
    assumeThat(_genMatB.is_rowvec(), is(true));
    assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));

    _genMatA.each_row(_rowInds, Op.ELEMTIMES, _genMatB);

    assertMatEquals(_genMatA, load("Mat.each_rowElemTimes"));
  }

  @Test
  public void testMatEach_rowElemDivide() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMatA.n_rows)));
    }
    assumeThat(_genMatB.is_rowvec(), is(true));
    assumeThat(_genMatB.n_cols, is(_genMatA.n_cols));

    _genMatA.each_row(_rowInds, Op.ELEMDIVIDE, _genMatB);

    assertMatEquals(_genMatA, load("Mat.each_rowElemDivide"));
  }

}
