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
public class TestInPlaceGenMatRowIndsColIndsGenDouble extends TestClass {

  @Parameters(name = "{index}: GenMat = {0}, RowInds = {2}, ColInds = {4}, GenDouble = {6}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenMat);
    inputClasses.add(InputClass.RowInds);
    inputClasses.add(InputClass.ColInds);
    inputClasses.add(InputClass.GenDouble);

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
  public String _genDoubleString;

  @Parameter(7)
  public double    _genDouble;

  protected double _copyOfGenDouble;

  @Before
  public void before() {
    _fileSuffix = _genMatString + "," + _rowIndsString + "," + _colIndsString + "," + _genDoubleString;

    _copyOfGenMat = new Mat(_genMat);
    _copyOfRowInds = new Col(_rowInds);
    _copyOfColInds = new Col(_colInds);
    _copyOfGenDouble = new Double(_genDouble);
  }

  @After
  public void after() {
    _genMat.inPlace(Op.EQUAL, _copyOfGenMat);
    _rowInds.inPlace(Op.EQUAL, _copyOfRowInds);
    _colInds.inPlace(Op.EQUAL, _copyOfColInds);
    _genDouble = new Double(_copyOfGenDouble);
  }

  @Test
  public void testMatSubmatPlus() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      for(int j = 0; j < _colInds.n_elem; j++) {
        assumeThat(_genMat.in_range((int) _rowInds.at(i), (int) _colInds.at(j)), is(true));
      }
    }

    _genMat.submat(_rowInds, _colInds, Op.PLUS, _genDouble);

    assertMatEquals(_genMat, load("Mat.submatPlus"));
  }

  @Test
  public void testMatSubmatMinus() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      for(int j = 0; j < _colInds.n_elem; j++) {
        assumeThat(_genMat.in_range((int) _rowInds.at(i), (int) _colInds.at(j)), is(true));
      }
    }

    _genMat.submat(_rowInds, _colInds, Op.MINUS, _genDouble);

    assertMatEquals(_genMat, load("Mat.submatMinus"));
  }

  @Test
  public void testMatSubmatTimes() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      for(int j = 0; j < _colInds.n_elem; j++) {
        assumeThat(_genMat.in_range((int) _rowInds.at(i), (int) _colInds.at(j)), is(true));
      }
    }

    _genMat.submat(_rowInds, _colInds, Op.TIMES, _genDouble);

    assertMatEquals(_genMat, load("Mat.submatTimes"));
  }

  @Test
  public void testMatSubmatDivide() throws IOException {
    for(int i = 0; i < _rowInds.n_elem; i++) {
      for(int j = 0; j < _colInds.n_elem; j++) {
        assumeThat(_genMat.in_range((int) _rowInds.at(i), (int) _colInds.at(j)), is(true));
      }
    }

    _genMat.submat(_rowInds, _colInds, Op.DIVIDE, _genDouble);

    assertMatEquals(_genMat, load("Mat.submatDivide"));
  }

}
