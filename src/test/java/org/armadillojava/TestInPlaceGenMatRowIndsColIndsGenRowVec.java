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
public class TestInPlaceGenMatRowIndsColIndsGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowInds = {2}, ColInds = {4}, GenRowVec = {6}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowInds);
    inputClasses.add(InputClass.ColInds);
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
  public String _colIndsString;

  @Parameter(5)
  public Col    _colInds;

  protected Col _copyOfColInds;

  @Parameter(6)
  public String _genRowVecString;

  @Parameter(7)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _rowIndsString + "," + _colIndsString + "," + _genRowVecString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRowInds = new Col(_rowInds);
    _copyOfColInds = new Col(_colInds);
    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _rowInds.inPlace(Op.EQUAL, _copyOfRowInds);
    _colInds.inPlace(Op.EQUAL, _copyOfColInds);
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
  }

  @Test
  public void testMatSubmatEqual() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      for(int j = 0; j < _colInds.n_elem; j++) {
        assumeThat(_genMat.in_range((int) _rowInds.at(i), (int) _colInds.at(j)), is(true));
      }
    }
    assumeThat(_genRowVec.n_rows, is(_rowInds.n_elem));
    assumeThat(_genRowVec.n_cols, is(_colInds.n_elem));

    _genMat.submat(_rowInds, _colInds, Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatEqual"));
  }

  @Test
  public void testMatSubmatPlus() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      for(int j = 0; j < _colInds.n_elem; j++) {
        assumeThat(_genMat.in_range((int) _rowInds.at(i), (int) _colInds.at(j)), is(true));
      }
    }
    assumeThat(_genRowVec.n_rows, is(_rowInds.n_elem));
    assumeThat(_genRowVec.n_cols, is(_colInds.n_elem));

    _genMat.submat(_rowInds, _colInds, Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatPlus"));
  }

  @Test
  public void testMatSubmatMinus() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      for(int j = 0; j < _colInds.n_elem; j++) {
        assumeThat(_genMat.in_range((int) _rowInds.at(i), (int) _colInds.at(j)), is(true));
      }
    }
    assumeThat(_genRowVec.n_rows, is(_rowInds.n_elem));
    assumeThat(_genRowVec.n_cols, is(_colInds.n_elem));

    _genMat.submat(_rowInds, _colInds, Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatMinus"));
  }

  @Test
  public void testMatSubmatElemTimes() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      for(int j = 0; j < _colInds.n_elem; j++) {
        assumeThat(_genMat.in_range((int) _rowInds.at(i), (int) _colInds.at(j)), is(true));
      }
    }
    assumeThat(_genRowVec.n_rows, is(_rowInds.n_elem));
    assumeThat(_genRowVec.n_cols, is(_colInds.n_elem));

    _genMat.submat(_rowInds, _colInds, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatElemTimes"));
  }

  @Test
  public void testMatSubmatElemDivide() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      for(int j = 0; j < _colInds.n_elem; j++) {
        assumeThat(_genMat.in_range((int) _rowInds.at(i), (int) _colInds.at(j)), is(true));
      }
    }
    assumeThat(_genRowVec.n_rows, is(_rowInds.n_elem));
    assumeThat(_genRowVec.n_cols, is(_colInds.n_elem));

    _genMat.submat(_rowInds, _colInds, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.submatElemDivide"));
  }

}
