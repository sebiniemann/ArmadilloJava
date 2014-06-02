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
public class TestInPlaceGenColVecElemInd extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, ElemInd = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.ElemInd);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genColVecString;

  @Parameter(1)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;

  @Parameter(2)
  public String _elemIndString;

  @Parameter(3)
  public int    _elemInd;

  protected int _copyOfElemInd;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _elemIndString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfElemInd = new Integer(_elemInd);
  }

  @After
  public void after() {
    _genColVec.inPlace(Op.EQUAL, _copyOfGenColVec);
    _elemInd = new Integer(_copyOfElemInd);
  }

  @Test
  public void testColVecAtIncrement() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genColVec.n_elem)));

    _genColVec.at(_elemInd, Op.INCREMENT);

    assertMatEquals(_genColVec, load("Col.atIncrement"));
  }

  @Test
  public void testColVecAtDecrement() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genColVec.n_elem)));

    _genColVec.at(_elemInd, Op.DECREMENT);

    assertMatEquals(_genColVec, load("Col.atDecrement"));
  }
  
  @Test
  public void testColVecShed_row() throws IOException {
	assumeThat(_elemInd, is(lessThan(_genColVec.n_rows)));
	  
	_genColVec.shed_row(_elemInd);
    assertMatEquals(_genColVec,load("Col.shed_row"));
  }

}