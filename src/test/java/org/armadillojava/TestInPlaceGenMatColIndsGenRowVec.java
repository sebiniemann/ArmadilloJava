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
public class TestInPlaceGenMatColIndsGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, ColInds = {2}, GenRowVec = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
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
  public String _colIndsString;

  @Parameter(3)
  public Col    _colInds;

  protected Col _copyOfColInds;

  @Parameter(4)
  public String _genRowVecString;

  @Parameter(5)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _colIndsString + "," + _genRowVecString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfColInds = new Col(_colInds);
    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _colInds.inPlace(Op.EQUAL, _copyOfColInds);
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
  }

  @Test
  public void testMatColsEqual() throws IOException {
    for(int j = 0; j < _colInds.n_elem; j++) {
      assumeThat((int) _colInds.at(j), is(lessThan(_genMat.n_cols)));
    }
    assumeThat(_genRowVec.n_cols, is(_colInds.n_elem));
    assumeThat(_genMat.n_rows, is(1));
    

    _genMat.cols(_colInds, Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.colsEqual"));
  }

  @Test
  public void testMatColsPlus() throws IOException {
    for(int j = 0; j < _colInds.n_elem; j++) {
      assumeThat((int) _colInds.at(j), is(lessThan(_genMat.n_cols)));
    }
    assumeThat(_genRowVec.n_cols, is(_colInds.n_elem));
    assumeThat(_genMat.n_rows, is(1));

    _genMat.cols(_colInds, Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.colsPlus"));
  }

  @Test
  public void testMatColsMinus() throws IOException {
    for(int j = 0; j < _colInds.n_elem; j++) {
      assumeThat((int) _colInds.at(j), is(lessThan(_genMat.n_cols)));
    }
    assumeThat(_genRowVec.n_cols, is(_colInds.n_elem));
    assumeThat(_genMat.n_rows, is(1));

    _genMat.cols(_colInds, Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.colsMinus"));
  }

  @Test
  public void testMatColsElemTimes() throws IOException {
    for(int j = 0; j < _colInds.n_elem; j++) {
      assumeThat((int) _colInds.at(j), is(lessThan(_genMat.n_cols)));
    }
    assumeThat(_genRowVec.n_cols, is(_colInds.n_elem));
    assumeThat(_genMat.n_rows, is(1));

    _genMat.cols(_colInds, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.colsElemTimes"));
  }

  @Test
  public void testMatColsElemDivide() throws IOException {
    for(int j = 0; j < _colInds.n_elem; j++) {
      assumeThat((int) _colInds.at(j), is(lessThan(_genMat.n_cols)));
    }
    assumeThat(_genRowVec.n_cols, is(_colInds.n_elem));
    assumeThat(_genMat.n_rows, is(1));

    _genMat.cols(_colInds, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.colsElemDivide"));
  }

  @Test
  public void testMatEach_colEqual() throws IOException {
    for(int j = 0; j < _colInds.n_elem; j++) {
      assumeThat((int) _colInds.at(j), is(lessThan(_genMat.n_cols)));
    }
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genMat.n_rows, is(1));

    _genMat.each_col(_colInds, Op.EQUAL, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_colEqual"));
  }

  @Test
  public void testMatEach_colPlus() throws IOException {
    for(int j = 0; j < _colInds.n_elem; j++) {
      assumeThat((int) _colInds.at(j), is(lessThan(_genMat.n_cols)));
    }
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genMat.n_rows, is(1));

    _genMat.each_col(_colInds, Op.PLUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_colPlus"));
  }

  @Test
  public void testMatEach_colMinus() throws IOException {
    for(int j = 0; j < _colInds.n_elem; j++) {
      assumeThat((int) _colInds.at(j), is(lessThan(_genMat.n_cols)));
    }
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genMat.n_rows, is(1));

    _genMat.each_col(_colInds, Op.MINUS, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_colMinus"));
  }

  @Test
  public void testMatEach_colElemTimes() throws IOException {
    for(int j = 0; j < _colInds.n_elem; j++) {
      assumeThat((int) _colInds.at(j), is(lessThan(_genMat.n_cols)));
    }
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genMat.n_rows, is(1));

    _genMat.each_col(_colInds, Op.ELEMTIMES, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_colElemTimes"));
  }

  @Test
  public void testMatEach_colElemDivide() throws IOException {
    for(int j = 0; j < _colInds.n_elem; j++) {
      assumeThat((int) _colInds.at(j), is(lessThan(_genMat.n_cols)));
    }
    assumeThat(_genRowVec.is_colvec(), is(true));
    assumeThat(_genMat.n_rows, is(1));

    _genMat.each_col(_colInds, Op.ELEMDIVIDE, _genRowVec);

    assertMatEquals(_genMat, load("Mat.each_colElemDivide"));
  }

}
