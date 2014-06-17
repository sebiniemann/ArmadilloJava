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
public class TestInPlaceGenRowVec extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenRowVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genRowVecString;

  @Parameter(1)
  public Row    _genRowVec;

  protected Row _copyOfGenRowVec;

  @Before
  public void before() {
    _fileSuffix = _genRowVecString;

    _copyOfGenRowVec = new Row(_genRowVec);
  }

  @After
  public void after() {
    _genRowVec.inPlace(Op.EQUAL, _copyOfGenRowVec);
  }
 
  @Test
  public void testRowOnes() throws IOException {
	_genRowVec.ones();
	
	assertMatEquals( _genRowVec , load("Row.ones"));
  }
  
  @Test
  public void testRowZeros() throws IOException {
	_genRowVec.zeros();
	
    assertMatEquals( _genRowVec , load("Row.zeros"));
  }
  
  @Test
  public void testRowInPlaceIncrement() throws IOException {
	_genRowVec.inPlace(Op.INCREMENT);
	
    assertMatEquals( _genRowVec , load("Row.inPlaceIncrement"));
  }
  
  @Test
  public void testRowInPlaceDecrement() throws IOException {
	_genRowVec.inPlace(Op.DECREMENT);
	
    assertMatEquals( _genRowVec , load("Row.inPlaceDecrement"));
  }
}
