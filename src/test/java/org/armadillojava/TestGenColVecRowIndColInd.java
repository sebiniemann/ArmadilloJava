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
import static org.junit.Assert.assertThat;
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
public class TestGenColVecRowIndColInd extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, RowInd = {2}, ColInd = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.RowInd);
    inputClasses.add(InputClass.ColInd);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genColVecString;

  @Parameter(1)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;

  @Parameter(2)
  public String _rowIndString;

  @Parameter(3)
  public int    _rowInd;

  protected int _copyOfRowInd;

  @Parameter(4)
  public String _colIndString;

  @Parameter(5)
  public int    _colInd;

  protected int _copyOfColInd;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _rowIndString + "," + _colIndString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfRowInd = new Integer(_rowInd);
    _copyOfColInd = new Integer(_colInd);
  }

  @After
  public void after() {
    assertMatEquals(_genColVec, _copyOfGenColVec, 0);
    assertThat(_rowInd, is(_copyOfRowInd));
    assertThat(_colInd, is(_copyOfColInd));
  }

  @Test
  public void testColIn_range() throws IOException {
    double expected = load("Col.in_range")._data[0];

    if (_genColVec.in_range(_rowInd, _colInd)) {
      assertThat(1.0, is(expected));
    } else {
      assertThat(0.0, is(expected));
    }
  }

}
