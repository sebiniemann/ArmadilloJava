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
public class TestInPlaceGenMatRowIndsColIndsGenMat extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowInds = {2}, ColInds = {4}, GenMat = {6}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowInds);
    inputClasses.add(InputClass.ColInds);
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
  public String _colIndsString;

  @Parameter(5)
  public Col    _colInds;

  protected Col _copyOfColInds;

  @Parameter(6)
  public String _genMatBString;

  @Parameter(7)
  public Mat    _genMatB;

  protected Mat _copyOfGenMatB;

  @Before
  public void before() {
    _fileSuffix = _genMatAString + "," + _rowIndsString + "," + _colIndsString + "," + _genMatBString;

    _copyOfGenMatA = new Mat(_genMatA);
    _copyOfRowInds = new Col(_rowInds);
    _copyOfColInds = new Col(_colInds);
    _copyOfGenMatB = new Mat(_genMatB);
  }

  @After
  public void after() {
    _genMatA.inPlace(Op.EQUAL, _copyOfGenMatA);
    _rowInds.inPlace(Op.EQUAL, _copyOfRowInds);
    _colInds.inPlace(Op.EQUAL, _copyOfColInds);
    _genMatB.inPlace(Op.EQUAL, _copyOfGenMatB);
  }

  @Test
  public void testMatSubmatEqual() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      for(int j = 0; j < _colInds.n_elem; j++) {
        assumeThat(_genMatA.in_range((int) _rowInds.at(i), (int) _colInds.at(j)), is(true));
      }
    }
    assumeThat(_genMatB.n_rows, is(_rowInds.n_elem));
    assumeThat(_genMatB.n_cols, is(_colInds.n_elem));

    _genMatA.submat(_rowInds, _colInds, Op.EQUAL, _genMatB);

    assertMatEquals(_genMatA, load("Mat.submatEqual"));
  }

  @Test
  public void testMatSubmatPlus() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      for(int j = 0; j < _colInds.n_elem; j++) {
        assumeThat(_genMatA.in_range((int) _rowInds.at(i), (int) _colInds.at(j)), is(true));
      }
    }
    assumeThat(_genMatB.n_rows, is(_rowInds.n_elem));
    assumeThat(_genMatB.n_cols, is(_colInds.n_elem));

    _genMatA.submat(_rowInds, _colInds, Op.PLUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.submatPlus"));
  }

  @Test
  public void testMatSubmatMinus() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      for(int j = 0; j < _colInds.n_elem; j++) {
        assumeThat(_genMatA.in_range((int) _rowInds.at(i), (int) _colInds.at(j)), is(true));
      }
    }
    assumeThat(_genMatB.n_rows, is(_rowInds.n_elem));
    assumeThat(_genMatB.n_cols, is(_colInds.n_elem));

    _genMatA.submat(_rowInds, _colInds, Op.MINUS, _genMatB);

    assertMatEquals(_genMatA, load("Mat.submatMinus"));
  }

  @Test
  public void testMatSubmatElemTimes() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      for(int j = 0; j < _colInds.n_elem; j++) {
        assumeThat(_genMatA.in_range((int) _rowInds.at(i), (int) _colInds.at(j)), is(true));
      }
    }
    assumeThat(_genMatB.n_rows, is(_rowInds.n_elem));
    assumeThat(_genMatB.n_cols, is(_colInds.n_elem));

    _genMatA.submat(_rowInds, _colInds, Op.ELEMTIMES, _genMatB);

    assertMatEquals(_genMatA, load("Mat.submatElemTimes"));
  }

  @Test
  public void testMatSubmatElemDivide() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      for(int j = 0; j < _colInds.n_elem; j++) {
        assumeThat(_genMatA.in_range((int) _rowInds.at(i), (int) _colInds.at(j)), is(true));
      }
    }
    assumeThat(_genMatB.n_rows, is(_rowInds.n_elem));
    assumeThat(_genMatB.n_cols, is(_colInds.n_elem));

    _genMatA.submat(_rowInds, _colInds, Op.ELEMDIVIDE, _genMatB);

    assertMatEquals(_genMatA, load("Mat.submatElemDivide"));
  }

}
