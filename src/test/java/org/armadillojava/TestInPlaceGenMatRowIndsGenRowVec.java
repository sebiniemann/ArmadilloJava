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
public class TestInPlaceGenMatRowIndsGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowInds = {2}, GenRowVec = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowInds);
    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genMatString;

  @Parameter(1)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Parameter(2)
  public String _rowIndsString;

  @Parameter(3)
  public Col    _rowInds;

  protected Col _copyOfRowInds;

  @Parameter(4)
  public String _genRowVecString;

  @Parameter(5)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _rowIndsString + "," + _genRowVecString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRowInds = new Col(_rowInds);
    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _rowInds.inPlace(Op.EQUAL, _copyOfRowInds);
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
  }

  @Test
  public void testMatRowsEqual() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMat.n_rows)));
    }
    assumeThat(_genRowVec.n_rows, is(_rowInds.n_elem));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    _genMat.rows(_rowInds, Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowsEqual"));
  }

  @Test
  public void testMatRowsPlus() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMat.n_rows)));
    }
    assumeThat(_genRowVec.n_rows, is(_rowInds.n_elem));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    _genMat.rows(_rowInds, Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowsPlus"));
  }

  @Test
  public void testMatRowsMinus() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMat.n_rows)));
    }
    assumeThat(_genRowVec.n_rows, is(_rowInds.n_elem));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    _genMat.rows(_rowInds, Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowsMinus"));
  }

  @Test
  public void testMatRowsElemTimes() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMat.n_rows)));
    }
    assumeThat(_genRowVec.n_rows, is(_rowInds.n_elem));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    _genMat.rows(_rowInds, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowsElemTimes"));
  }

  @Test
  public void testMatRowsElemDivide() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMat.n_rows)));
    }
    assumeThat(_genRowVec.n_rows, is(_rowInds.n_elem));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    _genMat.rows(_rowInds, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.rowsElemDivide"));
  }

  @Test
  public void testMatEach_rowEqual() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMat.n_rows)));
    }
    assumeThat(_genRowVec.is_rowvec(), is(true));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    _genMat.each_row(_rowInds, Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_rowEqual"));
  }

  @Test
  public void testMatEach_rowPlus() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMat.n_rows)));
    }
    assumeThat(_genRowVec.is_rowvec(), is(true));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    _genMat.each_row(_rowInds, Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_rowPlus"));
  }

  @Test
  public void testMatEach_rowMinus() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMat.n_rows)));
    }
    assumeThat(_genRowVec.is_rowvec(), is(true));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    _genMat.each_row(_rowInds, Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_rowMinus"));
  }

  @Test
  public void testMatEach_rowElemTimes() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMat.n_rows)));
    }
    assumeThat(_genRowVec.is_rowvec(), is(true));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    _genMat.each_row(_rowInds, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_rowElemTimes"));
  }

  @Test
  public void testMatEach_rowElemDivide() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      assumeThat((int) _rowInds.at(i), is(lessThan(_genMat.n_rows)));
    }
    assumeThat(_genRowVec.is_rowvec(), is(true));
    assumeThat(_genRowVec.n_cols, is(_genMat.n_cols));

    _genMat.each_row(_rowInds, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_rowElemDivide"));
  }

}
