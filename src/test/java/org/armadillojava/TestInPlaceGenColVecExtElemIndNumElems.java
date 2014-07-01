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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.armadillojava.TestUtil.assertMatEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

@RunWith(Parameterized.class)
public class TestInPlaceGenColVecExtElemIndNumElems extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, ExtElemInd = {2}, NumElems = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.ExtElemInd);
    inputClasses.add(InputClass.NumElems);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String    _genColVecString;

  @Parameter(1)
  public Col       _genColVec;

  protected Col    _copyOfGenColVec;

  @Parameter(2)
  public String    _extElemIndString;

  @Parameter(3)
  public int       _extElemInd;

  protected int    _copyOfExtElemInd;

  @Parameter(4)
  public String    _numElemsString;

  @Parameter(5)
  public int	_numElems;

  protected int _copyOfNumElems;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _extElemIndString + "," + _numElemsString;

    _copyOfGenColVec = new Col(_genColVec);
      _copyOfExtElemInd = new Integer(_extElemInd);
    _copyOfNumElems = _numElems;
  }

  @After
  public void after() {
    _genColVec.inPlace(Op.EQUAL, _copyOfGenColVec);
    _extElemInd = new Integer(_copyOfExtElemInd);
    _numElems = _copyOfNumElems;
  }

  @Test
  public void testColVecInsertRows() throws IOException {
    assumeThat(_extElemInd, is(lessThanOrEqualTo(_genColVec.n_elem)));

    _genColVec.insert_rows(_extElemInd, _numElems);

    assertMatEquals(_genColVec, load("Col.insert_rows"));
  }
  
  @Test
  public void testColVecInsertRowsTrue() throws IOException {
    assumeThat(_extElemInd, is(lessThanOrEqualTo(_genColVec.n_elem)));

    _genColVec.insert_rows(_extElemInd, _numElems, true);

    assertMatEquals(_genColVec, load("Col.insert_rows"));
  }
  
  @Test
  public void testColVecInsertRowsFalse() throws IOException {
    assumeThat(_extElemInd, is(lessThanOrEqualTo(_genColVec.n_elem)));

    _genColVec.insert_rows(_extElemInd, _numElems, false);
    assertThat(_genColVec.n_elem, is(_copyOfGenColVec.n_elem + _numElems));

    _genColVec.shed_rows(_extElemInd, _extElemInd + _numElems - 1);
    assertMatEquals(_genColVec, _copyOfGenColVec);
  }

}