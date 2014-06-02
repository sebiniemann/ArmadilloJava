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
public class TestInPlaceGenColVec extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genColVecString;

  @Parameter(1)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;

  @Before
  public void before() {
    _fileSuffix = _genColVecString;

    _copyOfGenColVec = new Col(_genColVec);
  }

  @After
  public void after() {
    _genColVec.inPlace(Op.EQUAL, _copyOfGenColVec);
  }
 
  @Test
  public void testColOnes() throws IOException {
	_genColVec.ones();
	
	assertMatEquals( _genColVec , load("Col.ones"));
  }
  
  @Test
  public void testColZeros() throws IOException {
	_genColVec.zeros();
	
    assertMatEquals( _genColVec , load("Col.zeros"));
  }
  
  @Test
  public void testColReset() throws IOException {
	_genColVec.reset();
	
    assertMatEquals( _genColVec , load("Col.reset"));
  }
  
  @Test
  public void testColClear() throws IOException {
	_genColVec.clear();
	
    assertMatEquals( _genColVec , load("Col.clear"));
  }
  
  @Test
  public void testColInPlaceIncrement() throws IOException {
	_genColVec.inPlace(Op.INCREMENT);
	
    assertMatEquals( _genColVec , load("Col.inPlaceIncrement"));
  }
  
  @Test
  public void testColInPlaceDecrement() throws IOException {
	_genColVec.inPlace(Op.DECREMENT);
	
    assertMatEquals( _genColVec , load("Col.inPlaceDecrement"));
  }
}
