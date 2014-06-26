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
public class TestInPlaceGenRowVecElemInd extends TestClass {

  @Parameters(name = "{index}: GenRowVec = {0}, ElemInd = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);
    inputClasses.add(InputClass.ElemInd);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Parameter(2)
  public String _elemIndString;

  @Parameter(3)
  public int    _elemInd;

  protected int _copyOfElemInd;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString + "," + _elemIndString;

    _copyOfGenRowVec = new Row(_genRowVec);
    _copyOfElemInd = new Integer(_elemInd);
  }

  @After
  public void after() {
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
    _elemInd = new Integer(_copyOfElemInd);
  }

  @Test
  public void testRowVecAtIncrement() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));

    _genRowVec.at(_elemInd, Op.INCREMENT);

    assertMatEquals(_genRowVec, load("Row.atIncrement"));
  }

  @Test
  public void testRowVecAtDecrement() throws IOException {
    assumeThat(_elemInd, is(lessThan(_genRowVec.n_elem)));

    _genRowVec.at(_elemInd, Op.DECREMENT);

    assertMatEquals(_genRowVec, load("Row.atDecrement"));
  }
  
  @Test
  public void testRowVecShed_col() throws IOException {
	assumeThat(_elemInd, is(lessThan(_genRowVec.n_cols)));
	  
	_genRowVec.shed_col(_elemInd);
    assertMatEquals(_genRowVec,load("Row.shed_col"));
  }

}