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
public class TestGenColVecElemInds extends TestClass {

  @Parameters(name = "{index}: GenColVec = {0}, ElemInds = {2}")
  public static Collection<Object[]> getParameters() {
    List<InputClass> inputClasses = new ArrayList<>();

    inputClasses.add(InputClass.GenColVec);
    inputClasses.add(InputClass.ElemInds);

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
 

  @Before
  public void before() {
    _fileSuffix = _genColVecString + "," + _elemIndsString ;

    _copyOfGenColVec = new Col(_genColVec);
    _copyOfElemInds = new Col(_elemInds);
  }

  @After
  public void after() {
    assertMatEquals(_genColVec, _copyOfGenColVec, 0);
    assertMatEquals(_elemInds, _copyOfElemInds, 0);
  }

  @Test
  public void testMatElem() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat((int) _elemInds.at(n), is(lessThan(_genColVec.n_elem)));
    }

    assertMatEquals(_genColVec.elem(_elemInds), load("Col.elem"));
  }
  
  @Test
  public void testMatRows() throws IOException {
    for(int n = 0; n < _elemInds.n_elem; n++) {
      assumeThat((int) _elemInds.at(n), is(lessThan(_genColVec.n_elem)));
    }

    assertMatEquals(_genColVec.rows(_elemInds), load("Col.rows"));
  }

}
