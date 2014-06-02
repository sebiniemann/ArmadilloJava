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
public class TestInPlaceGenColVecNumElems extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, NumElems = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.NumElems);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genColVecString;

  @Parameter(1)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;

  @Parameter(2)
  public String _numElemsString;

  @Parameter(3)
  public int    _numElems;

  protected int _copyOfNumElems;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _numElemsString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfNumElems = new Integer(_numElems);
  }

  @After
  public void after() {
    _genColVec.inPlace(Op.EQUAL, _copyOfGenColVec);
    _numElems = new Integer(_copyOfNumElems);
  }

  @Test
  public void testColVecOnes() throws IOException {

	_genColVec.ones(_numElems);

    assertMatEquals(_genColVec, load("Col.ones"));
  }

  @Test
  public void testColVecZeros() throws IOException {

    _genColVec.zeros(_numElems);

    assertMatEquals(_genColVec, load("Col.zeros"));
  }
  
  @Test
  public void testColVecResize() throws IOException {
	  
	_genColVec.resize(_numElems);
	
    assertMatEquals(_genColVec,load("Col.resize"));
  }
  
  @Test
  public void testColVecSetsize() throws IOException {
	  
	_genColVec.set_size(_numElems);
	
    assertMatEquals(_genColVec,load("Col.setSize"));
  }
  
  @Test
  public void testColVecCol() throws IOException {
	  
	_genColVec = new Col(_numElems);
	
    assertMatEquals(_genColVec,load("Col.col"));
  }

}