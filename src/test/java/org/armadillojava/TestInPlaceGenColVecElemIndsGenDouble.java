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
public class TestInPlaceGenColVecElemIndsGenDouble extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, ElemInds = {2}, GenDouble = {4}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.ElemInds);
    inputClasses.add(InputClass.GenDouble);

    return Input.getTestParameters(inputClasses);
  }

  @Parameter(0)
  public String _genColVecString;

  @Parameter(1)
  public Col    _genColVec;

  protected Col _copyOfGenColVec;

  @Parameter(2)
  public String _elemIndsString;

  @Parameter(3)
  public Col    _elemInds;

  protected Col _copyOfElemInds;
  
  @Parameter(4)
  public String _genDoubleString;

  @Parameter(5)
  public Double    _genDouble;

  protected Double _copyOfGenDouble;

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _elemIndsString + "," +_genDoubleString;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfElemInds = new Col(_elemInds);
    _copyOfGenDouble = new Double(_genDouble);
  }

  @After
  public void after() {
	_genColVec.inPlace(Op.EQUAL, _copyOfGenColVec);
	_elemInds = new Col(_copyOfElemInds);
	_genDouble = new Double(_copyOfGenDouble);
  }

  @Test
  public void testColElemPlus() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }

    _genColVec.elem(_elemInds, Op.PLUS, _genDouble);
    
    assertMatEquals(_genColVec, load("Col.elemPlus"));
  }
  
  @Test
  public void testColElemMinus() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }

    _genColVec.elem(_elemInds, Op.MINUS, _genDouble);
    
    assertMatEquals(_genColVec, load("Col.elemMinus"));
  }
  
  @Test
  public void testColElemTimes() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }

    _genColVec.elem(_elemInds, Op.TIMES, _genDouble);
    
    assertMatEquals(_genColVec, load("Col.elemTimes"));
  }
  
  @Test
  public void testColElemDivide() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }

    _genColVec.elem(_elemInds, Op.DIVIDE, _genDouble);
    
    assertMatEquals(_genColVec, load("Col.elemDivide"));
  }
  
  @Test
  public void testColRowsPlus() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }

    _genColVec.rows(_elemInds, Op.PLUS, _genDouble);
    
    assertMatEquals(_genColVec, load("Col.rowsPlus"));
  }
  
  @Test
  public void testColRowsMinus() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }

    _genColVec.rows(_elemInds, Op.MINUS, _genDouble);
    
    assertMatEquals(_genColVec, load("Col.rowsMinus"));
  }

  @Test
  public void testColRowsTimes() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }

    _genColVec.rows(_elemInds, Op.TIMES, _genDouble);
    
    assertMatEquals(_genColVec, load("Col.rowsTimes"));
  }
  
  @Test
  public void testColRowsDivide() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat(_genColVec.in_range((int) _elemInds.at(n)), is(true));
    }

    _genColVec.rows(_elemInds, Op.DIVIDE, _genDouble);
    
    assertMatEquals(_genColVec, load("Col.rowsDivide"));
  }
}
