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
public class TestInPlaceGenColVecElemIndGenMat extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, ElemInd = {2}, GenMat = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.ElemInd);
    inputClasses.add(InputClass.GenMat);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genColVecString;

  @Parameter(1)
  public Col       _genColVec;

  protected Col    _copyOfGenColVec;

  @Parameter(2)
  public String    _elemIndString;

  @Parameter(3)
  public int       _elemInd;

  protected int    _copyOfElemInd;

  @Parameter(4)
  public String    _genMatString;

  @Parameter(5)
  public Mat    _genMat;

  protected Mat _copyOfGenMat;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _elemIndString + "," + _genMatString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfElemInd = new Integer(_elemInd);
    _copyOfGenMat = new Mat(_genMat);
  }

  @After
  public void after() {
    _genColVec.inPlace(Op.EQUAL, _copyOfGenColVec);
    _elemInd = new Integer(_copyOfElemInd);
    _genMat = new Mat(_copyOfGenMat);
  }

  @Test
  public void testColVecRowEqual() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genColVec.n_elem)));
    assumeThat(_genMat.is_rowvec(), is(true));
    assumeThat(_genMat.n_cols, is(_genColVec.n_cols));

    _genColVec.row(_elemInd, Op.EQUAL, _genMat);

    assertMatEquals(_genColVec, load("Col.rowEqual"));
  }
  
  @Test
  public void testColVecRowPlus() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genColVec.n_elem)));
    assumeThat(_genMat.is_rowvec(), is(true));
    assumeThat(_genMat.n_cols, is(_genColVec.n_cols));

    _genColVec.row(_elemInd, Op.PLUS, _genMat);

    assertMatEquals(_genColVec, load("Col.rowPlus"));
  }

  @Test
  public void testColVecRowMinus() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genColVec.n_elem)));
    assumeThat(_genMat.is_rowvec(), is(true));
    assumeThat(_genMat.n_cols, is(_genColVec.n_cols));

    _genColVec.row(_elemInd, Op.MINUS, _genMat);

    assertMatEquals(_genColVec, load("Col.rowMinus"));
  }
  
  @Test
  public void testColVecInsertRows() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genColVec.n_elem)));
    assumeThat(_genMat.is_rowvec(), is(true));
    assumeThat(_genMat.n_cols, is(_genColVec.n_cols));

    _genColVec.insert_rows(_elemInd, _genMat);

    assertMatEquals(_genColVec, load("Col.insertRows"));
  }

}
